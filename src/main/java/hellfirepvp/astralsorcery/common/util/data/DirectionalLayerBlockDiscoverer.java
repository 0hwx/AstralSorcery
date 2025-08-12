/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util.data;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: DirectionalLayerBlockDiscoverer
 * Created by HellFirePvP
 * Date: 01.11.2016 / 15:53
 */
public class DirectionalLayerBlockDiscoverer {

    private World world;
    private BlockPos start;
    private int rad, stepWidth;

    public DirectionalLayerBlockDiscoverer(World world, BlockPos start, int discoverRadius, int stepWidth) {
        this.world = world;
        this.start = start;
        this.rad = discoverRadius;
        this.stepWidth = stepWidth;
    }

    public LinkedList<BlockPos> discoverApplicableBlocks() {
        LinkedList<BlockPos> visited = new LinkedList<>();
        // LinkedList<BlockPos> validCache = new LinkedList<>();

        int xPos = start.getX();
        int yPos = start.getY();
        int zPos = start.getZ();
        BlockPos currentPos = start;
        tryAdd(start, visited);

        ForgeDirection dir = NORTH;
        while (Math.abs(currentPos.getX() - xPos) <= rad && Math.abs(currentPos.getY() - yPos) <= rad
            && Math.abs(currentPos.getZ() - zPos) <= rad) {
            currentPos = currentPos.offset(dir, stepWidth);
            tryAdd(currentPos, visited);
            ForgeDirection tryDirNext = rotateY(dir);
            if (!visited.contains(currentPos.offset(tryDirNext, stepWidth))) {
                dir = tryDirNext;
            }
        }

        return visited;
    }

    private void tryAdd(BlockPos at, List<BlockPos> visited) {
        if (!visited.contains(at)) {
            visited.add(at);
        }
    }

    public ForgeDirection rotateY(ForgeDirection dir) {
        switch (dir) {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            default:
                throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        }
    }

}
