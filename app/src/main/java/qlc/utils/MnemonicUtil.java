package qlc.utils;

import android.content.res.AssetManager;

import com.stratagile.qlink.application.AppConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qlc.network.QlcException;

import static java.util.Arrays.copyOf;
import static java.util.Collections.unmodifiableMap;


public final class MnemonicUtil {
	
    private MnemonicUtil() {}
    
    public static List<String> seedToMnemonic(byte[] seed, MnemonicLanguage language) {
    	
        Checking.checkSeed(seed);

        int seedLength = seed.length * 8;
        byte[] seedWithChecksum = copyOf(seed, seed.length + 1);
        seedWithChecksum[seed.length] = checksum(seed);

        int checksumLength = seedLength / 32;
        int mnemonicSentenceLength = (seedLength + checksumLength) / 11;

        try {
        	List<String> ret = new ArrayList<>();
        	for (int i = 0; i < mnemonicSentenceLength; i++) {
        		ret.add(language.getWord(next11Bits(seedWithChecksum, i * 11)));
        	}
        	return ret;
        } finally {
            Helper.wipe(seedWithChecksum);
        }
    }

    public static byte[] mnemonicToSeed(List<String> mnemonic, MnemonicLanguage language) {
    	
    	Checking.check(!isValid(mnemonic, language), "Invalid mnemonic");
    	
        byte[] seedWithChecksum = extractSeedWithChecksum(mnemonic, language);
        try {
            return extractSeed(seedWithChecksum);
        } finally {
            Helper.wipe(seedWithChecksum);
        }
    }

    public static boolean isValid(List<String> mnemonic, MnemonicLanguage language) {
    	
        /*if (mnemonic.size() != 24 || !mnemonic.stream().allMatch(language::wordExists)) {
            return false;
        }*/
        if (mnemonic.size() != 24) {
            return false;
        }
        for (String m : mnemonic) {
        	if (!language.wordExists(m))
        		return false;
        }

        byte[] seedWithChecksum = extractSeedWithChecksum(mnemonic, language);
        byte[] seed = extractSeed(seedWithChecksum);

        byte expectedChecksum = checksum(seed);
        try {
            return expectedChecksum == seedWithChecksum[seedWithChecksum.length - 1];
        } finally {
            Helper.wipe(seedWithChecksum);
            Helper.wipe(seed);
        }
        
    }
    
    public static List<String> toList(String mnemonics) {
    	mnemonics = mnemonics.trim();
    	String[] strArr = mnemonics.split(" ");
    	return Arrays.asList(strArr);
    }

    private static byte[] extractSeedWithChecksum(List<String> mnemonic, MnemonicLanguage language) {
    	
        int mnemonicSentenceLength = mnemonic.size();

        int seedWithChecksumLength = mnemonicSentenceLength * 11;
        byte[] seedWithChecksum = new byte[(seedWithChecksumLength + 7) / 8];

        
        List<Integer> mnemonicIndexes = new ArrayList<>();
        for (String word: mnemonic) {
        	mnemonicIndexes.add(language.getIndex(word));
        }

        for (int i = 0; i < mnemonicSentenceLength; i++) {
        	writeNext11(seedWithChecksum, mnemonicIndexes.get(i), i * 11);
        }

        return seedWithChecksum;
    }

    private static byte[] extractSeed(byte[] seedWithChecksum) {
        return copyOf(seedWithChecksum, seedWithChecksum.length - 1);
    }

    private static byte checksum(final byte[] seed) {
        try {
            final byte[] hash = MessageDigest.getInstance("SHA-256").digest(seed);
            final byte firstByte = hash[0];
            Arrays.fill(hash, (byte) 0);
            return firstByte;
        } catch (NoSuchAlgorithmException e) {
        	throw new QlcException(Constants.EXCEPTION_CODE_1006, Constants.EXCEPTION_MSG_1006);
        }
    }

    private static int next11Bits(byte[] bytes, int offset) {
        final int skip = offset / 8;
        final int lowerBitsToRemove = (3 * 8 - 11) - (offset % 8);
        return (((int) bytes[skip] & 0xff) << 16 |
                ((int) bytes[skip + 1] & 0xff) << 8 |
                (lowerBitsToRemove < 8
                        ? (int) bytes[skip + 2] & 0xff
                        : 0)) >> lowerBitsToRemove & (1 << 11) - 1;
    }

    private static void writeNext11(byte[] bytes, int value, int offset) {
        int skip = offset / 8;
        int bitSkip = offset % 8;
        {//byte 0
            byte firstValue = bytes[skip];
            byte toWrite = (byte) (value >> (3 + bitSkip));
            bytes[skip] = (byte) (firstValue | toWrite);
        }

        {//byte 1
            byte valueInByte = bytes[skip + 1];
            final int i = 5 - bitSkip;
            byte toWrite = (byte) (i > 0 ? value << i : value >> -i);
            bytes[skip + 1] = (byte) (valueInByte | toWrite);
        }

        if (bitSkip >= 6) {//byte 2
            byte valueInByte = bytes[skip + 2];
            byte toWrite = (byte) (value << 13 - bitSkip);
            bytes[skip + 2] = (byte) (valueInByte | toWrite);
        }
    }


    public enum MnemonicLanguage {
    	
        ENGLISH("english.txt");

        private final List<String> dictionary;
        private final Map<String, Integer> dictionaryMap;

        MnemonicLanguage(String fileName) {
            try {

            	List<String> tempList = new ArrayList<String>();
//            	InputStream is = this.getClass().getResourceAsStream("english.txt");
//            	InputStreamReader isr = new InputStreamReader(is);
//                BufferedReader br = new BufferedReader(isr);
//                String len = null;
//                while ((len=br.readLine()) != null){
//                	tempList.add(len);
//                	len = null;
//                }

                AssetManager am = AppConfig.getInstance().getAssets();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(am.open("english.txt")));
                    String next = "";
                    while (null != (next = br.readLine())) {
                        tempList.add(next);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                this.dictionary = tempList;
                Map<String, Integer> tempDictionaryMap = new HashMap<>();
                for (String word: dictionary) {
                	tempDictionaryMap.put(word, dictionary.indexOf(word));
                }
                this.dictionaryMap = unmodifiableMap(tempDictionaryMap);
            } catch (Exception e) {
                throw new IllegalStateException("Could'nt read file " + fileName, e);
            }
        }

        /*private ClassLoader getClassLoader() {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                return classLoader;
            }
            return MnemonicLanguage.class.getClassLoader();
        }*/

        public List<String> getDictionary() {
            return dictionary;
        }

        public Map<String, Integer> getDictionaryMap() {
            return dictionaryMap;
        }

        public String getWord(int index) {
            return dictionary.get(index);
        }

        public boolean wordExists(String word) {
            return dictionaryMap.containsKey(word);
        }

        public Integer getIndex(String word) {
            return dictionaryMap.get(word);
        }
    }
}
