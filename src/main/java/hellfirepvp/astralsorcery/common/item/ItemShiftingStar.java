/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item;

import java.awt.Color;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemShiftingStar
 * Created by HellFirePvP
 * Date: 09.02.2017 / 23:05
 */
public class ItemShiftingStar extends Item {

    public ItemShiftingStar() {
        setMaxStackSize(1);
        setMaxDamage(0);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
        setUnlocalizedName("ItemShiftingStar");
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn) {
        playerIn.setItemInUse(stack, getMaxItemUseDuration(stack));
        return stack;
    }

    @Nullable
    @Override
    public ItemStack onEaten(ItemStack stack, World worldIn, EntityPlayer entityLiving) {
        if (!worldIn.isRemote && entityLiving instanceof EntityPlayer) {
            EntityPlayer pl = (EntityPlayer) entityLiving;
            if (ResearchManager.setAttunedConstellation(pl, null)) {
                pl.addChatMessage(
                    new ChatComponentTranslation("progress.remove.attunement")
                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
                // SoundHelper.playSoundAround(SoundEvents.BLOCK_GLASS_BREAK, worldIn, entityLiving.getPosition(), 1F,
                // 1F);
            }
        }
        return null;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (player.getEntityWorld().isRemote) {
            playEffects();
        }
    }

    @SideOnly(Side.CLIENT)
    private void playEffects() {
        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
        for (int i = 0; i < 3; i++) {
            EntityFXFacingParticle particle = EffectHelper
                .genericFlareParticle(p.posX, p.posY + p.getEyeHeight() / 2, p.posZ);
            particle.motion(-0.1 + itemRand.nextFloat() * 0.2, 0.01, -0.1 + itemRand.nextFloat() * 0.2);
            if (itemRand.nextInt(3) == 0) particle.setColor(Color.WHITE);
            particle.scale(0.3F);
            if (itemRand.nextInt(4) == 0) particle.scale(0.2F);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 60;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("astralsorcery:shifting_star");
    }

}
