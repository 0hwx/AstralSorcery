/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.util.EnumDyeColor;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockFlareLight
 * Created by HellFirePvP
 * Date: 22.10.2016 / 14:36
 */
public class BlockFlareLight extends Block {

//    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);
public static final AxisAlignedBB FULL_BLOCK_AABB = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockFlareLight() {
        super(RegistryItems.materialTransparentReplaceable);
        setLightLevel(1F);
        setBlockUnbreakable();
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
        setBlockName("BlockFlareLight");
//        setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.YELLOW));
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {}

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return FULL_BLOCK_AABB;
    }

//    @Override
//    public boolean causesSuffocation() {
//        return false;
//    }

    @Override
    public boolean isAir(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
        return Lists.newArrayList();
    }

//    @Override
//    public int getMeta(Block state) {
//        return state.getValue(COLOR).getMetadata();
//    }
//
//    @Override
//    public Block getStateFromMeta(int meta) {
//        return getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
//    }

//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, COLOR);
//    }

    @Override
    public int getRenderType() {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, int x, int y, int z, Random rand) {
        BlockPos pos = new BlockPos(x, y, z);
        int meta = worldIn.getBlockMetadata(x, y, z);
        EntityFXFacingParticle p = EffectHelper.genericFlareParticle(pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5).gravity(0.004);
        p.offset(rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1));
        p.scale(0.4F + rand.nextFloat() * 0.1F).setAlphaMultiplier(0.75F);
        p.motion(0, rand.nextFloat() * 0.02F, 0).setMaxAge(50 + rand.nextInt(20));
        p.setColor(MiscUtils.flareColorFromDye(EnumDyeColor.byDyeDamage(meta)));
        p = EffectHelper.genericFlareParticle(pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5).gravity(0.004);
        p.offset(rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1));
        p.scale(0.4F + rand.nextFloat() * 0.1F).setAlphaMultiplier(0.75F);
        p.motion(0, rand.nextFloat() * 0.02F, 0).setMaxAge(50 + rand.nextInt(20));
        p.setColor(MiscUtils.flareColorFromDye(EnumDyeColor.byDyeDamage(meta)));
        if(rand.nextBoolean()) {
            p = EffectHelper.genericFlareParticle(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5).gravity(0.004);
            p.offset(rand.nextFloat() * 0.02 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1), rand.nextFloat() * 0.02 * (rand.nextBoolean() ? 1 : -1));
            p.scale(0.1F + rand.nextFloat() * 0.05F).setColor(Color.WHITE).setMaxAge(25);
        }
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public boolean isTranslucent(Block state) {
//        return true;
//    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

//    @Override
//    public boolean isNormalCube(Block state) {
//        return false;
//    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean func_149730_j() {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List<net.minecraft.util.AxisAlignedBB> list, Entity collider) {}

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean canCollideCheck(int meta, boolean includeLiquid) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public int getMobilityFlag() {
        return 2;
    }

    public MapColor getMapColor(int meta)
    {
        return MapColor.quartzColor;
    }
}
