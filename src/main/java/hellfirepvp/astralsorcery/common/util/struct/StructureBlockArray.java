/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util.struct;


import hellfirepvp.astralsorcery.common.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Map;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: MultiBlockArray
 * Created by HellFirePvP
 * Date: 30.07.2016 / 16:07
 */
public class StructureBlockArray extends BlockArray {

    public Map<BlockPos, Block> placeInWorld(World world, BlockPos center, PastPlaceProcessor processor) {
        Map<BlockPos, Block> result = super.placeInWorld(world, center);
        if(processor != null) {
            for (Map.Entry<BlockPos, Block> entry : result.entrySet())
                processor.process(world, entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static interface PastPlaceProcessor {

        public void process(World world, BlockPos pos, Block currentState);

    }

}
