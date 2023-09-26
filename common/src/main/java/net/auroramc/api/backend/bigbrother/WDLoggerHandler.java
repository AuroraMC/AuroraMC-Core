/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
