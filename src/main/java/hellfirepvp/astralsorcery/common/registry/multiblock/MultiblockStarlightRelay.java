/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.registry.multiblock;

import net.minecraft.block.Block;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.util.struct.PatternBlockArray;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: MultiblockStarlightRelay
 * Created by HellFirePvP
 * Date: 30.03.2017 / 14:07
 */
public class MultiblockStarlightRelay extends PatternBlockArray {

    public MultiblockStarlightRelay() {
        load();
    }

    private void load() {
        Block marble = BlocksAS.blockMarble;
        Block chiseled = marble;// .getDefaultState().withProperty(BlockMarble.MARBLE_TYPE,
                                // BlockMarble.MarbleBlockType.CHISELED);
        Block arch = marble;// .getDefaultState().withProperty(BlockMarble.MARBLE_TYPE,
                            // BlockMarble.MarbleBlockType.ARCH);
        Block sooty = BlocksAS.blockBlackMarble;// .getDefaultState().withProperty(BlockBlackMarble.BLACK_MARBLE_TYPE,
                                                // BlockBlackMarble.BlackMarbleBlockType.RAW);

        addBlock(0, 0, 0, BlocksAS.attunementRelay);// .getDefaultState());

        addBlock(-1, -1, -1, chiseled);
        addBlock(1, -1, -1, chiseled);
        addBlock(1, -1, 1, chiseled);
        addBlock(-1, -1, 1, chiseled);

        addBlock(-1, -1, 0, arch);
        addBlock(1, -1, 0, arch);
        addBlock(0, -1, 1, arch);
        addBlock(0, -1, -1, arch);
        addBlock(0, -1, 0, sooty);
    }

}
