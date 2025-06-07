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
    private IIcon[] icons = new IIcon[MarbleBlockType.values().length];
    private static IIcon[] iconPillar = new IIcon[6];
    private static IIcon[] iconPillarTop = new IIcon[6];
    private static IIcon[] iconPillarbottom = new IIcon[6];

    public BlockMarble() {
        super(Material.rock);
        setBlockName("BlockMarble");
        setHardness(1.0F);
        setHarvestLevel("pickaxe", 1);
        setResistance(3.0F);
        setStepSound(soundTypeStone);
//        setTickRandomly(true);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
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

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean func_149730_j() { //isFullBlock
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

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        int meta = worldIn.getBlockMetadata(x, y, z);
        MarbleBlockType marbleType = MarbleBlockType.values()[meta];

        if (marbleType == MarbleBlockType.PILLAR) {
            // Get metadata for the blocks above and below
            Block blockAbove = worldIn.getBlock(x, y + 1, z);
            Block blockBelow = worldIn.getBlock(x, y - 1, z);
            int metaAbove = worldIn.getBlockMetadata(x, y + 1, z);
            int metaBelow = worldIn.getBlockMetadata(x, y - 1, z);
            int PILLAR_META = MarbleBlockType.PILLAR.getMeta();

            // Only process this if the current block is a PILLAR, and above or below is also a PILLAR
            if (blockAbove instanceof BlockMarble && blockBelow instanceof BlockMarble && metaAbove == metaBelow){
                return iconPillar[side];
            }
            else if (blockBelow instanceof BlockMarble && metaBelow == PILLAR_META) {
                return iconPillarTop[side];  // Use top texture if below is a pillar
            }
            else if (blockAbove instanceof BlockMarble && metaAbove == PILLAR_META) {
                return iconPillarbottom[side];  // Use bottom texture if above is a pillar
            }
        }

        return this.getIcon(side, meta);  // Default icon for non-pillar blocks
    }





    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        MarbleBlockType marbleType = MarbleBlockType.values()[meta];
        if (marbleType == MarbleBlockType.PILLAR) {
                return iconPillar[side];
        } else  {
            // For non-pillar types, just return the default icon based on meta
            return icons[meta % icons.length];
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg)
    {
        // Register all textures for MarbleBlockType values
        for (MarbleBlockType type : MarbleBlockType.values()) {
            // Loop through all six sides of the block
            if (type == MarbleBlockType.PILLAR) {
                for (int i = 0; i < 6; i++) {
                    type.Pillar(i, reg);
                }
            } else {
                icons[type.getMeta()] = reg.registerIcon("astralsorcery:marble_" + type.getName());
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
//        PILLAR_BOTTOM(2),;

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

        public int getMeta() {
            return meta;
        }

        public String getName() {
            return name().toLowerCase();
        }

        public void Pillar(int i, IIconRegister reg) {
            if (i == 0 || i == 1) {
                // Bottom or top side texture
                iconPillar[i] = iconPillarTop[i] = iconPillarbottom[i] = reg.registerIcon("astralsorcery:marble_pillar_updown");
            } else {
                iconPillar[i] = reg.registerIcon("astralsorcery:marble_pillar");
                iconPillarTop[i] = reg.registerIcon("astralsorcery:marble_pillar_side_top");
                iconPillarbottom[i] = reg.registerIcon("astralsorcery:marble_pillar_side_bottom");

            }
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
