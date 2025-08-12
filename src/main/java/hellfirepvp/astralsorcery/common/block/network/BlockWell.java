/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block.network;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import hellfirepvp.astralsorcery.common.base.WellLiquefaction;
import hellfirepvp.astralsorcery.common.block.fluid.FluidLiquidStarlight;
import hellfirepvp.astralsorcery.common.registry.RegistryAchievements;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.TileWell;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockWell
 * Created by HellFirePvP
 * Date: 18.10.2016 / 12:43
 */
public class BlockWell extends BlockStarlightNetwork {

    private static final AxisAlignedBB boxWell = AxisAlignedBB
        .getBoundingBox(1D / 16D, 0D, 1D / 16D, 15D / 16D, 1, 15D / 16D);
    private static List<AxisAlignedBB> collisionBoxes;

    public BlockWell() {
        super("BlockWell", Material.rock);
        setHardness(3.0F);
        setStepSound(soundTypeStone);
        setResistance(25.0F);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
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
        return new TileWell();
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileWell tw = MiscUtils.getTileAt(world, pos, TileWell.class, true);
        if (tw != null) {
            if (tw.getHeldFluid() != null) {
                return tw.getHeldFluid()
                    .getLuminosity();
            }
        }
        return super.getLightValue(world, x, y, z);
    }

    // @Override
    // public boolean causesSuffocation() {
    // return false;
    // }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack heldItem = player.getHeldItem();
            BlockPos pos = new BlockPos(x, y, z);
            if (heldItem != null && heldItem.getItem() != null && player instanceof EntityPlayerMP) {
                TileWell tw = MiscUtils.getTileAt(worldIn, pos, TileWell.class, false);
                if (tw == null) return false;

                WellLiquefaction.LiquefactionEntry entry = WellLiquefaction.getLiquefactionEntry(heldItem);
                if (entry != null) {
                    IInventory handle = tw.getInventoryHandler();
                    if (handle.getStackInSlot(0) != null) return false;

                    if (!worldIn.isAirBlock(
                        pos.up()
                            .getX(),
                        pos.up()
                            .getY(),
                        pos.up()
                            .getZ())) {
                        return false;
                    }

                    handle.setInventorySlotContents(0, ItemUtils.copyStackWithSize(heldItem, 1));
                    // worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP,
                    // SoundCategory.PLAYERS, 0.2F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F +
                    // 1.0F) * 2.0F);

                    if (!MiscUtils.isPlayerFakeMP((EntityPlayerMP) player)
                        && entry.producing instanceof FluidLiquidStarlight) {
                        // Lets assume it starts collecting right away...
                        player.addStat(RegistryAchievements.achvLiqStarlight, 1);
                    }

                    if (!player.capabilities.isCreativeMode) {
                        heldItem.stackSize--;
                    }
                    if (heldItem.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                }

                // if(FluidUtil.tryFillContainerAndStow(heldItem,
                // tw.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN), new
                // InvWrapper(playerIn.inventory), 1000, playerIn)) {
                // SoundHelper.playSoundAround(SoundEvents.ITEM_BUCKET_FILL, worldIn, pos, 1F, 1F);
                // tw.markForUpdate();
                // }
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        BlockPos pos = new BlockPos(x, y, z);
        TileWell tw = MiscUtils.getTileAt(worldIn, pos, TileWell.class, true);
        if (tw != null && !worldIn.isRemote) {
            ItemStack stack = tw.getInventoryHandler()
                .getStackInSlot(0);
            if (stack != null) {
                tw.breakCatalyst();
            }
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return side != ForgeDirection.UP;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask,
        List<net.minecraft.util.AxisAlignedBB> list, Entity collider) {
        // for (AxisAlignedBB box : collisionBoxes) {
        // addCollisionBoxesToList(worldIn, x, y, z, box, list, collider);
        // }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return boxWell;
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    static {
        List<AxisAlignedBB> boxes = new LinkedList<>();

        boxes.add(AxisAlignedBB.getBoundingBox(1D / 16D, 0D, 1D / 16D, 15D / 16D, 5D / 16D, 15D / 16D));

        boxes.add(AxisAlignedBB.getBoundingBox(1D / 16D, 5D / 16D, 1D / 16D, 2D / 16D, 1D, 15D / 16D));
        boxes.add(AxisAlignedBB.getBoundingBox(1D / 16D, 5D / 16D, 1D / 16D, 15D / 16D, 1D, 2D / 16D));
        boxes.add(AxisAlignedBB.getBoundingBox(14D / 16D, 5D / 16D, 1D / 16D, 15D / 16D, 1D, 15D / 16D));
        boxes.add(AxisAlignedBB.getBoundingBox(1D / 16D, 5D / 16D, 14D / 16D, 15D / 16D, 1D, 15D / 16D));

        collisionBoxes = Collections.unmodifiableList(boxes);
    }

}
