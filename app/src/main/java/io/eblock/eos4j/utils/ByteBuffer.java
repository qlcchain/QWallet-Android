package io.eblock.eos4j.utils;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:30:29
 */
public class ByteBuffer {

	private byte[] buffer = new byte[] {};

	public void concat(byte[] b) {

		// int[] a = IntStream.range(0, b.length).map(i -> b[i] & 0xff).toArray();
		// for(int i=1;i<=a.length;i++) {
		// System.out.print(a[i-1]+","+((i%8==0)?"\n":""));
		// }
		buffer = ByteUtils.concat(buffer, b);
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
}
