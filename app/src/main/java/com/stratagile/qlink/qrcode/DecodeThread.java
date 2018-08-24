package com.stratagile.qlink.qrcode;

import android.os.Handler;
import android.os.Looper;

import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.vondear.rxtools.activity.ActivityScanerCode;

import java.util.concurrent.CountDownLatch;

/**
 *
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {

    private final CountDownLatch handlerInitLatch;
	ScanQrCodeActivity activity;
    private Handler handler;

	DecodeThread(ScanQrCodeActivity activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
