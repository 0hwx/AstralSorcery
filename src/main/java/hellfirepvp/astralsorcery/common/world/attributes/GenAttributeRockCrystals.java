/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.world.attributes;

import hellfirepvp.astralsorcery.common.block.BlockCustomOre;
import hellfirepvp.astralsorcery.common.data.config.Config;
import hellfirepvp.astralsorcery.common.data.world.WorldCacheManager;
import hellfirepvp.astralsorcery.common.data.world.data.RockCrystalBuffer;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.world.WorldGenAttribute;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Random;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: GenAttributeRockCrystals
 * Created by HellFirePvP
 * Date: 12.01.2017 / 21:52
 */
public class GenAttributeRockCrystals extends WorldGenAttribute {

    public GenAttributeRockCrystals() {
        super(0);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world) {
        if (Config.crystalDensity <= 0 || random.nextInt(Config.crystalDensity) == 0) {
            int xPos = chunkX * 16 + random.nextInt(16) + 8;
            int zPos = chunkZ * 16 + random.nextInt(16) + 8;
            int yPos = 2 + random.nextInt(4);
            BlockPos pos = new BlockPos(xPos, yPos, zPos);
            Block state = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            if (state.equals(Blocks.stone)) {
//                BlockStone stoneType = state.getItemDropped(BlockStone.VARIANT);
//                if (stoneType != null && stoneType.equals(BlockStone.EnumType.STONE)) {
                    int newState = BlocksAS.customOre.damageDropped(BlockCustomOre.OreType.ROCK_CRYSTAL.getMeta());//getDefaultState().withProperty(BlockCustomOre.ORE_TYPE, BlockCustomOre.OreType.ROCK_CRYSTAL);
                    world.setBlock(pos.getX(), pos.getY(), pos.getZ(),state, newState,3);
                    RockCrystalBuffer buf = WorldCacheManager.getOrLoadData(world, WorldCacheManager.SaveKey.ROCK_CRYSTAL);
                    buf.addOre(pos);
//                }
            }
        }
    }
}
