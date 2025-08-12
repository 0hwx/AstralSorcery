package hellfirepvp.astralsorcery.common.registry.multiblock;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import hellfirepvp.astralsorcery.common.block.BlockMarble;
import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystalBase;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.item.crystal.CrystalProperties;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.tile.network.TileCollectorCrystal;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.struct.PatternBlockArray;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: MultiblockCrystalEnhancement
 * Created by HellFirePvP
 * Date: 22.04.2017 / 17:05
 */
public class MultiblockCrystalEnhancement extends PatternBlockArray {

    public MultiblockCrystalEnhancement() {
        load();
    }

    private void load() {
        Block mru = BlockMarble.MarbleBlockType.RUNED.asBlock();
        Block mpl = BlockMarble.MarbleBlockType.PILLAR.asBlock();
        Block mch = BlockMarble.MarbleBlockType.CHISELED.asBlock();
        Block mgr = BlockMarble.MarbleBlockType.ENGRAVED.asBlock();
        Block mrw = BlockMarble.MarbleBlockType.RAW.asBlock();

        addBlockCube(mrw, -1, -5, -1, 1, -5, 1);
        for (BlockPos offset : TileCollectorCrystal.offsetsLiquidStarlight) {
            addBlock(
                offset,
                BlocksAS.blockLiquidStarlight,
                (world, pos, state) -> state.equals(BlocksAS.blockLiquidStarlight)
                    && world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ()) == 0);
        }
        addBlockCube(Blocks.air, 1, 1, 1, -1, -1, -1);
        addBlock(0, 0, 0, BlocksAS.celestialCollectorCrystal);
        addTileCallback(BlockPos.ORIGIN, new TileEntityCallback() {

            @Override
            public boolean isApplicable(TileEntity te) {
                return te != null && te instanceof TileCollectorCrystal;
            }

            @Override
            public void onPlace(IBlockAccess access, BlockPos at, TileEntity te) {
                IWeakConstellation rand = ConstellationRegistry.getWeakConstellations()
                    .get(
                        new Random().nextInt(
                            ConstellationRegistry.getWeakConstellations()
                                .size()));
                ((TileCollectorCrystal) te).onPlace(
                    rand,
                    CrystalProperties.getMaxCelestialProperties(),
                    true,
                    BlockCollectorCrystalBase.CollectorCrystalType.CELESTIAL_CRYSTAL);
            }
        });

        addBlock(0, -2, 0, mch);
        addBlock(0, -3, 0, mpl);
        addBlock(0, -4, 0, mpl);

        addBlock(-2, -4, -2, mch);
        addBlock(-2, -4, 2, mch);
        addBlock(2, -4, 2, mch);
        addBlock(2, -4, -2, mch);
        addBlock(-2, -3, -2, mgr);
        addBlock(-2, -3, 2, mgr);
        addBlock(2, -3, 2, mgr);
        addBlock(2, -3, -2, mgr);

        addBlock(-2, -4, -1, mru);
        addBlock(-2, -4, 0, mru);
        addBlock(-2, -4, 1, mru);
        addBlock(2, -4, -1, mru);
        addBlock(2, -4, 0, mru);
        addBlock(2, -4, 1, mru);
        addBlock(-1, -4, -2, mru);
        addBlock(0, -4, -2, mru);
        addBlock(1, -4, -2, mru);
        addBlock(-1, -4, 2, mru);
        addBlock(0, -4, 2, mru);
        addBlock(1, -4, 2, mru);
    }

}
