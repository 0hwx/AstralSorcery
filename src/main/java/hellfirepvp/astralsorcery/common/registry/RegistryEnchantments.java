package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.enchantment.EnchantmentNightVision;
import hellfirepvp.astralsorcery.common.enchantment.EnchantmentPlayerWornTick;
import hellfirepvp.astralsorcery.common.enchantment.EnchantmentScorchingHeat;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;

import java.util.LinkedList;
import java.util.List;

import static hellfirepvp.astralsorcery.common.lib.EnchantmentsAS.*;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: RegistryEnchantments
 * Created by HellFirePvP
 * Date: 15.03.2017 / 19:41
 */
public class RegistryEnchantments {

    public static List<EnchantmentPlayerWornTick> wearableTickEnchantments = new LinkedList<>();

    public static void init() {
        enchantmentNightVision = register(new EnchantmentNightVision());
        enchantmentScorchingHeat = register(new EnchantmentScorchingHeat());
    }

    private static <T extends Enchantment> T register(T e) {
        e.setName(AstralSorcery.MODID + ":" + e.getName());
        MinecraftForge.EVENT_BUS.register(e);
        if (e instanceof EnchantmentPlayerWornTick) {
            wearableTickEnchantments.add((EnchantmentPlayerWornTick) e);
        }
        return e;
    }

}
