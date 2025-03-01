/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block.network;

import hellfirepvp.astralsorcery.common.item.crystal.base.ItemTunedCrystalBase;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import hellfirepvp.astralsorcery.common.tile.base.TileReceiverBaseInventory;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockRitualPedestal
 * Created by HellFirePvP
 * Date: 28.09.2016 / 13:45
 */
public class BlockRitualPedestal extends BlockStarlightNetwork {

    private static final AxisAlignedBB box = AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 13D/16D, 1);

    public BlockRitualPedestal() {
        super("BlockRitualPedestal", Material.rock);
        setHardness(3.0F);
//        setSoundType(SoundType.STONE);
        setResistance(25.0F);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        ItemStack pedestal = new ItemStack(itemIn);
        list.add(pedestal);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileRitualPedestal();
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        RenderingUtils.playBlockBreakParticles(pos,
                BlocksAS.blockMarble.getDefaultState()
                        .withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.RAW));
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return true;
    }*/

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        BlockPos pos = new BlockPos(x, y, z);
        TileRitualPedestal pedestal = MiscUtils.getTileAt(worldIn, pos, TileRitualPedestal.class, true);
        if(pedestal == null) {
            return false;
        }
        IInventory handle = pedestal.getInventoryHandler();
        ItemStack in = handle.getStackInSlot(0);
        ItemStack heldItem = player.getHeldItem();
        if(heldItem != null) {
            Item i = heldItem.getItem();
            if(i != null && i instanceof ItemTunedCrystalBase) {
                if(in == null) {
                    handle.setInventorySlotContents(0, ItemUtils.copyStackWithSize(heldItem, 1));
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                }
            }
        }
        if(in != null && player.isSneaking()) {
            ItemUtils.dropItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, in);
            handle.setInventorySlotContents(0, null);
        }
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        BlockPos pos = new BlockPos(x, y, z);
        TileRitualPedestal te = MiscUtils.getTileAt(world, pos, TileRitualPedestal.class, true);
        if(te != null && !world.isRemote) {
            BlockPos toCheck = pos.up();
            Block other = world.getBlock(toCheck.getX(), toCheck.getY(), toCheck.getZ());
            if(other.isSideSolid(world, toCheck.getX(), toCheck.getY(), toCheck.getZ(), ForgeDirection.DOWN)) {
                TileReceiverBaseInventory.ItemHandlerTile handle = te.getInventoryHandler();
                ItemUtils.dropInventory(te.getInventoryHandler(), world, pos);
//                handle.clearInventory();
                te.markForUpdate();
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        BlockPos pos = new BlockPos(x, y, z);
        TileRitualPedestal te = MiscUtils.getTileAt(worldIn, pos, TileRitualPedestal.class, true);
        if(te != null && !worldIn.isRemote) {
            if(placer != null && placer instanceof EntityPlayer) {
                te.setOwner(placer.getUniqueID());
            }
        }
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return side == ForgeDirection.DOWN;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return box;
    }

    @Override
    public int getRenderType() {
        return 1;
    }

//    @Override
//    public boolean canRenderInLayer(BlockRenderLayer layer) {
//        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
//    }
}
