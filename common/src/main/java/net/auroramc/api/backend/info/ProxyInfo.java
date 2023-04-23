/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.backend.info;

import java.util.UUID;

public class ProxyInfo extends Info {

    private final int buildNumber;

    public ProxyInfo(UUID uuid, String ip, int port, Network network, boolean forced, int protocolPort, int buildNumber, String authKey) {
        super(uuid.toString(), ip, port, network, forced, protocolPort, authKey);
        this.buildNumber = buildNumber;
    }

    public int getBuildNumber() {
        return buildNumber;
    }
}
