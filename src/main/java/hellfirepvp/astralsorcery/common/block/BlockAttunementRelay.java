/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.block.network.BlockAltar;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.TileAttunementRelay;
import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.struct.BlockArray;
import hellfirepvp.astralsorcery.common.util.struct.BlockDiscoverer;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockAttunementRelay
 * Created by HellFirePvP
 * Date: 30.11.2016 / 13:16
 */
public class BlockAttunementRelay extends BlockContainer {

    private static final AxisAlignedBB box = AxisAlignedBB
        .getBoundingBox(3F / 16F, 0, 3F / 16F, 13F / 16F, 3F / 16F, 13F / 16F);

    public BlockAttunementRelay() {
        super(Material.glass);
        setBlockName("BlockAttunementRelay");
        setHardness(0.5F);
        setHarvestLevel("pickaxe", 0);
        setResistance(1.0F);
        setLightLevel(0.8F);
        // setSoundType(SoundType.GLASS);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAttunementRelay();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileAttunementRelay();
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        if (!worldIn.isRemote) {
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity inv = MiscUtils.getTileAt(worldIn, pos, TileEntity.class, true);
            // if(inv != null) {
            // for (EnumFacing face : EnumFacing.VALUES) {
            // IInventory handle = inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
            // if(handle != null) {
            // ItemUtils.dropInventory(handle, worldIn, pos);
            // break;
            // }
            // }
            // }

            BlockAltar.startSearchForRelayUpdate(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    @Override
    public void onBlockAdded(World worldIn, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        startSearchRelayLinkThreadAt(worldIn, pos, true);
    }

    public static void startSearchRelayLinkThreadAt(World world, BlockPos pos, boolean recUpdate) {
        Thread searchThread = new Thread(() -> {
            BlockPos closestAltar = null;
            double dstSqOtherRelay = Double.MAX_VALUE;
            BlockArray relaysAndAltars = BlockDiscoverer.searchForBlocksAround(
                world,
                pos,
                16,
                (world1, x, y, z, block) -> block.equals(BlocksAS.blockAltar)
                    || block.equals(BlocksAS.attunementRelay));
            relaysAndAltars.getPattern()
                .remove(pos);
            for (Map.Entry<BlockPos, BlockArray.BlockInformation> entry : relaysAndAltars.getPattern()
                .entrySet()) {
                if (entry.getValue().type.equals(BlocksAS.blockAltar)) {
                    if (closestAltar == null || pos.distanceSq(entry.getKey()) < pos.distanceSq(closestAltar)) {
                        closestAltar = entry.getKey();
                    }
                } else {
                    double dstSqOther = entry.getKey()
                        .distanceSq(pos);
                    if (dstSqOther < dstSqOtherRelay) {
                        dstSqOtherRelay = dstSqOther;
                    }
                }
            }

            BlockPos finalClosestAltar = closestAltar;
            double finalDstSqOtherRelay = dstSqOtherRelay;
            AstralSorcery.proxy.scheduleDelayed(() -> {
                TileAttunementRelay tar = MiscUtils.getTileAt(world, pos, TileAttunementRelay.class, true);
                if (tar != null) {
                    tar.updatePositionData(finalClosestAltar, finalDstSqOtherRelay);
                }
                if (recUpdate) {
                    BlockAltar.startSearchForRelayUpdate(world, pos.getX(), pos.getY(), pos.getZ());
                }
            });
        });
        searchThread.setName("AttRelay PositionFinder at " + pos.toString());
        searchThread.start();
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        if (!worldIn.isRemote) {
            ItemStack held = player.getHeldItem();
            BlockPos pos = new BlockPos(x, y, z);
            if (held != null) {
                TileAttunementRelay tar = MiscUtils.getTileAt(worldIn, pos, TileAttunementRelay.class, true);
                if (tar != null) {
                    TileInventoryBase.ItemHandlerTile mod = tar.getInventoryHandler();
                    if (mod.getStackInSlot(0) != null) {
                        ItemStack stack = mod.getStackInSlot(0);
                        ItemUtils
                            .dropItem(worldIn, player.posX, player.posY, player.posZ, stack).delayBeforeCanPickup = 0;
                        mod.setInventorySlotContents(0, null);
                        tar.markForUpdate();
                    }

                    if (!worldIn.isAirBlock(
                        pos.up()
                            .getX(),
                        pos.up()
                            .getY(),
                        pos.up()
                            .getZ())) {
                        return false;
                    }

                    mod.setInventorySlotContents(0, ItemUtils.copyStackWithSize(held, 1));
                    // worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP,
                    // SoundCategory.PLAYERS, 0.2F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F +
                    // 1.0F) * 2.0F);
                    if (!player.capabilities.isCreativeMode) {
                        held.stackSize--;
                    }
                    if (held.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                    tar.markForUpdate();
                }
            } else {
                TileAttunementRelay tar = MiscUtils.getTileAt(worldIn, pos, TileAttunementRelay.class, true);
                if (tar != null) {
                    TileInventoryBase.ItemHandlerTile mod = tar.getInventoryHandler();
                    if (mod.getStackInSlot(0) != null) {
                        ItemStack stack = mod.getStackInSlot(0);
                        ItemUtils
                            .dropItem(worldIn, player.posX, player.posY, player.posZ, stack).delayBeforeCanPickup = 0;
                        mod.setInventorySlotContents(0, null);
                        tar.markForUpdate();
                    }
                }
            }
        }
        return true;
    }

    // @Override
    // public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
    // return box;
    // }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    // @Override
    // public boolean canRenderInLayer(BlockRenderLayer layer) {
    // return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
    // }

    @Override
    public int getRenderType() {
        return 1;
    }

}
