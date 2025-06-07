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
import hellfirepvp.astralsorcery.common.item.ItemCraftingComponent;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockCustomSandOre
 * Created by HellFirePvP
 * Date: 17.08.2016 / 13:07
 */
public class BlockCustomSandOre extends BlockFalling implements BlockCustomName, BlockVariants {

    private static final Random rand = new Random();

    private IIcon[] icons = new IIcon[OreType.values().length];
//    public static PropertyEnum<OreType> ORE_TYPE = PropertyEnum.create("oretype", OreType.class);

    public BlockCustomSandOre() {
        super(Material.sand);
        setBlockName("BlockCustomSandOre");
        setHardness(0.5F);
//        setSoundType(SoundType.SAND);
        setHarvestLevel("shovel", 1);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (OreType t : OreType.values()) {
            list.add(new ItemStack(item, 1, t.ordinal()));
        }
    }

//    @Override
//    public int getMeta(Block state) {
//        OreType type = state.getValue(ORE_TYPE);
//        return type == null ? 0 : type.getMeta();
//    }
//
//    @Override
//    public Block getStateFromMeta(int meta) {
//        return meta < OreType.values().length ? getDefaultState().withProperty(ORE_TYPE, OreType.values()[meta]) : getDefaultState();
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, ORE_TYPE);
//    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
//        OreType type = state.getValue(ORE_TYPE);
        List<ItemStack> drops = new ArrayList<>();
        switch (metadata) {
            case 0://AQUAMARINE
                int f = fortune + 3;
                int i = rand.nextInt(f * 2) - 1;
                if(i < 0) {
                    i = 0;
                }
                for (int j = 0; j < (i + 1); j++) {
                    drops.add(ItemCraftingComponent.MetaType.AQUAMARINE.asStack());
                }
                break;
        }
        return (ArrayList<ItemStack>) drops;
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
        OreType ot = OreType.values()[meta];
        return ot.getName();
    }

    @Override
    public List<Block> getValidStates() {
        List<Block> ret = new LinkedList<>();
//        for (OreType type : OreType.values()) {
//            ret.add(getDefaultState().withProperty(ORE_TYPE, type));
//        }
        return ret;
    }

    @Override
    public String getMetaName(int meta) {
        return meta + "";
    }

    @Override
    public int getMeta() {
        for (OreType type : OreType.values()) {
            return type.getMeta();
        }
        return 0;
    }
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return icons[meta];
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg)
    {
        for (OreType type : OreType.values()) {
            icons[type.getMeta()] = reg.registerIcon("astralsorcery:ore_" + type.getName());
        }
    }


    public static enum OreType  {

        AQUAMARINE(0);

        private final int meta;

        private OreType(int meta) {
            this.meta = meta;
        }

        public ItemStack asStack() {
            return new ItemStack(BlocksAS.customSandOre, 1, meta);
        }

        public int getMeta() {
            return meta;
        }

        public String getName() {
            return name().toLowerCase();
        }
    }

}
