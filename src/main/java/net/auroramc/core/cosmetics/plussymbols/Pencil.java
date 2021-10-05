package net.auroramc.core.cosmetics.plussymbols;

import net.auroramc.core.api.cosmetics.PlusSymbol;
import org.bukkit.Material;

import java.util.Collections;

public class Pencil extends PlusSymbol {

    public Pencil() {
        super(216, "Pencil", "&3&l✎ Pencil", "There is currently no description for this cosmetic!", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", true, Material.BOOK_AND_QUILL, (short)0, '✎');
    }
}
