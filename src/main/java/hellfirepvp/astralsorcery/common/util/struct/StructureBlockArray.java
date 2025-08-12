/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util.struct;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: MultiBlockArray
 * Created by HellFirePvP
 * Date: 30.07.2016 / 16:07
 */
public class StructureBlockArray extends BlockArray {

    public Map<BlockPos, BlockInformation> placeInWorld(World world, BlockPos center, PastPlaceProcessor processor) {
        Map<BlockPos, BlockInformation> result = super.placeInWorld(world, center);
        if (processor != null) {
            for (Map.Entry<BlockPos, BlockInformation> entry : result.entrySet())
                processor.process(world, entry.getKey(), entry.getValue().type, entry.getValue().metadata);
        }
        return result;
    }

    public static interface PastPlaceProcessor {

        public void process(World world, BlockPos pos, Block block, int meta);

    }

}
