/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.backend.bigbrother;

import java.io.StreamCorruptedException;
import java.net.SocketException;

public class WDUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof StreamCorruptedException || e instanceof SocketException) {
            return;
        }
        Watchdog.logException(e);
    }
}
