/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockCustomFlower
 * Created by HellFirePvP
 * Date: 28.03.2017 / 23:24
 */
public class BlockCustomFlower extends Block implements BlockCustomName, BlockVariants, IShearable {

    private IIcon[] icons = new IIcon[FlowerType.values().length];
    private static final AxisAlignedBB box = AxisAlignedBB.getBoundingBox(1.5D / 16D, 0, 1.5D / 16D, 14.5D / 16D, 13D / 16D, 14.5D / 16D);
    private static final Random rand = new Random();

    public BlockCustomFlower() {
        super(Material.plants);
        setLightLevel(0.2F);
//        setSoundType(SoundType.PLANT);
        setBlockName("BlockCustomFlower");
        setTickRandomly(true);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
        return Lists.newArrayList();
    }

    @Override
    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player) {
        if(!worldIn.isRemote && !player.capabilities.isCreativeMode) {
            switch (meta) {
                case 0: //GLOW_FLOWER:
                    int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, player.getHeldItem());
                    int looting = EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, player.getHeldItem());
                    if(looting > fortune) {
                        fortune = looting;
                    }
                    int size = 1;
                    for (int i = 0; i < fortune; i++) {
                        size += rand.nextInt(3) + 1;
                    }
                    for (int i = 0; i < size; i++) {
                        ItemUtils.dropItemNaturally(worldIn, x + 0.5, y + 0.1, z + 0.5, new ItemStack(Items.glowstone_dust));
                    }
                    break;
            }
        }
        super.onBlockHarvested(worldIn, x, y, z, meta, player);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return canBlockStay(worldIn, pos);
    }

    protected void checkAndDropBlock(World worldIn, int x, int y, int z){
        BlockPos pos = new BlockPos(x, y, z);
        int meta = worldIn.getBlockMetadata(x, y, z);
        if (!this.canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), meta, 0);
            worldIn.setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        this.checkAndDropBlock(world, x, y, z);
    }

    @Override
    public void updateTick(World worldIn, int x, int y, int z, Random random) {
        this.checkAndDropBlock(worldIn, x, y, z);
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        Block downState = worldIn.getBlock(pos.down().getX(), pos.down().getY(), pos.down().getZ());
        return downState.isSideSolid(worldIn, pos.getX(), pos.getY(), pos.getZ(), ForgeDirection.UP);
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        return false;
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return box;
    }

//    @Nullable
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(Block blockState, World worldIn, BlockPos pos) {
//        return NULL_AABB;
//    }
//
//    @Override
//    public BlockRenderLayer getBlockLayer() {
//        return BlockRenderLayer.CUTOUT;
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, FLOWER_TYPE);
//    }
//
//    @Override
//    public Block getStateFromMeta(int meta) {
//        return getDefaultState().withProperty(FLOWER_TYPE, FlowerType.values()[meta]);
//    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

//    @Override
//    public int getMeta(Block state) {
//        return state.getValue(FLOWER_TYPE).getMeta();
//    }

    @Override
    public String getIdentifierForMeta(int meta) {
        return FlowerType.values()[meta].getName();
    }

    @Override
    public List<Block> getValidStates() {
        List<Block> states = new LinkedList<>();
//        for (FlowerType type : FlowerType.values()) {
//            states.add(getDefaultState().withProperty(FLOWER_TYPE, type));
//        }
        return states;
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
        for (FlowerType type : FlowerType.values()) {
            icons[type.getMeta()] = reg.registerIcon("astralsorcery:" + type.getName());
        }
    }
    @Override
    public int getRenderType()
    {
        return 1;
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
        return Lists.newArrayList(ItemUtils.createBlockStack(world.getBlock(x, y, z)));
    }

    public static enum FlowerType {

        GLOW_FLOWER;

        public String getName() {
            return name().toLowerCase();
        }

        public int getMeta() {
            return ordinal();
        }

    }

}
