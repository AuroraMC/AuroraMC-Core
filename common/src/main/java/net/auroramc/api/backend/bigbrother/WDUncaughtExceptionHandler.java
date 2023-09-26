/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
