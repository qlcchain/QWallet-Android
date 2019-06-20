package com.stratagile.qlc.utils;

import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Provider;
import java.security.SecureRandomSpi;
import java.security.Security;

public class LinuxSecureRandom extends SecureRandomSpi {
    private static final FileInputStream urandom;

    private static class LinuxSecureRandomProvider extends Provider {
        public LinuxSecureRandomProvider() {
            super("LinuxSecureRandom", 1.0, "A Linux specific random number provider that uses /dev/urandom");
            put("SecureRandom.LinuxSecureRandom", LinuxSecureRandom.class.getName());
        }
    }

    static {
        try {
            File file = new File("/dev/urandom");
            // This stream is deliberately leaked.
            urandom = new FileInputStream(file);
            if (urandom.read() == -1)
                throw new RuntimeException("/dev/urandom not readable?");
            // Now override the default SecureRandom implementation with this one.
            int position = Security.insertProviderAt(new LinuxSecureRandomProvider(), 1);

            if (position != -1)
                Log.i("INFO", "Secure randomness will be read from {} only: " + file);
            else
                Log.i("INFO", "Randomness is already secure.");
        } catch (FileNotFoundException e) {
            // Should never happen.
            Log.e("ERROR", "/dev/urandom does not appear to exist or is not openable");
            throw new RuntimeException(e);
        } catch (IOException e) {
            Log.e("ERROR", "/dev/urandom does not appear to be readable");
            throw new RuntimeException(e);
        }
    }

    private final DataInputStream dis;

    public LinuxSecureRandom() {
        // DataInputStream is not thread safe, so each random object has its own.
        dis = new DataInputStream(urandom);
    }

    @Override
    protected void engineSetSeed(byte[] bytes) {
        // Ignore.
    }

    @Override
    protected void engineNextBytes(byte[] bytes) {
        try {
            dis.readFully(bytes); // This will block until all the bytes can be read.
        } catch (IOException e) {
            throw new RuntimeException(e); // Fatal error. Do not attempt to recover from this.
        }
    }

    @Override
    protected byte[] engineGenerateSeed(int i) {
        byte[] bits = new byte[i];
        engineNextBytes(bits);
        return bits;
    }
}