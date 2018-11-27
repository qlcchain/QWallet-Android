package com.stratagile.qlink.utils.eth;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.SpUtil;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import static org.web3j.crypto.Keys.ADDRESS_LENGTH_IN_HEX;

/**
 * 以太坊钱包创建工具类
 * Created by dwq on 2018/3/15/015.
 * e-mail:lomapa@163.com
 */

public class ETHWalletUtils {

    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    /**
     * 随机
     */
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();
    private Credentials credentials;
    /**
     * 通用的以太坊基于bip44协议的助记词路径 （imtoken jaxx Metamask myetherwallet）
     */
    public static String ETH_JAXX_TYPE = "m/44'/60'/0'/0/0";
    public static String ETH_LEDGER_TYPE = "m/44'/60'/0'/0";
    public static String ETH_CUSTOM_TYPE = "m/44'/60'/1'/0/0";

    /**
     * 创建助记词，并通过助记词创建钱包
     *
     * @return
     */
    public static EthWallet generateMnemonic() {
        String[] pathArray = ETH_JAXX_TYPE.split("/");
        String passphrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;

        DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase, creationTimeSeconds);
        return generateWalletByMnemonic(ds, pathArray);
    }

//    /**
//     * 创建助记词，并通过助记词创建钱包
//     *
//     * @param pwd
//     * @return
//     */
//    public static EthWallet generateMnemonic(String pwd) {
//        String[] pathArray = ETH_JAXX_TYPE.split("/");
//        String passphrase = "";
//        long creationTimeSeconds = System.currentTimeMillis() / 1000;
//
//        DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase, creationTimeSeconds);
//        return generateWalletByMnemonic(ds, pathArray, pwd);
//    }

    /**
     * 通过导入助记词，导入钱包
     *
     * @param path 路径
     * @param list 助记词
     * @return
     */
    public static EthWallet importMnemonic(String path, List<String> list) {
        if (!path.startsWith("m") && !path.startsWith("M")) {
            //参数非法
            return null;
        }
        String[] pathArray = path.split("/");
        if (pathArray.length <= 1) {
            //内容不对
            return null;
        }
        String passphrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;
        DeterministicSeed ds = new DeterministicSeed(list, null, passphrase, creationTimeSeconds);
        return generateWalletByMnemonic(ds, pathArray);
    }

    @NonNull
    public static String generateNewWalletName() {
        List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        int size = ethWallets.size();
        if (size < 9) {
            return "ETH-Wallet 0" + (ethWallets.size() + 1);
        } else {
            return "ETH-Wallet " + (ethWallets.size() + 1);
        }
    }

    /**
     * 以助记词检查钱包是否存在
     *
     * @param mnemonic
     * @return
     */
    public static boolean checkRepeatByMenmonic(String mnemonic) {
        List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        for (EthWallet ethWallet : ethWallets
                ) {
            if (TextUtils.equals(ethWallet.getMnemonic().trim(), mnemonic.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查钱包名称是否存在
     *
     * @param name
     * @return
     */
    public static boolean walletNameChecking(String name) {
        List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        for (EthWallet ethWallet : ethWallets
                ) {
            if (TextUtils.equals(ethWallet.getName(), name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param ds        助记词加密种子
     * @param pathArray 助记词标准
     * @return
     */
    @Nullable
    public static EthWallet generateWalletByMnemonic(DeterministicSeed ds, String[] pathArray) {
        //种子
        byte[] seedBytes = ds.getSeedBytes();
//        System.out.println(Arrays.toString(seedBytes));
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();
//        System.out.println(Arrays.toString(mnemonic.toArray()));
        if (seedBytes == null) {
            return null;
        }
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0,
                        pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }
        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
        EthWallet ethWallet = generateWallet(getPassword(), keyPair);
        if (ethWallet != null) {
            ethWallet.setMnemonic(convertMnemonicList(mnemonic));
        }
        return ethWallet;
    }

    /**
     * @param ds        助记词加密种子
     * @param pathArray 助记词标准
     * @param pwd       密码
     * @return
     */
    @Nullable
    public static EthWallet generateWalletByMnemonic(DeterministicSeed ds, String[] pathArray, String pwd) {
        //种子
        byte[] seedBytes = ds.getSeedBytes();
//        System.out.println(Arrays.toString(seedBytes));
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();
//        System.out.println(Arrays.toString(mnemonic.toArray()));
        if (seedBytes == null) {
            return null;
        }
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0,
                        pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }
        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
        EthWallet ethWallet = generateWallet(pwd, keyPair);
        if (ethWallet != null) {
            ethWallet.setMnemonic(convertMnemonicList(mnemonic));
        }
        return ethWallet;
    }

    private static String convertMnemonicList(List<String> mnemonics) {
        StringBuilder sb = new StringBuilder();
        for (String mnemonic : mnemonics
                ) {
            sb.append(mnemonic);
            sb.append(" ");
        }
        return sb.toString();
    }


    @Nullable
    private static EthWallet generateWallet(String pwd, ECKeyPair ecKeyPair) {
        WalletFile walletFile;
        try {
            walletFile = Wallet.create(pwd, ecKeyPair, 1024, 1); // WalletUtils. .generateNewWalletFile();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        BigInteger publicKey = ecKeyPair.getPublicKey();
        String s = publicKey.toString();
        KLog.i("ETHWalletUtils", "publicKey = " + s);
        File destination = new File(Environment.getExternalStorageDirectory() + "/Qlink/KeyStore", "keystore_" + walletFile.getAddress() + ".json");

        //目录不存在则创建目录，创建不了则报错
        if (!createParentDir(destination)) {
            return null;
        }
        try {
            objectMapper.writeValue(destination, walletFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        EthWallet ethWallet = new EthWallet();
        ethWallet.setName(generateNewWalletName());
        ethWallet.setAddress(Keys.toChecksumAddress(walletFile.getAddress()));
        ethWallet.setKeystorePath(destination.getAbsolutePath());
        ethWallet.setCurrent(true);
        ethWallet.setIsLook(false);
        ethWallet.setPassword(enCodePassword(pwd));
        return ethWallet;
    }

    /**
     * 通过keystore.json文件导入钱包
     *
     * @param keystore 原json文件
     * @param pwd      json文件密码
     * @return
     */
    public static EthWallet loadWalletByKeystore(String keystore, String pwd) {
        Credentials credentials = null;
        try {
            WalletFile walletFile = null;
            walletFile = objectMapper.readValue(keystore, WalletFile.class);

//            WalletFile walletFile = new Gson().fromJson(keystore, WalletFile.class);
            credentials = Credentials.create(Wallet.decrypt(pwd, walletFile));
        } catch (IOException e) {
            e.printStackTrace();
            KLog.e("ETHWalletUtils", e.toString());
        } catch (CipherException e) {
            KLog.e("ETHWalletUtils", e.toString());
//            ToastUtils.showToast(R.string.load_wallet_by_official_wallet_keystore_input_tip);
            e.printStackTrace();
        }
        if (credentials != null) {
            return generateWallet(pwd, credentials.getEcKeyPair());
        }
        return null;
    }

    /**
     * 通过明文私钥导入钱包
     *
     * @param privateKey
     * @return
     */
    public static EthWallet loadWalletByPrivateKey(String privateKey) {
        Credentials credentials = null;
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        return generateWallet(getPassword(), ecKeyPair);
    }

    /**
     * 创建钱包的时候，需要每次拿最新的密码
     * @return
     */
    public static String getPassword() {
        KLog.i(SpUtil.getString(AppConfig.getInstance(), ConstantValue.walletPassWord, ""));
        String password = new String(qlinkcom.AES(Base64.decode(SpUtil.getString(AppConfig.getInstance(), ConstantValue.walletPassWord, ""), Base64.NO_WRAP), 1));
        KLog.i(password);
        return password;
    }

    /**
     * 使用钱包的时候，要用钱包里边保存的密码
     * @param content
     * @return
     */
    public static String getPassword(String content) {
//        KLog.i(content);
        String password = "";
        try {
            password = new String(qlinkcom.AES(Base64.decode(content, Base64.NO_WRAP), 1));
        } catch (Exception e) {
            e.printStackTrace();
            password = content;
        }
//        KLog.i(password);
        return password;
    }

    public static String enCodePassword(String password) {
        String encode = Base64.encodeToString(qlinkcom.AES(password.getBytes(),0),Base64.NO_WRAP);
        return encode;
    }


    private static boolean createParentDir(File file) {
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            System.out.println("目标文件所在目录不存在，准备创建");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在目录失败！");
                return false;
            }
        }
        return true;
    }

    /**
     * 修改钱包密码
     *
     * @param walletId
     * @param walletName
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public static EthWallet modifyPassword(long walletId, String walletName, String oldPassword, String newPassword) {
        EthWallet ethWallet = AppConfig.getInstance().getDaoSession().getEthWalletDao().load(walletId);
        Credentials credentials = null;
        ECKeyPair keypair = null;
        try {
            credentials = WalletUtils.loadCredentials(oldPassword, ethWallet.getKeystorePath());
            keypair = credentials.getEcKeyPair();
            File destinationDirectory = new File(Environment.getExternalStorageDirectory() + "/Qlink/KeyStore", "keystore_" + walletName + ".json");
            WalletUtils.generateWalletFile(newPassword, keypair, destinationDirectory, true);
            ethWallet.setPassword(newPassword);
            AppConfig.getInstance().getDaoSession().getEthWalletDao().update(ethWallet);
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ethWallet;
    }

    /**
     * 导出明文私钥
     *
     * @param walletId 钱包Id
     * @param pwd      钱包密码
     * @return
     */
    public static String derivePrivateKey(long walletId) {
        EthWallet ethWallet = AppConfig.getInstance().getDaoSession().getEthWalletDao().load(walletId);
        String pwd = getPassword(ethWallet.getPassword());
        Credentials credentials;
        ECKeyPair keypair;
        String privateKey = null;
        try {
            credentials = WalletUtils.loadCredentials(pwd, ethWallet.getKeystorePath());
            keypair = credentials.getEcKeyPair();
            privateKey = Numeric.toHexStringNoPrefixZeroPadded(keypair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 导出keystore文件
     *
     * @param walletId
     * @param pwd
     * @return
     */
    public static String deriveKeystore(long walletId) {
        EthWallet ethWallet = AppConfig.getInstance().getDaoSession().getEthWalletDao().load(walletId);
        String keystore = null;
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(new File(ethWallet.getKeystorePath()), WalletFile.class);
            keystore = objectMapper.writeValueAsString(walletFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keystore;
    }

    /**
     * 删除钱包
     *
     * @param walletId
     * @return
     */
    public static boolean deleteWallet(long walletId) {
        EthWallet ethWallet = AppConfig.getInstance().getDaoSession().getEthWalletDao().load(walletId);
        if (deleteFile(ethWallet.getKeystorePath())) {
            AppConfig.getInstance().getDaoSession().getEthWalletDao().deleteByKey(walletId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
//                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
//                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
//            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    public static boolean isETHValidAddress(String input) {
        if (input == null || "".equals(input) || !input.startsWith("0x")) {
            return false;
        }
        return isValidAddress(input);
    }

    private static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == ADDRESS_LENGTH_IN_HEX;
    }
}
