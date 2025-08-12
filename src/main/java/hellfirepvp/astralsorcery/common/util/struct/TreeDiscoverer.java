package hellfirepvp.astralsorcery.common.util.struct;

import java.util.Stack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import hellfirepvp.astralsorcery.common.base.TreeTypes;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.BlockStateCheck;
import hellfirepvp.astralsorcery.common.util.data.Tuple;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TreeDiscoverer
 * Created by HellFirePvP
 * Date: 11.03.2017 / 12:01
 */
public class TreeDiscoverer {

    // Pass -1 into the limits to make unlimited.
    @Nullable
    public static BlockArray tryCaptureTreeAt(World world, BlockPos origin, int xzLimit, boolean cornerSpread) {
        Tuple<BlockStateCheck, BlockStateCheck> treeChecks = discoverTreeLogAndLeaf(world, origin);
        BlockStateCheck logCheck = treeChecks.key;
        BlockStateCheck leafCheck = treeChecks.value;

        // Nothing found to search for.
        if (logCheck == null && leafCheck == null) return null;

        int xzLimitSq = xzLimit == -1 ? -1 : xzLimit * xzLimit;

        BlockArray out = new BlockArray();
        Tuple<BlockStateCheck, BlockStateCheck> checks = itDiscoverAndAdd(
            world,
            origin,
            logCheck,
            leafCheck,
            xzLimitSq,
            cornerSpread,
            out);
        if (checks.key == null || checks.value == null) return null; // If we only have leaves or only logs, we didn't
                                                                     // really find a tree...
        return out;
    }

    private static Tuple<BlockStateCheck, BlockStateCheck> itDiscoverAndAdd(World world, BlockPos origin,
        BlockStateCheck logCheck, BlockStateCheck leafCheck, int xzLimitSq, boolean cornerSpread, BlockArray out) {
        Stack<BlockPos> offsetPositions = new Stack<>();
        offsetPositions.add(origin);
        while (!offsetPositions.isEmpty()) {
            BlockPos offset = offsetPositions.pop();

            Block atBlock = world.getBlock(offset.getX(), offset.getY(), offset.getZ());
            int atMeta = world.getBlockMetadata(offset.getX(), offset.getY(), offset.getZ());
            boolean successful = false;
            Tuple<BlockStateCheck, BlockStateCheck> atChecks = null;
            if (logCheck == null || leafCheck == null) {
                atChecks = discoverTreeLogAndLeaf(world, offset);
            }
            if (logCheck == null && atChecks.key != null) {
                logCheck = atChecks.key;
            }
            if (leafCheck == null && atChecks.value != null) {
                leafCheck = atChecks.value;
            }
            if (logCheck != null) {
                if (logCheck.isStateValid(world, offset.getX(), offset.getY(), offset.getZ(), atBlock)) {
                    out.addBlock(offset, atBlock, atMeta);
                    successful = true;
                }
            }
            if (leafCheck != null) {
                if (leafCheck.isStateValid(world, offset.getX(), offset.getY(), offset.getZ(), atBlock)) {
                    out.addBlock(offset, atBlock, atMeta);
                    successful = true;
                }
            }

            if (successful) {
                if (cornerSpread) {
                    for (int xx = -1; xx <= 1; xx++) {
                        for (int yy = -1; yy <= 1; yy++) {
                            for (int zz = -1; zz <= 1; zz++) {
                                BlockPos newPos = offset.add(xx, yy, zz);
                                if ((xzLimitSq == -1 || flatDistanceSq(newPos, origin) <= xzLimitSq)
                                    && !out.hasBlockAt(newPos)) {
                                    offsetPositions.push(newPos);
                                }
                            }
                        }
                    }
                } else {
                    for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
                        BlockPos newPos = offset.add(face.offsetX, face.offsetY, face.offsetZ);
                        if ((xzLimitSq == -1 || flatDistanceSq(newPos, origin) <= xzLimitSq)
                            && !out.hasBlockAt(newPos)) {
                            offsetPositions.push(newPos);
                        }
                    }
                }
            }
        }
        return new Tuple<>(logCheck, leafCheck);
    }

    private static double flatDistanceSq(BlockPos from, BlockPos to) {
        double xDiff = (double) from.getX() - to.getX();
        double zDiff = (double) from.getZ() - to.getZ();
        return xDiff * xDiff + zDiff * zDiff;
    }

    // LogStateCheck, LeafStateCheck
    @Nonnull
    private static Tuple<BlockStateCheck, BlockStateCheck> discoverTreeLogAndLeaf(World world, BlockPos pos) {
        BlockStateCheck logCheck = null, leafCheck = null;

        TreeTypes t = TreeTypes.getTree(world, pos);
        if (t != null) {
            Block atBlock = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            if (t.getLogCheck()
                .isStateValid(world, pos.getX(), pos.getY(), pos.getZ(), atBlock)) {
                logCheck = t.getLogCheck();
            }
            if (t.getLeavesCheck()
                .isStateValid(world, pos.getX(), pos.getY(), pos.getZ(), atBlock)) {
                leafCheck = t.getLeavesCheck();
            }
        } else {
            Block atBlock = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            if (atBlock instanceof BlockLog) {
                logCheck = new BlockStateCheck.Block(atBlock);
            } else if (atBlock.isWood(world, pos.getX(), pos.getY(), pos.getZ())) {
                logCheck = new BlockStateCheck.Block(atBlock);
            }
            if (atBlock instanceof BlockLeaves) {
                leafCheck = new BlockStateCheck.Block(atBlock);
            } else if (atBlock.isLeaves(world, pos.getX(), pos.getY(), pos.getZ())) {
                leafCheck = new BlockStateCheck.Block(atBlock);
            }
        }
        return new Tuple<>(logCheck, leafCheck);
    }

}
