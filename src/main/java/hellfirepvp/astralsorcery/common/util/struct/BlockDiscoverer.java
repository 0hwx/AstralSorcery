/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util.struct;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.BlockStateCheck;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockDiscoverer
 * Created by HellFirePvP
 * Date: 07.02.2017 / 01:09
 */
public class BlockDiscoverer {

    public static BlockArray searchForBlocksAround(World world, BlockPos origin, int cubeSize, BlockStateCheck match) {
        BlockArray out = new BlockArray();

        for (int xx = -cubeSize; xx <= cubeSize; xx++) {
            for (int zz = -cubeSize; zz <= cubeSize; zz++) {
                for (int yy = -cubeSize; yy <= cubeSize; yy++) {
                    int x = origin.getX() + xx;
                    int y = origin.getY() + yy;
                    int z = origin.getZ() + zz;
                    BlockPos offset = new BlockPos(x, y, z);
                    Block block = world.getBlock(x, y, z);
                    int metadata = world.getBlockMetadata(x, y, z);

                    if (match.isStateValid(world, x, y, z, block)) {
                        out.addBlock(offset, block, metadata);
                    }
                }
            }
        }
        return out;
    }

    public static BlockArray discoverBlocksWithSameStateAround(World world, BlockPos origin, boolean onlyExposed,
        int cubeSize, int limit, boolean searchCorners) {
        Block matchBlock = world.getBlock(origin.getX(), origin.getY(), origin.getZ());
        int matchMeta = world.getBlockMetadata(origin.getX(), origin.getY(), origin.getZ());

        BlockArray foundArray = new BlockArray();
        foundArray.addBlock(origin, matchBlock, matchMeta);
        List<BlockPos> visited = new LinkedList<>();

        Deque<BlockPos> searchNext = new LinkedList<>();
        searchNext.addFirst(origin);

        while (!searchNext.isEmpty()) {
            Deque<BlockPos> currentSearch = searchNext;
            searchNext = new LinkedList<>();

            for (BlockPos offsetPos : currentSearch) {
                if (searchCorners) {
                    for (int xx = -1; xx <= 1; xx++) {
                        for (int yy = -1; yy <= 1; yy++) {
                            for (int zz = -1; zz <= 1; zz++) {
                                BlockPos search = offsetPos.add(xx, yy, zz);
                                if (visited.contains(search)) continue;
                                if (getCubeDistance(search, origin) > cubeSize) continue;
                                if (limit != -1 && foundArray.getPattern()
                                    .size() + 1 > limit) continue;

                                visited.add(search);

                                if (!onlyExposed || isExposedToAir(world, search)) {
                                    Block currentBlock = world.getBlock(search.getX(), search.getY(), search.getZ());
                                    int currentMeta = world
                                        .getBlockMetadata(search.getX(), search.getY(), search.getZ());
                                    if (currentBlock == matchBlock && currentMeta == matchMeta) {
                                        foundArray.addBlock(search, currentBlock, currentMeta);
                                        searchNext.add(search);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
                        BlockPos search = offsetPos.add(face.offsetX, face.offsetY, face.offsetZ);
                        if (visited.contains(search)) continue;
                        if (getCubeDistance(search, origin) > cubeSize) continue;
                        if (limit != -1 && foundArray.getPattern()
                            .size() + 1 > limit) continue;

                        visited.add(search);

                        if (!onlyExposed || isExposedToAir(world, search)) {
                            Block currentBlock = world.getBlock(search.getX(), search.getY(), search.getZ());
                            int currentMeta = world.getBlockMetadata(search.getX(), search.getY(), search.getZ());
                            if (currentBlock == matchBlock && currentMeta == matchMeta) {
                                foundArray.addBlock(search, currentBlock, currentMeta);
                                searchNext.add(search);
                            }
                        }
                    }
                }
            }
        }

        return foundArray;
    }

    public static int getCubeDistance(BlockPos p1, BlockPos p2) {
        return (int) MathHelper
            .abs_max(MathHelper.abs_max(p1.getX() - p2.getX(), p1.getY() - p2.getY()), p1.getZ() - p2.getZ());
    }

    public static boolean isExposedToAir(World world, BlockPos pos) {
        for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
            int x = pos.getX() + face.offsetX;
            int y = pos.getY() + face.offsetY;
            int z = pos.getZ() + face.offsetZ;
            if (world.isAirBlock(x, y, z)) return true;
        }
        return false;
    }

}
