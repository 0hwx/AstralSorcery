package hellfirepvp.astralsorcery.common.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EnchantmentNightVision
 * Created by HellFirePvP
 * Date: 15.03.2017 / 17:45
 */
public class EnchantmentNightVision extends EnchantmentPlayerWornTick {

    public EnchantmentNightVision() {
        super("as.nightvision", 100,1, EnumEnchantmentType.armor_head);
    }

    @Override
    public void onWornTick(boolean isClient, EntityPlayer base, int level) {
        base.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 300, level - 1, true));
    }

    @Override
    public void func_151368_a(EntityLivingBase user, Entity target, int level) {
        if(target instanceof EntityLivingBase) {
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 300, level - 1, true));
        }
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return type.canEnchantItem(stack.getItem());
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

}
