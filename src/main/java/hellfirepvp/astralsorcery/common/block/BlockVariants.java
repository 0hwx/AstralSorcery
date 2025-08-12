/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import java.util.List;

import net.minecraft.block.Block;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockVariants
 * Created by HellFirePvP
 * Date: 31.07.2016 / 09:30
 */
public interface BlockVariants {

    public List<Block> getValidStates();

    public String getMetaName(int meta);

    int getMeta();

    default public String getBlockName(Block state) {
        return state.getClass()
            .getSimpleName();
    }

}
