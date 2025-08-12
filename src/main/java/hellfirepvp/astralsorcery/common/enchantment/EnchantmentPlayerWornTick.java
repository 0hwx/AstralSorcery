package hellfirepvp.astralsorcery.common.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EnchantmentPlayerWornTick
 * Created by HellFirePvP
 * Date: 15.03.2017 / 18:01
 */
public abstract class EnchantmentPlayerWornTick extends EnchantmentBase {

    public EnchantmentPlayerWornTick(String name, int Id, int rarityIn, EnumEnchantmentType typeIn) {
        super(name, Id, rarityIn, typeIn);
    }

    public void onWornTick(boolean isClient, EntityPlayer base, int level) {}

}
