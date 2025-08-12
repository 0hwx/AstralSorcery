/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util.struct;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: PatternBlockArray
 * Created by HellFirePvP
 * Date: 30.07.2016 / 16:24
 */
public class PatternBlockArray extends BlockArray {

    private Map<BlockPos, TileEntityMatcher> matcherMap = new HashMap<>();

    public void addMatcher(BlockPos offset, TileEntityMatcher matcher) {
        matcherMap.put(offset, matcher);
    }

    public boolean matches(World world, BlockPos center) {
        for (Map.Entry<BlockPos, BlockInformation> entry : pattern.entrySet()) {
            BlockInformation info = entry.getValue();
            BlockPos at = center.add(entry.getKey());
            Block state = world.getBlock(at.getX(), at.getY(), at.getZ());
            if (!info.matcher.isStateValid(world, at, state)) {
                return false;
            }
            if (matcherMap.containsKey(entry.getKey())) {
                TileEntity te = world.getTileEntity(at.getX(), at.getY(), at.getZ());
                TileEntityMatcher matcher = matcherMap.get(entry.getKey());
                if (matcher.isApplicable(te) && !matcher.matches(world, entry.getKey(), te)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static interface TileEntityMatcher {

        public boolean isApplicable(TileEntity te);

        public boolean matches(World world, BlockPos at, TileEntity te);

    }

}
