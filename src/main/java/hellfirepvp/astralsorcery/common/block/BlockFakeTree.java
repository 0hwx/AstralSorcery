/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import java.awt.*;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.tile.TileFakeTree;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockFakeTree
 * Created by HellFirePvP
 * Date: 11.11.2016 / 20:31
 */
public class BlockFakeTree extends BlockContainer {

    public BlockFakeTree() {
        // Material.BARRIER -> (new Material(MapColor.airColor).setRequiresTool().setImmovableMobility());
        super(Material.air);
        setBlockName("BlockFakeTree");
        setBlockUnbreakable();
        setResistance(6000001.0F);
        setLightLevel(0.6F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        BlockPos pos = new BlockPos(x, y, z);
        TileFakeTree tft = MiscUtils.getTileAt(world, pos, TileFakeTree.class, false);
        if (tft != null && tft.getFakedState() != null) {
            RenderingUtils.playBlockBreakParticles(pos, tft.getFakedState());
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, int x, int y, int z, Random rand) {
        BlockPos pos = new BlockPos(x, y, z);
        TileFakeTree tft = MiscUtils.getTileAt(worldIn, pos, TileFakeTree.class, false);
        if (tft != null && tft.getReference() == null) return;
        if (rand.nextInt(20) == 0) {
            EntityFXFacingParticle p = EffectHelper.genericFlareParticle(
                pos.getX() + rand.nextFloat(),
                pos.getY() + rand.nextFloat(),
                pos.getZ() + rand.nextFloat());
            p.motion(0, 0, 0);
            p.scale(0.45F)
                .setColor(new Color(63, 255, 63))
                .setMaxAge(65);
        }
    }

    // @Override
    // public SoundType getSoundType(Block state, World world, BlockPos pos, @Nullable Entity entity) {
    // TileFakeTree tft = MiscUtils.getTileAt(world, pos, TileFakeTree.class, true);
    // if(tft != null && tft.getFakedState() != null) {
    // Block fake = tft.getFakedState();
    // return fake.getBlock().getSoundType(fake, world, pos, entity);
    // }
    // return super.getSoundType(state, world, pos, entity);
    // }

    @Override
    public int getRenderType() {
        return 1;
    }

    // @Override
    // public BlockRenderLayer getBlockLayer() {
    // return BlockRenderLayer.TRANSLUCENT;
    // }
    //
    // @Override
    // public boolean isFullyOpaque() {
    // return false;
    // }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    // @Override
    // public boolean isTranslucent() {
    // return true;
    // }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
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
        return new TileFakeTree();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        BlockPos pos = new BlockPos(x, y, z);
        TileFakeTree tft = MiscUtils.getTileAt(world, pos, TileFakeTree.class, true);
        try {
            if (tft != null && tft.getFakedState() != null) {
                return tft.getFakedState()
                    .getPickBlock(target, world, pos.getX(), pos.getY(), pos.getZ(), player);
            }
        } catch (Exception ignored) {}
        return null;
    }

}
