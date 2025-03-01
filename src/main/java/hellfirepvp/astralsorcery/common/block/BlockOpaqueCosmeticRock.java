/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockOpaqueCosmetic
 * Created by HellFirePvP
 * Date: 12.05.2016 / 16:58
 */
public class BlockOpaqueCosmeticRock extends Block implements BlockCustomName {

    public static final AxisAlignedBB FULL_BLOCK_AABB = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockOpaqueCosmeticRock() {
        super(Material.rock);
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 3);
        setResistance(20.0F);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (BlockType bt : BlockType.values()) {
            list.add(new ItemStack(item, 1, bt.ordinal()));
        }
    }

//    @Override
//    public Block getStateFromMeta(int meta) {
//        return meta < BlockType.values().length ? getDefaultState().withProperty(BLOCK_TYPE, BlockType.values()[meta]) : getDefaultState();
//    }
//
//    @Override
//    public int getMeta(Block state) {
//        BlockType type = state.getValue(BLOCK_TYPE);
//        return type == null ? 0 : type.ordinal();
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, BLOCK_TYPE);
//    }

    @Override
    public String getIdentifierForMeta(int meta) {
        BlockType mt = meta < BlockOpaqueCosmeticRock.BlockType.values().length ? BlockOpaqueCosmeticRock.BlockType.values()[meta] : null;
        return mt == null ? "null" : mt.getName();
    }

    public static enum BlockType {

        NONE;


        public String getName() {
            return name().toLowerCase();
        }

    }

}
