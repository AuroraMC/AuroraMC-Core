/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.backend.bigbrother;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class WDLoggerHandler extends Handler {


    @Override
    public void publish(LogRecord record) {
        if (record.getThrown() != null) {
            Watchdog.logException(record.getThrown());
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
