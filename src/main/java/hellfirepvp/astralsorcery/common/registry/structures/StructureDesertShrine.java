/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.registry.structures;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystalBase;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.item.crystal.CrystalProperties;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.tile.network.TileCollectorCrystal;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.struct.StructureBlockArray;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: StructureDesertShrine
 * Created by HellFirePvP
 * Date: 21.10.2016 / 12:17
 */
public class StructureDesertShrine extends StructureBlockArray {

    private static Random rand = new Random();

    public StructureDesertShrine() {
        load();
    }

    @Override
    public Map<BlockPos, BlockInformation> placeInWorld(World world, BlockPos center) {
        center = center.down();
        Map<BlockPos, BlockInformation> result = super.placeInWorld(world, center);
        placeSand(world, center);
        return result;
    }

    @Override
    public Map<BlockPos, BlockInformation> placeInWorld(World world, BlockPos center, PastPlaceProcessor processor) {
        center = center.down();
        Map<BlockPos, BlockInformation> result = super.placeInWorld(world, center, processor);
        placeSand(world, center);
        return result;
    }

    private void placeSand(World world, BlockPos center) {
        Block marble = BlocksAS.blockMarble;
        Block mch = marble;// .getDefaultState().withProperty(MARBLE_TYPE, BlockMarble.MarbleBlockType.CHISELED);
        Block mpl = marble;// .getDefaultState().withProperty(MARBLE_TYPE, BlockMarble.MarbleBlockType.PILLAR);

        placeBlockCube(world, center, -4, 1, -4, 4, 1, 4);

        BlockPos at = center.add(0, 1, 0);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mpl);
        at = center.add(0, 2, 0);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mch);
        at = center.add(3, 1, 3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mpl);
        at = center.add(3, 2, 3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mpl);
        at = center.add(3, 3, 3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mch);

        at = center.add(-3, 1, 3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mpl);
        at = center.add(-3, 2, 3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mpl);
        at = center.add(-3, 3, 3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mch);

        at = center.add(-3, 1, -3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mpl);
        at = center.add(-3, 2, -3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mpl);
        at = center.add(-3, 3, -3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mch);

        at = center.add(3, 1, -3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mpl);
        at = center.add(3, 2, -3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mpl);
        at = center.add(3, 3, -3);
        world.setBlock(at.getX(), at.getY(), at.getZ(), mch);

        topBlockWithOffset(3, 4, 3, world, center, 1F);
        topBlockWithOffset(-3, 4, 3, world, center, 1F);
        topBlockWithOffset(3, 4, -3, world, center, 1F);
        topBlockWithOffset(-3, 4, -3, world, center, 1F);
        topBlockWithOffset(0, 3, 0, world, center, 1F);

        topBlockWithOffset(1, 2, 0, world, center, 1F);
        topBlockWithOffset(-1, 2, 0, world, center, 1F);
        topBlockWithOffset(0, 2, 1, world, center, 1F);
        topBlockWithOffset(0, 2, -1, world, center, 1F);

        topBlockWithOffset(-3, 2, -4, world, center, 1F);
        topBlockWithOffset(-4, 2, -3, world, center, 1F);
        topBlockWithOffset(-3, 2, -2, world, center, 1F);
        topBlockWithOffset(-2, 2, -3, world, center, 1F);
        topBlockWithOffset(-3, 2, 4, world, center, 1F);
        topBlockWithOffset(-4, 2, 3, world, center, 1F);
        topBlockWithOffset(-3, 2, 2, world, center, 1F);
        topBlockWithOffset(-2, 2, 3, world, center, 1F);
        topBlockWithOffset(3, 2, -4, world, center, 1F);
        topBlockWithOffset(4, 2, -3, world, center, 1F);
        topBlockWithOffset(3, 2, -2, world, center, 1F);
        topBlockWithOffset(2, 2, -3, world, center, 1F);
        topBlockWithOffset(3, 2, 4, world, center, 1F);
        topBlockWithOffset(4, 2, 3, world, center, 1F);
        topBlockWithOffset(3, 2, 2, world, center, 1F);
        topBlockWithOffset(2, 2, 3, world, center, 1F);

        topBlockWithOffset(3, 3, 4, world, center, 0.5);
        topBlockWithOffset(4, 3, 3, world, center, 0.5);
        topBlockWithOffset(3, 3, 2, world, center, 0.5);
        topBlockWithOffset(2, 3, 3, world, center, 0.5);
        topBlockWithOffset(-3, 3, 4, world, center, 0.5);
        topBlockWithOffset(-4, 3, 3, world, center, 0.5);
        topBlockWithOffset(-3, 3, 2, world, center, 0.5);
        topBlockWithOffset(-2, 3, 3, world, center, 0.5);
        topBlockWithOffset(3, 3, -4, world, center, 0.5);
        topBlockWithOffset(4, 3, -3, world, center, 0.5);
        topBlockWithOffset(3, 3, -2, world, center, 0.5);
        topBlockWithOffset(2, 3, -3, world, center, 0.5);
        topBlockWithOffset(-3, 3, -4, world, center, 0.5);
        topBlockWithOffset(-4, 3, -3, world, center, 0.5);
        topBlockWithOffset(-3, 3, -2, world, center, 0.5);
        topBlockWithOffset(-2, 3, -3, world, center, 0.5);

        topBlockWithOffset(1, 2, 1, world, center, 0.5);
        topBlockWithOffset(-1, 2, 1, world, center, 0.5);
        topBlockWithOffset(1, 2, -1, world, center, 0.5);
        topBlockWithOffset(-1, 2, -1, world, center, 0.5);

        topBlockWithOffset(0, 3, 1, world, center, 0.4);
        topBlockWithOffset(1, 3, 0, world, center, 0.4);
        topBlockWithOffset(0, 3, -1, world, center, 0.4);
        topBlockWithOffset(-1, 3, 0, world, center, 0.4);
        topBlockWithOffset(0, 2, 2, world, center, 0.4);
        topBlockWithOffset(2, 2, 0, world, center, 0.4);
        topBlockWithOffset(0, 2, -2, world, center, 0.4);
        topBlockWithOffset(-2, 2, 0, world, center, 0.4);

        topBlockWithOffset(2, 2, 2, world, center, 0.3);
        topBlockWithOffset(4, 2, 2, world, center, 0.3);
        topBlockWithOffset(2, 2, 4, world, center, 0.3);
        topBlockWithOffset(4, 2, 4, world, center, 0.3);
        topBlockWithOffset(2, 2, -2, world, center, 0.3);
        topBlockWithOffset(4, 2, -2, world, center, 0.3);
        topBlockWithOffset(2, 2, -4, world, center, 0.3);
        topBlockWithOffset(4, 2, -4, world, center, 0.3);
        topBlockWithOffset(-2, 2, 2, world, center, 0.3);
        topBlockWithOffset(-4, 2, 2, world, center, 0.3);
        topBlockWithOffset(-2, 2, 4, world, center, 0.3);
        topBlockWithOffset(-4, 2, 4, world, center, 0.3);
        topBlockWithOffset(-2, 2, -2, world, center, 0.3);
        topBlockWithOffset(-4, 2, -2, world, center, 0.3);
        topBlockWithOffset(-2, 2, -4, world, center, 0.3);
        topBlockWithOffset(-4, 2, -4, world, center, 0.3);
    }

    private void topBlockWithOffset(int x, int y, int z, World world, BlockPos center, double chance) {
        if (rand.nextFloat() <= chance) {
            // BlockPos at = center.add(x, y, z);
            // Block top = world.getBiomeGenForCoords(at.getX(), at.getZ()).topBlock;
            // world.setBlock(at.getX(), at.getY(), at.getZ(), top);
        }
    }

    private void placeBlockCube(World world, BlockPos offset, int ox, int oy, int oz, int tx, int ty, int tz) {
        int lx, ly, lz;
        int hx, hy, hz;
        if (ox < tx) {
            lx = ox;
            hx = tx;
        } else {
            lx = tx;
            hx = ox;
        }
        if (oy < ty) {
            ly = oy;
            hy = ty;
        } else {
            ly = ty;
            hy = oy;
        }
        if (oz < tz) {
            lz = oz;
            hz = tz;
        } else {
            lz = tz;
            hz = oz;
        }

        // for (int xx = lx; xx <= hx; xx++) {
        // for (int zz = lz; zz <= hz; zz++) {
        // for (int yy = ly; yy <= hy; yy++) {
        // BlockPos at = offset.add(xx, yy, zz);
        // world.setBlock(at.getX(), at.getY(), at.getZ(), world.getBiomeGenForCoords(at.getX(), at.getZ()).topBlock);
        // }
        // }
        // }
    }

    private void load() {
        Block marble = BlocksAS.blockMarble;

        Block mch = marble;// .getDefaultState().withProperty(MARBLE_TYPE, BlockMarble.MarbleBlockType.CHISELED);
        Block mbr = marble;// .getDefaultState().withProperty(MARBLE_TYPE, BlockMarble.MarbleBlockType.BRICKS);
        Block mrw = marble;// .getDefaultState().withProperty(MARBLE_TYPE, BlockMarble.MarbleBlockType.RAW);
        Block mar = marble;// .getDefaultState().withProperty(MARBLE_TYPE, BlockMarble.MarbleBlockType.ARCH);
        Block mpl = marble;// .getDefaultState().withProperty(MARBLE_TYPE, BlockMarble.MarbleBlockType.PILLAR);
        Block air = Blocks.air;
        Block sand = Blocks.sand;

        addBlockCube(mrw, -4, 0, -3, 4, -7, 3);
        addBlockCube(mrw, -3, 0, -4, 3, -7, 4);

        addBlockCube(mar, -4, 0, -3, -4, 0, 3);
        addBlockCube(mar, 4, 0, -3, 4, 0, 3);
        addBlockCube(mar, -3, 0, -4, 3, 0, -4);
        addBlockCube(mar, -3, 0, 4, 3, 0, 4);

        addBlockCube(mbr, -3, 0, -3, 3, 0, 3);
        addBlockCube(mrw, -2, 0, -2, 2, 0, 2);

        addBlockCube(air, -2, -2, -2, 2, -5, 2);

        addBlockCube(mpl, -3, -2, -1, -3, -5, -1);
        addBlockCube(mpl, -3, -2, 1, -3, -5, 1);
        addBlockCube(mpl, 3, -2, -1, 3, -5, -1);
        addBlockCube(mpl, 3, -2, 1, 3, -5, 1);
        addBlockCube(mpl, -1, -2, -3, -1, -5, -3);
        addBlockCube(mpl, 1, -2, -3, 1, -5, -3);
        addBlockCube(mpl, -1, -2, 3, -1, -5, 3);
        addBlockCube(mpl, 1, -2, 3, 1, -5, 3);

        addBlockCube(mar, -2, -6, -2, 2, -6, 2);
        addBlockCube(air, -2, -6, 0, 2, -6, 0);
        addBlockCube(air, 0, -6, -2, 0, -6, 2);
        addBlock(-2, -6, -2, mrw);
        addBlock(-2, -6, 2, mrw);
        addBlock(2, -6, -2, mrw);
        addBlock(2, -6, 2, mrw);

        addBlock(-3, -3, 0, Blocks.water);
        addBlock(3, -3, 0, Blocks.water);
        addBlock(0, -3, -3, Blocks.water);
        addBlock(0, -3, 3, Blocks.water);

        TileEntityCallback lootCallback = new TileEntityCallback() {

            @Override
            public boolean isApplicable(TileEntity te) {
                return te != null && te instanceof TileEntityChest;
            }

            @Override
            public void onPlace(IBlockAccess access, BlockPos at, TileEntity te) {
                if (te instanceof TileEntityChest) {
                    // ((TileEntityChest) te).setLootTable(LootTableUtil.LOOT_TABLE_SHRINE, STATIC_RAND.nextLong());
                }
            }
        };

        addBlock(2, -5, -2, Blocks.chest);
        addTileCallback(new BlockPos(2, -5, -2), lootCallback);

        addBlock(0, -3, 0, BlocksAS.collectorCrystal);
        addTileCallback(new BlockPos(0, -3, 0), new TileEntityCallback() {

            @Override
            public boolean isApplicable(TileEntity te) {
                return te != null && te instanceof TileCollectorCrystal;
            }

            @Override
            public void onPlace(IBlockAccess access, BlockPos at, TileEntity te) {
                if (te instanceof TileCollectorCrystal) {
                    ((TileCollectorCrystal) te).onPlace(
                        MiscUtils.getRandomEntry(ConstellationRegistry.getMajorConstellations(), STATIC_RAND),
                        CrystalProperties.getMaxRockProperties(),
                        false,
                        BlockCollectorCrystalBase.CollectorCrystalType.ROCK_CRYSTAL);
                }
            }
        });
    }

}
