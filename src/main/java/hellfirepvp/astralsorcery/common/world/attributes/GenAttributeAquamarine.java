/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.world.attributes;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import hellfirepvp.astralsorcery.common.block.BlockCustomSandOre;
import hellfirepvp.astralsorcery.common.data.config.Config;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.world.WorldGenAttribute;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: GenAttributeAquamarine
 * Created by HellFirePvP
 * Date: 12.01.2017 / 21:59
 */
public class GenAttributeAquamarine extends WorldGenAttribute {

    public GenAttributeAquamarine() {
        super(0);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world) {
        for (int i = 0; i < Config.aquamarineAmount; i++) {
            int rX = (chunkX * 16) + random.nextInt(16) + 8;
            int rY = 48 + random.nextInt(19);
            int rZ = (chunkZ * 16) + random.nextInt(16) + 8;
            BlockPos pos = new BlockPos(rX, rY, rZ);
            Block stateAt = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            if (!stateAt.equals(Blocks.sand)) {
                continue;
            }

            boolean foundWater = false;
            for (int yy = 0; yy < 2; yy++) {
                BlockPos check = pos.offset(ForgeDirection.UP, yy);
                Block bs = world.getBlock(check.getX(), check.getY(), check.getZ());
                Block block = bs;
                if ((block instanceof BlockLiquid && bs.getMaterial() == Material.water) || block.equals(Blocks.ice)
                    || block.equals(Blocks.packed_ice)) {// || block.equals(Blocks.FROSTED_ICE)) {
                    foundWater = true;
                    break;
                }
            }
            if (!foundWater) continue;

            world.setBlock(
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                BlocksAS.customSandOre,
                BlockCustomSandOre.OreType.AQUAMARINE.getMeta(),
                3);
            // .withProperty(BlockCustomSandOre.ORE_TYPE, BlockCustomSandOre.OreType.AQUAMARINE));
        }
    }
}
