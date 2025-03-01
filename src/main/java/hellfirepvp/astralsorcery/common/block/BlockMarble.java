/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockMarble
 * Created by HellFirePvP
 * Date: 22.05.2016 / 16:13
 */
public class BlockMarble extends Block implements BlockCustomName{

    //private static final int RAND_MOSS_CHANCE = 10;
    private IIcon[][] icons = new IIcon[MarbleBlockType.values().length][6];

    public BlockMarble() {
        super(Material.rock);
        setHardness(1.0F);
        setHarvestLevel("pickaxe", 1);
        setResistance(3.0F);
        setBlockName("BlockMarble");
//        setSoundType(SoundType.STONE);
//        setTickRandomly(true);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
//        setDefaultState(this.blockState.getBaseState().withProperty(MARBLE_TYPE, MarbleBlockType.RAW));
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (MarbleBlockType t : MarbleBlockType.values()) {
//            if(!t.obtainableInCreative()) continue;
            list.add(new ItemStack(item, 1, t.getMeta()));
        }
    }

    /*@Override
    public void updateTick(World worldIn, int x, int y, int z, Random rand) {
        if (!worldIn.isRemote && worldIn.isRaining() && rand.nextInt(RAND_MOSS_CHANCE) == 0) {
            MarbleBlockType type = state.getValue(MARBLE_TYPE);
            if (type.canTurnMossy() && worldIn.isRainingAt(pos)) {
                worldIn.setBlockState(pos, state.withProperty(MARBLE_TYPE, type.getMossyEquivalent()), 3);
            }
        }
    }*/

//    @Override
//    public Block getActualState(Block state, IBlockAccess worldIn, BlockPos pos) {
//        //return super.getActualState(state, worldIn, pos);
//        if(state.getValue(MARBLE_TYPE).isPillar()) {
//            Block st = worldIn.getBlockState(pos.up());
//            boolean top = false;
//            if(st.getBlock() instanceof BlockMarble && st.getValue(MARBLE_TYPE).isPillar()) {
//                top = true;
//            }
//            st = worldIn.getBlockState(pos.down());
//            boolean down = false;
//            if(st.getBlock() instanceof BlockMarble && st.getValue(MARBLE_TYPE).isPillar()) {
//                down = true;
//            }
//            if(top && down) {
//                return state.withProperty(MARBLE_TYPE, MarbleBlockType.PILLAR);
//            } else if(top) {
//                return state.withProperty(MARBLE_TYPE, MarbleBlockType.PILLAR_BOTTOM);
//            } else if(down) {
//                return state.withProperty(MARBLE_TYPE, MarbleBlockType.PILLAR_TOP);
//            } else {
//                return state.withProperty(MARBLE_TYPE, MarbleBlockType.PILLAR);
//            }
//        }
//        return super.getActualState(state, worldIn, pos);
//    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean isOpaqueCube() {
//        for (MarbleBlockType marbleType : MarbleBlockType.values()) {
//          return marbleType != MarbleBlockType.PILLAR && marbleType != MarbleBlockType.PILLAR_BOTTOM && marbleType != MarbleBlockType.PILLAR_TOP;
//        }
        return false;
    }

    @Override
    public boolean isNormalCube() {
//        for (MarbleBlockType marbleType : MarbleBlockType.values()) {
//            return marbleType != MarbleBlockType.PILLAR && marbleType != MarbleBlockType.PILLAR_BOTTOM && marbleType != MarbleBlockType.PILLAR_TOP;
//        }
        return false;
    }

    @Override
    public boolean func_149730_j() { //isFullBlock
//        for (MarbleBlockType marbleType : MarbleBlockType.values()) {
//            return marbleType != MarbleBlockType.PILLAR && marbleType != MarbleBlockType.PILLAR_BOTTOM && marbleType != MarbleBlockType.PILLAR_TOP;
//        }
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

//    @Override
//    public boolean isFullyOpaque() {
//        return true;
//    }

    @Override
    public String getIdentifierForMeta(int meta) {
        MarbleBlockType type = MarbleBlockType.values()[meta];
        return type.getName();
    }

//    @Override
//    public int getMeta(Block state) {
//        MarbleBlockType type = state.getValue(MARBLE_TYPE);
//        return type == null ? 0 : type.getMeta();
//    }

//    @Override
//    public Block getStateFromMeta(int meta) {
//        return meta < MarbleBlockType.values().length ? getDefaultState().withProperty(MARBLE_TYPE, MarbleBlockType.values()[meta]) : getDefaultState();
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, MARBLE_TYPE);
//    }

