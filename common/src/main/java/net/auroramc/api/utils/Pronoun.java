/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.utils;

public enum Pronoun {

    NONE("None", "None"),
    SHE_HER("She/Her","She/Her/Hers"),
    HE_HIM("He/Him","He/Him/His"),
    THEY_THEM("They/Them","They/Them/Theirs"),
    SHE_THEY("She/They","She/Her/Hers/They/Them/Theirs"),
    HE_THEY("He/They","He/Him/His/They/Them/Theirs"),
    EY_EM("E(y)/Em","E(y)/Em/Eir"),
    SIE_HIR("Sie/Hir","Sir/Hir/Hirs"),
    PER_PERS("Per/Pers","Per/Pers"),
    TEY_TER("Tey/Ter","Tey/Ter/Tem/Ters"),
    VE_VER("Ve/Ver","Ve/Ver/Vis/Vers"),
    XE_XEM("Xe/Xem","Xe/Xem/Xyr/Xyrs"),
    ZE_HIR("Ze/Hir","Ze/Hir/Hirs"),
    ZIR_ZIM("Zir/Zim","Zie/Zim/Zir/Zis"),
    ANY_PRONOUNS("Any", "Any Pronouns");


    private final String display;
    private final String full;

    Pronoun(String display, String full) {
        this.display = display;
        this.full = full;
    }

    public String getDisplay() {
        return display;
    }

    public String getFull() {
        return full;
    }
}
