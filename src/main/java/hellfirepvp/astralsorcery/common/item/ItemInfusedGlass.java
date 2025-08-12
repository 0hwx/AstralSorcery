package hellfirepvp.astralsorcery.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.starmap.ActiveStarMap;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemInfusedGlass
 * Created by HellFirePvP
 * Date: 03.05.2017 / 14:11
 */
public class ItemInfusedGlass extends Item {

    public ItemInfusedGlass() {
        setMaxStackSize(1);
        setMaxDamage(3);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
        setUnlocalizedName("ItemInfusedGlass");
    }

    // isEnchantable in 1.11.2 - that's also a better name for this method...
    @Override
    public boolean isItemTool(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 15;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (damage < getDamage(stack)) return;
        int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
        if (lvl > 0) {
            for (int i = 0; i < lvl; i++) {
                if (itemRand.nextFloat() > 0.7) {
                    return;
                }
            }
        }
        super.setDamage(stack, damage);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return getMapEngravingInformations(stack) != null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        ActiveStarMap map = getMapEngravingInformations(stack);
        if (map != null) {
            if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
                for (IConstellation c : map.getConstellations()) {
                    String out = ChatFormatting.GRAY + "- " + ChatFormatting.BLUE + I18n.format(c.getUnlocalizedName());
                    if (playerIn.capabilities.isCreativeMode) {
                        out += ChatFormatting.LIGHT_PURPLE + " (Creative) " + (int) (map.getPercentage(c) * 100) + "%";
                    }
                    tooltip.add(out);
                }
            } else {
                tooltip.add(
                    ChatFormatting.DARK_GRAY + ChatFormatting.ITALIC.toString() + I18n.format("misc.moreInformation"));
            }
            tooltip.add("");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        ActiveStarMap map = getMapEngravingInformations(stack);
        if (map != null) {
            return super.getUnlocalizedName(stack) + ".active";
        }
        return super.getUnlocalizedName(stack);
    }

    public static void setMapEngravingInformations(ItemStack infusedGlassStack, ActiveStarMap map) {
        NBTHelper.getPersistentData(infusedGlassStack)
            .setTag("map", map.serialize());
    }

    @Nullable
    public static ActiveStarMap getMapEngravingInformations(ItemStack infusedGlassStack) {
        NBTTagCompound tag = NBTHelper.getPersistentData(infusedGlassStack);
        if (!tag.hasKey("map")) return null;
        return ActiveStarMap.deserialize(tag.getCompoundTag("map"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("astralsorcery:glass_infused");
    }

}