//    @Override
//    public List<Block> getValidStates() {
//        List<Block> ret = new LinkedList<>();
//        for (MarbleBlockType type : MarbleBlockType.values()) {
//            ret.add(type.asBlock());
//        }
//        return ret;
//    }
//
//    @Override
//    public String getMetaName(int meta) {
//        MarbleBlockType type = MarbleBlockType.values()[meta];
//        return type.getName();
//    }
//
//    @Override
//    public int getMeta() {
//        for (MarbleBlockType type : MarbleBlockType.values()) {
//             return type.getMeta();
//        }
//        return 0;
//    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
//        int meta = worldIn.getBlockMetadata(x, y, z);
//        MarbleBlockType marbleType = MarbleBlockType.values()[meta];
//        ForgeDirection direction = ForgeDirection.getOrientation(side);
//        // If the adjacent block is a liquid or fluid, skip rendering
//        Block adjacentBlock = worldIn.getBlock(x, y, z);
//        if((adjacentBlock instanceof BlockLiquid || adjacentBlock instanceof BlockFluidBase) &&
//            (marbleType == MarbleBlockType.PILLAR)) {// || marbleType == MarbleBlockType.PILLAR_BOTTOM || marbleType == MarbleBlockType.PILLAR_TOP)) {
//            return false;
//        }
//        if(marbleType == MarbleBlockType.PILLAR_TOP) {
//            return direction == ForgeDirection.UP;
//        }
//        if(marbleType == MarbleBlockType.PILLAR_BOTTOM) {
//            return direction == ForgeDirection.DOWN;
//        }
        return super.shouldSideBeRendered(worldIn, x, y, z, side);
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return icons[meta % icons.length][side < 2 ? 1 : 0];
//        if (meta >= 0 && meta < MarbleBlockType.values().length) {
//            MarbleBlockType marbleType = MarbleBlockType.values()[meta];
//
//            // If it's a pillar, return the correct side-based texture
////            if (marbleType.isPillar()) {
////                return icons[marbleType.getMeta()][side];
////            }
//
//            // For non-pillar types, return the default icon for the specific meta
//            return icons[meta][side];
//        }
//        return null;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg)
    {
        for (MarbleBlockType type : MarbleBlockType.values()) {
            for (int i = 0; i < 6; i++) {
                icons[type.getMeta()][i] = reg.registerIcon("astralsorcery:marble_" + type.getName());
            }
        }
    }


    public static enum MarbleBlockType {

        RAW(0),
        BRICKS(1),
        PILLAR(2),
        ARCH(3),
        CHISELED(4),
        ENGRAVED(5),
        RUNED(6);

//        PILLAR_TOP(2),
//        PILLAR_BOTTOM(2);

        //BRICKS_MOSSY,
        //PILLAR_MOSSY,
        //CRACK_MOSSY;

        private final int meta;

        private MarbleBlockType(int meta) {
            this.meta = meta;
        }

        public ItemStack asStack() {
            return new ItemStack(BlocksAS.blockMarble, 1, meta);
        }

        public Block asBlock() {
            return BlocksAS.blockMarble;//.getStateFromMeta(meta);
        }

        public boolean isPillar() {
//            return this == PILLAR_BOTTOM || this == PILLAR || this == PILLAR_TOP;
            return this == PILLAR;
        }

//        public boolean obtainableInCreative() {
//            return this != PILLAR_TOP && this != PILLAR_BOTTOM;
//        }

        public int getMeta() {
            return meta;
        }

        public String getName() {
            return name().toLowerCase();
        }

        /*public boolean canTurnMossy() {
            return this == BRICKS || this == PILLAR || this == CRACKED;
        }

        public MarbleBlockType getMossyEquivalent() {
            if(!canTurnMossy()) return null;
            switch (this) {
                case BRICKS:
                    return BRICKS_MOSSY;
                case PILLAR:
                    return PILLAR_MOSSY;
                case CRACKED:
                    return CRACK_MOSSY;
            }
            return null;
        }*/
    }

}
