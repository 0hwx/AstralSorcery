/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block.network;

import com.google.common.collect.Lists;
import cpw.mods.fml.client.registry.RenderingRegistry;
import hellfirepvp.astralsorcery.common.block.BlockVariants;
import hellfirepvp.astralsorcery.common.item.crystal.CrystalProperties;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.network.TileCrystalLens;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockLens
 * Created by HellFirePvP
 * Date: 07.08.2016 / 22:31
 */
public class BlockLens extends BlockStarlightNetwork implements BlockVariants {

    private static final AxisAlignedBB boxLens =  AxisAlignedBB.getBoundingBox(2.5D/16D, 0, 2.5D/16D, 13.5D/16D, 14.5D/16D, 13.5D/16D);

//    public static PropertyBool RENDER_FULLY = PropertyBool.create("render");

    public BlockLens() {
        super("BlockLens", Material.rock);
        setHardness(3.0F);
//        setSoundType(SoundType.GLASS);
        setResistance(12.0F);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
//        setDefaultState(this.blockState.getBaseState().withProperty(RENDER_FULLY, true));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        ItemStack stack = new ItemStack(itemIn);
        CrystalProperties.applyCrystalProperties(stack, CrystalProperties.getMaxCelestialProperties());
        list.add(stack);
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
//        CrystalProperties.addPropertyTooltip(CrystalProperties.getCrystalProperties(stack), tooltip, CrystalProperties.MAX_SIZE_CELESTIAL);
//    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return boxLens;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() { return false; }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

//    @Override
//    public int getMeta() {
//        return 0;
//    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileCrystalLens();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, RENDER_FULLY);
//    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return side == ForgeDirection.DOWN;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
        return Lists.newArrayList();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        ItemStack stack = super.getPickBlock(target, world, x, y, z, player);
        BlockPos pos = new BlockPos(x, y, z);
        TileCrystalLens lens = MiscUtils.getTileAt(world, pos, TileCrystalLens.class, true);
        if(lens != null && lens.getCrystalProperties() != null) {
            CrystalProperties.applyCrystalProperties(stack, lens.getCrystalProperties());
        } else {
            CrystalProperties.applyCrystalProperties(stack, CrystalProperties.getMaxCelestialProperties());
        }
        return stack;
    }

    @Override
    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player) {
        BlockPos pos = new BlockPos(x, y, z);
        TileCrystalLens lens = MiscUtils.getTileAt(worldIn, pos, TileCrystalLens.class, true);
        if(lens != null && !worldIn.isRemote && !player.capabilities.isCreativeMode) {
            ItemStack drop;
            if(lens.getLensColor() != null) {
                drop = lens.getLensColor().asStack();
                ItemUtils.dropItemNaturally(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
            }

            drop = new ItemStack(BlocksAS.lens);
            CrystalProperties.applyCrystalProperties(drop, lens.getCrystalProperties());
            ItemUtils.dropItemNaturally(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
        }

        super.onBlockHarvested(worldIn, pos.getX(), pos.getY(), pos.getZ(),meta, player);
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote && player.isSneaking()) {
            BlockPos pos = new BlockPos(x, y, z);
            TileCrystalLens lens = MiscUtils.getTileAt(worldIn, pos, TileCrystalLens.class, true);
            if(lens != null && lens.getLensColor() != null) {
                ItemStack drop = lens.getLensColor().asStack();
                ItemUtils.dropItemNaturally(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
//                SoundHelper.playSoundAround(Sounds.clipSwitch, worldIn, pos, 0.8F, 1.5F);
                lens.setLensColor(null);
                return true;
            }
        }
        return false;
    }

//    @Override
//    public Block getActualState(Block state, IBlockAccess worldIn, BlockPos pos) {
//        return state.withProperty(RENDER_FULLY, false);
//    }

    @Override
    public int getRenderType() {
        return RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        BlockPos pos = new BlockPos(x, y, z);
        TileCrystalLens te = MiscUtils.getTileAt(worldIn, pos, TileCrystalLens.class, true);
        if(te == null) return;
        te.onPlace(CrystalProperties.getCrystalProperties(stack));
    }

    @Override
    public List<Block> getValidStates() {
        return Arrays.asList();
//        return Arrays.asList(getDefaultState().withProperty(RENDER_FULLY, false), getDefaultState().withProperty(RENDER_FULLY, true));
    }

    @Override
    public String getMetaName(int meta) {
        return "";
//        return state.getValue(RENDER_FULLY).toString();
    }

    @Override
    public int getMeta() {
        return 0;
    }

}
