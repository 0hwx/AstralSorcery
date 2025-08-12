/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.world.structure;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

import hellfirepvp.astralsorcery.common.data.world.data.StructureGenBuffer;
import hellfirepvp.astralsorcery.common.lib.MultiBlockArrays;
import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: StructureDesertShrine
 * Created by HellFirePvP
 * Date: 21.10.2016 / 13:43
 */
public class StructureDesertShrine extends WorldGenAttributeStructure {

    public StructureDesertShrine() {
        super(
            0,
            "desertStructure",
            () -> MultiBlockArrays.desertShrine,
            StructureGenBuffer.StructureType.DESERT,
            BiomeDictionary.Type.SANDY);
        this.idealDistance = 512F;
    }

    @Override
    public void generate(BlockPos pos, World world, Random rand) {
        getStructureTemplate().placeInWorld(world, pos.down());
        getBuffer(world).markStructureGeneration(pos, getStructureType());
    }

    @Override
    public boolean fulfillsSpecificConditions(BlockPos pos, World world, Random random) {
        if (!isDesertBiome(world, pos)) return false;
        if (!canSpawnShrineCorner(world, pos.add(-4, 0, 4))) return false;
        if (!canSpawnShrineCorner(world, pos.add(4, 0, -4))) return false;
        if (!canSpawnShrineCorner(world, pos.add(4, 0, 4))) return false;
        if (!canSpawnShrineCorner(world, pos.add(-4, 0, -4))) return false;
        return true;
    }

    @Override
    public BlockPos getGenerationPosition(int chX, int chZ, World world, Random rand) {
        int rX = (chX * 16) + rand.nextInt(16) + 8;
        int rZ = (chZ * 16) + rand.nextInt(16) + 8;
        int rY = world.getTopSolidOrLiquidBlock(rX, rZ);
        return new BlockPos(rX, rY, rZ);
    }

    private boolean canSpawnShrineCorner(World world, BlockPos pos) {
        int dY = world.getTopSolidOrLiquidBlock(pos.getX(), pos.getZ());
        return dY >= cfgEntry.getMinY() && dY <= cfgEntry.getMaxY()
            && Math.abs(dY - pos.getY()) <= 4
            && isDesertBiome(world, pos);
    }

    private boolean isDesertBiome(World world, BlockPos pos) {
        if (cfgEntry.shouldIgnoreBiomeSpecifications()) return true;

        // BiomeGenBase b = world.getBiomeGenForCoords(pos.getX(), pos.getZ());
        // BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(b);
        // if(types == null || types.length == 0) return false;
        boolean applicable = false;
        // for (BiomeDictionary.Type t : types) {
        // if (cfgEntry.getTypes().contains(t)) applicable = true;
        // }
        return applicable;
    }
}
