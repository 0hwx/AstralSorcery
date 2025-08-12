package hellfirepvp.astralsorcery.common.block;

import net.minecraft.block.BlockStairs;

import hellfirepvp.astralsorcery.common.registry.RegistryItems;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockMarbleStairs
 * Created by HellFirePvP
 * Date: 26.04.2017 / 11:32
 */
public class BlockMarbleStairs extends BlockStairs {

    public BlockMarbleStairs() {
        super(BlockMarble.MarbleBlockType.BRICKS.asBlock(), BlockMarble.MarbleBlockType.BRICKS.getMeta());
        setBlockName("BlockMarbleStairs");
        setHardness(1.0F);
        setHarvestLevel("pickaxe", 1);
        setResistance(3.0F);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }
}
