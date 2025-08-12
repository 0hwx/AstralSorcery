/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block.network;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.RenderingRegistry;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockAttunementAltar
 * Created by HellFirePvP
 * Date: 28.11.2016 / 10:20
 */
public class BlockAttunementAltar extends BlockContainer {

    public static final AxisAlignedBB boxAttunementAlar = AxisAlignedBB
        .getBoundingBox(-2D / 16D, 0, -2D / 16D, 18D / 16D, 6D / 16D, 18D / 16D);

    public BlockAttunementAltar() {
        super(Material.rock);
        setBlockName("BlockAttunementAltar");
        setHardness(3.0F);
        setStepSound(soundTypeStone);
        setResistance(25.0F);
        setLightLevel(0.8F);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        Block block = world.getBlock(x, y, z);
        BlockPos pos = new BlockPos(x, y, z);
        RenderingUtils.playBlockBreakParticles(pos, block);
        return true;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask,
        List<AxisAlignedBB> list, Entity collider) {
        this.setBlockBounds(
            (float) boxAttunementAlar.minX,
            (float) boxAttunementAlar.minY,
            (float) boxAttunementAlar.minZ,
            (float) boxAttunementAlar.maxX,
            (float) boxAttunementAlar.maxY,
            (float) boxAttunementAlar.maxZ);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        // this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public int getRenderType() {
        return RenderingRegistry.getNextAvailableRenderId();
    }

    // @Override
    // public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
    // return boxAttunementAlar;
    // }

    /*
     * @Override
     * public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
     * //worldIn.setBlockState(pos.up(),
     * BlocksAS.blockStructural.getDefaultState().withProperty(BlockStructural.BLOCK_TYPE,
     * BlockStructural.BlockType.ATTUNEMENT_ALTAR_STRUCT));
     * TileAttunementAltar te = MiscUtils.getTileAt(worldIn, pos, TileAttunementAltar.class, true);
     * if(te != null && !worldIn.isRemote) {
     * if(placer != null && placer instanceof EntityPlayer) {
     * te.setOwner(placer.getUniqueID());
     * }
     * }
     * super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
     * }
     */

    /*
     * @Override
     * public void neighborChanged(Block state, World world, BlockPos pos, Block neighbor) {
     * if(world.isAirBlock(pos.up())) {
     * world.setBlockToAir(pos);
     * }
     * super.neighborChanged(state, world, pos, neighbor);
     * }
     * @Override
     * public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
     * if(!(world instanceof World)) {
     * super.onNeighborChange(world, pos, neighbor);
     * return;
     * }
     * if(world.isAirBlock(pos.up())) {
     * ((World) world).setBlockToAir(pos);
     * }
     * }
     */

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return super.getPickBlock(target, world, x, y, z, player);
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
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean func_149730_j() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileAttunementAltar();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

}
