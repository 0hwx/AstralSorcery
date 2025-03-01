/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item.wand;

import hellfirepvp.astralsorcery.common.util.EnumDyeColor;
import hellfirepvp.astralsorcery.common.block.BlockTranslucentBlock;
import hellfirepvp.astralsorcery.common.data.config.Config;
import hellfirepvp.astralsorcery.common.item.ItemAlignmentChargeConsumer;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.TileTranslucent;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemIlluminationWand
 * Created by HellFirePvP
 * Date: 17.01.2017 / 15:09
 */
public class ItemIlluminationWand extends Item implements ItemAlignmentChargeConsumer {

    public ItemIlluminationWand() {
        setMaxDamage(0);
        setMaxStackSize(1);
        setUnlocalizedName("ItemIlluminationWand");
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        EnumDyeColor color = getConfiguredColor(stack);
        if(color != null) {
            tooltip.add(MiscUtils.ChatFormattingForDye(color) + MiscUtils.capitalizeFirst(I18n.format(color.getDyeColorName())));
            tooltip.add("");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldReveal(ChargeType ct, ItemStack stack) {
        return ct != ChargeType.PERM;
    }

    public static void setConfiguredColor(ItemStack stack, EnumDyeColor color) {
        NBTHelper.getPersistentData(stack).setInteger("color", color.getDyeDamage());
    }

    @Nullable
    public static EnumDyeColor getConfiguredColor(ItemStack stack) {
        NBTTagCompound tag = NBTHelper.getPersistentData(stack);
        if(tag != null && tag.hasKey("color")) {
            return EnumDyeColor.byDyeDamage(NBTHelper.getPersistentData(stack).getInteger("color"));
        }
        return null;
    }

    public static Block getPlacingState(ItemStack wand) {
        EnumDyeColor config = getConfiguredColor(wand);
        if(config != null) {
            return BlocksAS.blockVolatileLight;//.getDefaultState().withProperty(BlockFlareLight.COLOR, config);
        }
        return BlocksAS.blockVolatileLight;//.getDefaultState();
    }

    @Override
   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            BlockPos pos = new BlockPos(x, y, z);
            Block at = worldIn.getBlock(pos.getX(), pos.getY(), pos.getZ());
            if(!playerIn.isSneaking()) {
                Block block = worldIn.getBlock(pos.getX(), pos.getY(), pos.getZ());

                ForgeDirection facing = ForgeDirection.getOrientation(side);
                if (!block.isReplaceable(worldIn, pos.getX(), pos.getY(), pos.getZ())) {
                    pos = pos.offset(facing);
                }
                if(playerIn.canPlayerEdit(pos.getX(), pos.getY(), pos.getZ(), facing.ordinal(), stack) && worldIn.canPlaceEntityOnSide(BlocksAS.blockVolatileLight, pos.getX(), pos.getY(), pos.getZ(), true, facing.ordinal(), null, stack) &&
                        drainTempCharge(playerIn, Config.illuminationWandUseCost, true)) {
                    if (worldIn.setBlock(pos.getX(), pos.getY(), pos.getZ(), getPlacingState(stack))) {
//                        SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, playerIn);
//                        worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        drainTempCharge(playerIn, Config.illuminationWandUseCost, false);
                        gainPermCharge(playerIn, Config.illuminationWandUseCost / 4);
                    }
                }
            } else {
                if(at.isNormalCube()) {
                    TileEntity te = worldIn.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
                    if(te == null && !at.hasTileEntity(at.damageDropped(stack.getItemDamage())) && drainTempCharge(playerIn, Config.illuminationWandUseCost, true)) {
                        worldIn.setBlock(pos.getX(), pos.getY(), pos.getZ(), BlocksAS.translucentBlock);
                        TileTranslucent tt = MiscUtils.getTileAt(worldIn, pos, TileTranslucent.class, true);
                        if(tt == null) {
                            worldIn.setBlock(pos.getX(), pos.getY(), pos.getZ(), at);
                        } else {
                            tt.setFakedState(at);
                            drainTempCharge(playerIn, Config.illuminationWandUseCost, false);
                            gainPermCharge(playerIn, Config.illuminationWandUseCost);
                        }
                    }
                } else if(at instanceof BlockTranslucentBlock) {
                    TileTranslucent tt = MiscUtils.getTileAt(worldIn, pos, TileTranslucent.class, true);
                    if(tt != null && tt.getFakedState() != null) {
                        worldIn.setBlock(pos.getX(), pos.getY(), pos.getZ(), tt.getFakedState());
                    }
                }
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register)
    {
        this.itemIcon = register.registerIcon("astralsorcery:illuminationWand");
    }
}
