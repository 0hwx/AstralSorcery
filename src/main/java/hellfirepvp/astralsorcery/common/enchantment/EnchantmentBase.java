package hellfirepvp.astralsorcery.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EnchantmentBase
 * Created by HellFirePvP
 * Date: 06.05.2017 / 10:46
 */
public class EnchantmentBase extends Enchantment {

    protected EnchantmentBase(String unlocName, int Id, int rarityIn, EnumEnchantmentType typeIn) {
        super(Id, rarityIn, typeIn);
        setName(unlocName);
    }

}
