/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util.struct;

import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.BlockStateCheck;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

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

//        BlockPos.PooledMutableBlockPos offset = BlockPos.PooledMutableBlockPos.retain();
//        for (int xx = -cubeSize; xx <= cubeSize; xx++) {
//            for (int zz = -cubeSize; zz <= cubeSize; zz++) {
//                for (int yy = -cubeSize; yy <= cubeSize; yy++) {
//                    offset.setPos(origin.getX() + xx, origin.getY() + yy, origin.getZ() + zz);
//                    Block atState = world.getBlockState(offset);
//                    if(match.isStateValid(world, offset, atState)) {
//                        out.addBlock(new BlockPos(offset), atState);
//                    }
//                }
//            }
//        }
//        offset.release();
        return out;
    }

    public static BlockArray discoverBlocksWithSameStateAround(World world, BlockPos origin, boolean onlyExposed, int cubeSize, int limit, boolean searchCorners) {
        Block toMatch = world.getBlock(origin.getX(), origin.getY(), origin.getZ());
        Block matchBlock = toMatch;
        int matchMeta = world.getBlockMetadata(origin.getX(), origin.getY(), origin.getZ());;

        BlockArray foundArray = new BlockArray();
        foundArray.addBlock(origin, toMatch);
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
                                if (limit != -1 && foundArray.pattern.size() + 1 > limit) continue;

                                visited.add(search);

                                if (!onlyExposed || isExposedToAir(world, search)) {
                                    Block current = world.getBlock(search.getX(), search.getY(), search.getZ());
                                    int currentMeta = world.getBlockMetadata(search.getX(), search.getY(), search.getZ());
                                    if (current == matchBlock && currentMeta == matchMeta) {
                                        foundArray.addBlock(search, current);
                                        searchNext.add(search);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (ForgeDirection face : ForgeDirection.values()) {
                        BlockPos search = offsetPos.offset(face);
                        if (visited.contains(search)) continue;
                        if (getCubeDistance(search, origin) > cubeSize) continue;
                        if (limit != -1 && foundArray.pattern.size() + 1 > limit) continue;

                        visited.add(search);

                        if (!onlyExposed || isExposedToAir(world, search)) {
                            Block current = world.getBlock(search.getX(), search.getY(), search.getZ());
                            int currentMeta = world.getBlockMetadata(search.getX(), search.getY(), search.getZ());
                            if (current == matchBlock && currentMeta == matchMeta) {
                                foundArray.addBlock(search, current);
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
        return (int) MathHelper.abs_max(MathHelper.abs_max(p1.getX() - p2.getX(), p1.getY() - p2.getY()), p1.getZ() - p2.getZ());
    }

    public static boolean isExposedToAir(World world, BlockPos pos) {
        for (ForgeDirection face : ForgeDirection.values()) {
            BlockPos offset = pos.offset(face);
            if (world.isAirBlock(offset.getX(), offset.getY(), offset.getZ()) || world.getBlock(offset.getX(), offset.getY(), offset.getZ()).isReplaceable(world, offset.getX(), offset.getY(), offset.getZ())) return true;
        }
        return false;
    }

}
