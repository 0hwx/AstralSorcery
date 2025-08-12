/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.tile;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.entities.EntityFlare;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.tile.base.TileSkybound;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.BlockStateCheck;
import hellfirepvp.astralsorcery.common.util.data.DirectionalLayerBlockDiscoverer;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TileIlluminator
 * Created by HellFirePvP
 * Date: 01.11.2016 / 16:01
 */
public class TileIlluminator extends TileSkybound {

    private static final Random rand = new Random();
    public static final LightCheck illuminatorCheck = new LightCheck();

    public static final int SEARCH_RADIUS = 64;
    public static final int STEP_WIDTH = 4;

    private LinkedList<BlockPos>[] validPositions = null;
    private boolean recalcRequested = false;
    private int ticksUntilNext = 180;
    private boolean playerPlaced = false;

    @Override
    public void tick() {
        super.tick();

        if (!playerPlaced) return;

        if (!worldObj.isRemote && doesSeeSky()) {
            if (validPositions == null) recalculate();
            if (rand.nextInt(3) == 0 && placeFlares()) {
                recalcRequested = true;
            }
            ticksUntilNext--;
            if (ticksUntilNext <= 0) {
                ticksUntilNext = 180;
                if (recalcRequested) {
                    recalcRequested = false;
                    recalculate();
                }
            }
        }
        if (worldObj.isRemote) {
            playEffects();
        }
    }

    public void setPlayerPlaced() {
        playerPlaced = true;
        markForUpdate();
    }

    @SideOnly(Side.CLIENT)
    private void playEffects() {
        if (Minecraft.isFancyGraphicsEnabled() || rand.nextInt(5) == 0) {
            EntityFXFacingParticle p = EffectHelper.genericFlareParticle(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
            p.motion(
                (rand.nextFloat() * 0.025F) * (rand.nextBoolean() ? 1 : -1),
                (rand.nextFloat() * 0.025F) * (rand.nextBoolean() ? 1 : -1),
                (rand.nextFloat() * 0.025F) * (rand.nextBoolean() ? 1 : -1));
            p.scale(0.25F);
            switch (rand.nextInt(3)) {
                case 0:
                    p.setColor(Color.WHITE);
                    break;
                case 1:
                    p.setColor(new Color(0xFEFF9E));
                    break;
                case 2:
                    p.setColor(new Color(0xFFE539));
                    break;
            }
        }
    }

    private boolean placeFlares() {
        boolean needsRecalc = false;
        for (LinkedList<BlockPos> list : validPositions) {
            if (list.isEmpty()) {
                needsRecalc = true;
                continue;
            }
            int index = rand.nextInt(list.size());
            BlockPos at = list.remove(index);
            if (!needsRecalc && list.isEmpty()) needsRecalc = true;
            at = at.add(rand.nextInt(5) - 2, rand.nextInt(13) - 6, rand.nextInt(5) - 2);
            if (illuminatorCheck.isStateValid(worldObj, at, worldObj.getBlock(at.getX(), at.getY(), at.getZ()))) {
                worldObj.setBlock(at.getX(), at.getY(), at.getZ(), BlocksAS.blockVolatileLight);
                if (rand.nextInt(4) == 0) {
                    EntityFlare.spawnAmbient(
                        worldObj,
                        new Vector3(this).add(-1 + rand.nextFloat() * 3, 0.6, -1 + rand.nextFloat() * 3));
                }
            }
        }
        return needsRecalc;
    }

    private void recalculate() {
        int parts = yPartsFromHeight();
        validPositions = new LinkedList[parts];
        for (int i = 1; i <= parts; i++) {
            int yLevel = (int) (((float) yCoord) * (((float) i) / ((float) parts)));
            LinkedList<BlockPos> calcPositions = new DirectionalLayerBlockDiscoverer(
                worldObj,
                new BlockPos(xCoord, yLevel, zCoord),
                SEARCH_RADIUS,
                STEP_WIDTH).discoverApplicableBlocks();
            validPositions[i - 1] = repeatList(calcPositions);
        }
    }

    private int yPartsFromHeight() {
        return Math.max(2, yCoord / 8);
    }

    private LinkedList<BlockPos> repeatList(LinkedList<BlockPos> list) {
        LinkedList<BlockPos> rep = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            rep.addAll(list);
        }
        return rep;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setBoolean("playerPlaced", this.playerPlaced);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.playerPlaced = compound.getBoolean("playerPlaced");
    }

    @Override
    protected void onFirstTick() {
        recalculate();
    }

    public static class LightCheck implements BlockStateCheck {

        @Override
        public boolean isStateValid(World world, BlockPos pos, Block state) {
            return world.isAirBlock(pos.getX(), pos.getY(), pos.getZ())
                && !world.canBlockSeeTheSky(pos.getX(), pos.getY(), pos.getZ())
                && world.getFullBlockLightValue(pos.getX(), pos.getY(), pos.getZ()) < 8
                && world.getSavedLightValue(EnumSkyBlock.Sky, pos.getX(), pos.getY(), pos.getZ()) < 6;
        }

    }

}
