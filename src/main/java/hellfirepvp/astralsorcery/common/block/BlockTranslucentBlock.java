/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.tile.TileTranslucent;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockTranslucentBlock
 * Created by HellFirePvP
 * Date: 17.01.2017 / 03:44
 */
public class BlockTranslucentBlock extends BlockContainer {

    public BlockTranslucentBlock() {
        super(Material.air); // Material.BARRIER -> (new
                             // Material(MapColor.airColor).setRequiresTool().setImmovableMobility());
        setBlockName("BlockTranslucentBlock");
        setBlockUnbreakable();
        setResistance(6000001.0F);
        setLightLevel(0.6F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        BlockPos pos = new BlockPos(x, y, z);
        Block fst = getFakedStateTile(world, pos);
        if (fst != null) {
            RenderingUtils.playBlockBreakParticles(pos, fst);
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, int x, int y, int z, Random rand) {
        if (rand.nextInt(30) == 0) {
            EntityFXFacingParticle p = EffectHelper
                .genericFlareParticle(x + rand.nextFloat(), y + rand.nextFloat(), z + rand.nextFloat());
            p.motion(0, 0, 0);
            p.scale(0.45F)
                .setColor(Color.WHITE)
                .setMaxAge(65);
        }
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    // @Override
    // public SoundType getSoundType(Block state, World world, BlockPos pos, @Nullable Entity entity) {
    // Block fst = getFakedStateTile(world, pos);
    // if(fst != null) {
    // return fst.getBlock().getSoundType(fst, world, pos, entity);
    // }
    // return super.getSoundType(state, world, pos, entity);
    // }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        BlockPos pos = new BlockPos(x, y, z);
        Block fst = getFakedStateTile(worldIn, pos);
        if (fst != null) {
            try {
                return fst
                    .onBlockActivated(worldIn, pos.getX(), pos.getY(), pos.getZ(), player, side, hitX, hitY, hitZ);
            } catch (Exception exc) {}
        }
        return super.onBlockActivated(worldIn, pos.getX(), pos.getY(), pos.getZ(), player, side, hitX, hitY, hitZ);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return Lists.newArrayList();
    }

    private Block getFakedStateTile(World world, BlockPos pos) {
        TileTranslucent tt = MiscUtils.getTileAt(world, pos, TileTranslucent.class, true);
        if (tt == null) return null;
        return tt.getFakedState();
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    // @Override
    // public BlockRenderLayer getBlockLayer() {
    // return BlockRenderLayer.TRANSLUCENT;
    // }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    // @Override
    // public boolean isTranslucent() {
    // return true;
    // }
    //
    // @Override
    // public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, ForgeDirection side) {
    // return false;
    // }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    // @Override
    // public boolean isFullyOpaque() {
    // return false;
    // }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        BlockPos pos = new BlockPos(x, y, z);
        Block fst = getFakedStateTile(world, pos);
        try {
            if (fst != null) {
                return fst.getPickBlock(target, world, pos.getX(), pos.getY(), pos.getZ(), player);
            }
        } catch (Exception ignored) {}
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileTranslucent();
    }
}
