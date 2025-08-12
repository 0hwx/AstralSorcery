/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.starlight.network.handlers;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.effect.EffectHandler;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.base.LightOreTransmutations;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightNetworkRegistry;
import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockTransmutationHandler
 * Created by HellFirePvP
 * Date: 30.01.2017 / 12:38
 */
public class BlockTransmutationHandler implements StarlightNetworkRegistry.IStarlightBlockHandler {

    private static Map<BlockPos, ActiveTransmutation> runningTransmutations = new HashMap<>();

    @Override
    public boolean isApplicable(World world, BlockPos pos, Block block) {
        return LightOreTransmutations.searchForTransmutation(block) != null;
    }

    @Override
    public void receiveStarlight(World world, Random rand, BlockPos pos, @Nullable IWeakConstellation starlightType,
        double amount) {
        long ms = System.currentTimeMillis();

        if (!runningTransmutations.containsKey(pos)) {
            Block tryStateIn = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            LightOreTransmutations.Transmutation tr = LightOreTransmutations.searchForTransmutation(tryStateIn);
            if (tr != null) {
                ActiveTransmutation atr = new ActiveTransmutation();
                runningTransmutations.put(pos, atr);
                atr.accCharge = 0D;
                atr.lastMSrec = ms;
                atr.runningTransmutation = tr;
            } else {
                return;
            }
        }
        ActiveTransmutation node = runningTransmutations.get(pos);
        long diff = ms - node.lastMSrec;
        if (diff >= 15_000) node.accCharge = 0;
        node.accCharge += amount;
        node.lastMSrec = ms;

        PktParticleEvent pkt = new PktParticleEvent(
            PktParticleEvent.ParticleEventType.TRANSMUTATION_CHARGE,
            pos.getX(),
            pos.getY(),
            pos.getZ());
        PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(world, pos, 16));

        if (node.accCharge >= node.runningTransmutation.cost) {
            runningTransmutations.remove(pos);

            world.setBlock(pos.getX(), pos.getY(), pos.getZ(), node.runningTransmutation.output);
        }

    }

    @SideOnly(Side.CLIENT)
    public static void playTransmutationEffects(PktParticleEvent event) {
        Random rand = EffectHandler.STATIC_EFFECT_RAND;

        EntityFXFacingParticle p = EffectHelper.genericFlareParticle(
            event.getVec()
                .getX() + rand.nextFloat(),
            event.getVec()
                .getY() + rand.nextFloat(),
            event.getVec()
                .getZ() + rand.nextFloat());
        p.motion(0, rand.nextFloat() * 0.05, 0);
        p.scale(0.2F)
            .setColor(Color.WHITE);
    }

    public static class ActiveTransmutation {

        private LightOreTransmutations.Transmutation runningTransmutation;
        private long lastMSrec;
        private double accCharge;

    }

}
