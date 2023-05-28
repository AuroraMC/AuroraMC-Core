/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.abstraction;

import net.auroramc.api.AuroraMCAPI;

public class ScheduleFactory {


    public static void scheduleAsync(Runnable runnable) {
        AuroraMCAPI.getAbstractedMethods().scheduleAsyncTask(runnable);
    }

    public static Object scheduleAsyncLater(Runnable runnable, long delay) {
        return AuroraMCAPI.getAbstractedMethods().scheduleAsyncTaskLater(runnable, delay);
    }

    public static void scheduleSync(Runnable runnable) {
        AuroraMCAPI.getAbstractedMethods().scheduleSyncTask(runnable);
    }

}
