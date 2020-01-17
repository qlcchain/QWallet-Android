package qlc.utils;

import com.rfksystems.blake2b.Blake2b;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class WorkUtil {
	
    private static final Long THRESHOLD = 0xfffffe0000000000L;
    
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

	public static String generateWorkOneThread(byte[] hash) {
		final byte[] pow = new byte[8], zero = new byte[8];
		Arrays.fill(zero, (byte) 0x00);
		Arrays.fill(pow, (byte) 0x00);

        Blake2b blake2b = new Blake2b(null, 8, null, null);
		byte[] output = new byte[8];
		while (isEqual(pow, zero)) {
			byte[] bytes = new byte[8];
			RANDOM.nextBytes(bytes);
			for (byte b = -128; b < 127; b++) {
				bytes[7] = b;
				blake2b.reset();
		        blake2b.update(bytes, 0, bytes.length);
		        blake2b.update(hash, 0, hash.length);
		        blake2b.digest(output, 0);
				byte[] digest = Helper.reverse(output);
				if (overThreshold(digest)) {
					System.arraycopy(Helper.reverse(bytes), 0, pow, 0, 8);
					break;
				}
			}
		}
		
		return Helper.byteToHexString(pow);
	}

	public static String generateWork(byte[] hash) {
		final byte[] pow = new byte[8], zero = new byte[8];
		Arrays.fill(zero, (byte) 0x00);
		Arrays.fill(pow, (byte) 0x00);

		Thread[] threads = new Thread[4];
		for (int i=0; i<4; i++) {
			Thread powFinder = new Thread() {
				@Override
				public void run() {
			        Blake2b blake2b = new Blake2b(null, 8, null, null);
					byte[] output = new byte[8];
					while (isEqual(pow, zero)) {
						byte[] bytes = new byte[8];
						RANDOM.nextBytes(bytes);
						for (byte b = -128; b < 127; b++) {
							bytes[7] = b;
							blake2b.reset();
					        blake2b.update(bytes, 0, bytes.length);
					        blake2b.update(hash, 0, hash.length);
					        blake2b.digest(output, 0);
							byte[] digest = Helper.reverse(output);
							if (overThreshold(digest)) {
								System.arraycopy(Helper.reverse(bytes), 0, pow, 0, 8);
								break;
							}
						}
					}
				}
			};
			threads[i] = powFinder;
			powFinder.start();
		}
		
		while (isEqual(pow, zero)) {
			try {
				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return Helper.byteToHexString(pow);
	}
	
	private static boolean isEqual(byte[] b0, byte[] b1) {
		for (int i = 0; i < b0.length; i++) {
			if (b0[i] != b1[i])
				return false;
		}
		return true;
	}
	
	private static boolean overThreshold(byte[] bytes) {
	    long result = 0; //faster than ByteBuffer apparently:
	    for (int i = 0; i < 8; i++) {
	        result <<= 8;
	        result |= (bytes[i] & 0xFF);
	    }
	    
	    long MIN_VALUE = 0x8000000000000000L;
	    long x = result + MIN_VALUE, y = THRESHOLD + MIN_VALUE;
	    return ((x < y) ? -1 : ((x == y) ? 0 : 1)) > 0;
		//return Long.compareUnsigned(result, THRESHOLD) > 0; //wew java 8!
	}

}
