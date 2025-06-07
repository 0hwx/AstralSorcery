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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockBlackMarble
 * Created by HellFirePvP
 * Date: 21.10.2016 / 23:02
 */
public class BlockBlackMarble extends Block implements BlockCustomName, BlockVariants {

//    public static PropertyEnum<BlackMarbleBlockType> BLACK_MARBLE_TYPE = PropertyEnum.create("marbletype", BlackMarbleBlockType.class);
    private IIcon[] icons = new IIcon[BlackMarbleBlockType.values().length];

    public BlockBlackMarble() {
        super(Material.rock);
        setBlockName("BlockBlackMarble");
        setHardness(1.0F);
        setHarvestLevel("pickaxe", 1);
        setResistance(3.0F);
setStepSound(soundTypeStone);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
//        setDefaultState(this.blockState.getBaseState().withProperty(BLACK_MARBLE_TYPE, BlackMarbleBlockType.RAW));
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (BlackMarbleBlockType t : BlackMarbleBlockType.values()) {
            list.add(new ItemStack(item, 1, t.ordinal()));
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public boolean isNormalCube() {
        return true;
    }

    @Override
    public boolean func_149730_j() {
        return true;
    }

//    @Override
//    public boolean isFullyOpaque() {
//        return true;
//    }

    @Override
    public String getIdentifierForMeta(int meta) {
        return BlackMarbleBlockType.values()[meta].getName();
    }

//    @Override
//    public int getMeta(Block state) {
//        BlackMarbleBlockType type = state.getValue(BLACK_MARBLE_TYPE);
//        return type == null ? 0 : type.getMeta();
//    }
//
//    @Override
//    public Block getStateFromMeta(int meta) {
//        return meta < BlackMarbleBlockType.values().length ? getDefaultState().withProperty(BLACK_MARBLE_TYPE, BlackMarbleBlockType.values()[meta]) : getDefaultState();
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, BLACK_MARBLE_TYPE);
//    }

    @Override
    public List<Block> getValidStates() {
        List<Block> ret = new LinkedList<>();
//        for (BlackMarbleBlockType type : BlackMarbleBlockType.values()) {
//            ret.add(getDefaultState().withProperty(BLACK_MARBLE_TYPE, type));
//        }
        return ret;
    }

    @Override
    public String getMetaName(int meta) {
        return "state.getUnlocalizedName()";
    }

    @Override
    public int getMeta() {
        return 0;
    }


    public IIcon getIcon(int side, int meta)
    {
        return icons[meta];
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        for (BlackMarbleBlockType type : BlackMarbleBlockType.values()) {
            icons[type.getMeta()] = reg.registerIcon("astralsorcery:black_marble_" + type.getName());
        }
    }

    public static enum BlackMarbleBlockType {

        RAW(0);

        private final int meta;

        private BlackMarbleBlockType(int meta) {
            this.meta = meta;
        }

        public ItemStack asStack() {
            return new ItemStack(BlocksAS.blockBlackMarble, 1, meta);
        }

        public Block asBlock() {
            return BlocksAS.blockBlackMarble;//.getStateFromMeta(meta);
        }

        public int getMeta() {
            return meta;
        }

        public String getName() {
            return name().toLowerCase();
        }

    }

}
