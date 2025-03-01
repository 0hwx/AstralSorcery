/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.util;


import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;


import javax.annotation.Nullable;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: AirBlockRenderWorld
 * Created by HellFirePvP
 * Date: 07.02.2017 / 01:32
 */
public class AirBlockRenderWorld implements IBlockAccess {

    private final BiomeGenBase globalBiome;
    private final WorldType globalType;

    public AirBlockRenderWorld(BiomeGenBase globalBiome, WorldType globalType) {
        this.globalBiome = globalBiome;
        this.globalType = globalType;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return null;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        return null;
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int lightValue) {
        return 0;
    }

//    @Override
//    public int getCombinedLight(BlockPos pos, int lightValue) {
//        return 0;
//    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        return 0;
    }
//    public  Block getBlockState(BlockPos pos) {
//        return Blocks.AIR.getDefaultState();
//    }

    @Override
    public boolean isAirBlock(int x, int y, int z) {
        return true;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        return globalBiome;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return false;
    }

    @Override
    public int isBlockProvidingPowerTo(int x, int y, int z, int directionIn) {
        return 0;
    }

//    @Override
//    public WorldType getWorldType() {
//        return globalType;
//    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
        return _default;
    }

}
