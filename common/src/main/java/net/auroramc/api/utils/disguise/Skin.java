/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.utils.disguise;

public class Skin {

    public String value;
    public String signature;

    public Skin(String value, String signature) {
        this.signature = signature;
        this.value = value;
    }

    public String getSignature() {
        return signature;
    }

    public String getValue() {
        return value;
    }
}
