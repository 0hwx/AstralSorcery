package hellfirepvp.astralsorcery.common.util.struct;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: OreDiscoverer
 * Created by HellFirePvP
 * Date: 12.03.2017 / 14:06
 */
public class OreDiscoverer {

    public static BlockArray startSearch(World world, Vector3 position, int xzLimit) {
        xzLimit = MathHelper.clamp_int(xzLimit, 0, 32);
        BlockPos originPos = position.toBlockPos();
        BlockArray out = new BlockArray();
        List<BlockOreInfo> successfulOres = new ArrayList<>(12);

        for (int xx = -xzLimit; xx <= xzLimit; xx++) {
            for (int zz = -xzLimit; zz <= xzLimit; zz++) {
                int chunkX = originPos.getX() + xx;
                int chunkZ = originPos.getZ() + zz;
                Chunk c = world.getChunkFromBlockCoords(chunkX, chunkZ);
                int highest = (c.getTopFilledSegment() + 1) * 16; // Get height limit for chunk

                for (int y = 0; y < highest; y++) {
                    BlockPos currentPos = new BlockPos(chunkX, y, chunkZ);
                    Block block = world.getBlock(chunkX, y, chunkZ);
                    int metadata = world.getBlockMetadata(chunkX, y, chunkZ);

                    BlockOreInfo currentOre = new BlockOreInfo(block, metadata);
                    if (successfulOres.contains(currentOre)) {
                        out.addBlock(currentPos, block, metadata);
                    } else if (isOre(world, block, metadata, currentPos)) {
                        out.addBlock(currentPos, block, metadata);
                        successfulOres.add(currentOre);
                    }
                }
            }
        }
        return out;
    }

    private static boolean isOre(World world, Block block, int metadata, BlockPos pos) {
        if (block instanceof BlockOre) { // WELL that's easy enough.
            return true;
        }
        ItemStack blockStack = ItemUtils.createBlockStack(block, metadata);
        return blockStack != null && ItemUtils.hasOreNamePart(blockStack, "ore");
    }

    // Helper class to track discovered ore types
    private static class BlockOreInfo {

        private final Block block;
        private final int metadata;

        public BlockOreInfo(Block block, int metadata) {
            this.block = block;
            this.metadata = metadata;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BlockOreInfo)) return false;
            BlockOreInfo other = (BlockOreInfo) obj;
            return block == other.block && metadata == other.metadata;
        }

        @Override
        public int hashCode() {
            return block.hashCode() * 31 + metadata;
        }
    }

}
