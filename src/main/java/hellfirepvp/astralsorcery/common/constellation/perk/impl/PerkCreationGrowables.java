/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.constellation.perk.impl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.relauncher.Side;
import hellfirepvp.astralsorcery.common.constellation.perk.ConstellationPerk;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.CropHelper;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: PerkCreationGrowables
 * Created by HellFirePvP
 * Date: 04.12.2016 / 15:29
 */
public class PerkCreationGrowables extends ConstellationPerk {

    private static int chanceToBonemeal = 6;

    public PerkCreationGrowables() {
        super("CRE_GROWTH", Target.PLAYER_TICK);
    }

    @Override
    public void onPlayerTick(EntityPlayer player, Side side) {
        if (side == Side.SERVER) {
            if (rand.nextInt(chanceToBonemeal) == 0) {
                BlockPos pos = new BlockPos(player).getPosition()
                    .add(rand.nextInt(4) - 2, rand.nextInt(4) - 2, rand.nextInt(4) - 2);
                World w = player.getEntityWorld();
                CropHelper.GrowablePlant plant = CropHelper.wrapPlant(w, pos);
                PktParticleEvent pkt = null;
                if (plant != null) {
                    if (plant.tryGrow(w, rand)) {
                        pkt = new PktParticleEvent(PktParticleEvent.ParticleEventType.CE_CROP_INTERACT, pos);
                        addAlignmentCharge(player, 0.4);
                    }
                } else {
                    Block at = w.getBlock(pos.getX(), pos.getY(), pos.getZ());
                    /*
                     * if(at.getBlock() instanceof IGrowable) {
                     * if(((IGrowable) at.getBlock()).canUseBonemeal(w, rand, pos, at)) {
                     * ((IGrowable) at.getBlock()).grow(w, rand, pos, at);
                     * pkt = new PktParticleEvent(PktParticleEvent.ParticleEventType.CE_CROP_INTERACT, pos);
                     * }
                     * } else
                     */ if (at instanceof BlockDirt) {// &&
                                                      // at.getValue(BlockDirt.VARIANT).equals(BlockDirt.DirtType.DIRT))
                                                      // {
                        w.setBlock(pos.getX(), pos.getY(), pos.getZ(), Blocks.grass);
                        pkt = new PktParticleEvent(PktParticleEvent.ParticleEventType.CE_CROP_INTERACT, pos);
                        addAlignmentCharge(player, 0.2);
                    }
                }
                if (pkt != null) {
                    PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(w, pos, 16));
                }
            }
        }
    }

    @Override
    public boolean hasConfigEntries() {
        return true;
    }

    @Override
    public void loadFromConfig(Configuration cfg) {
        chanceToBonemeal = cfg.getInt(
            getKey() + "ChanceForBonemeal",
            getConfigurationSection(),
            chanceToBonemeal,
            2,
            4000,
            "Sets the chance (Random.nextInt(chance) == 0) to try to see if a random plant near the player gets bonemeal'd");
    }

}
