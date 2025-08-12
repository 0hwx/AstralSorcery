/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.common.entities.EntityIlluminationSpark;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemIlluminationPowder
 * Created by HellFirePvP
 * Date: 07.04.2017 / 23:04
 */
public class ItemIlluminationPowder extends Item {

    public ItemIlluminationPowder() {
        setUnlocalizedName("ItemIlluminationPowder");
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player) {
        if (stack == null || stack.getItem() == null || worldIn.isRemote) {
            return stack;
        }
        worldIn.spawnEntityInWorld(new EntityIlluminationSpark(worldIn, player));
        if (!player.capabilities.isCreativeMode) {
            stack.stackSize--;
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (stack == null || stack.getItem() == null || worldIn.isRemote) {
            return false;
        }
        BlockPos pos = new BlockPos(x, y, z);
        Block block = worldIn.getBlock(pos.getX(), pos.getY(), pos.getZ());
        if (!block.isReplaceable(worldIn, pos.getX(), pos.getY(), pos.getZ())) {
            pos = pos.offset(ForgeDirection.getOrientation(side));
        }
        if (playerIn.canPlayerEdit(pos.getX(), pos.getY(), pos.getZ(), side, stack) && worldIn.canPlaceEntityOnSide(
            BlocksAS.blockVolatileLight,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            true,
            side,
            playerIn,
            stack)) {
            if (worldIn.setBlock(pos.getX(), pos.getY(), pos.getZ(), BlocksAS.blockVolatileLight)) {
                // SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos),
                // worldIn, pos, playerIn);
                // worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
                // (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                if (!playerIn.capabilities.isCreativeMode) {
                    stack.stackSize--;
                }
                if (stack.stackSize <= 0) {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                }
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("astralsorcery:illumdust");
    }
}
