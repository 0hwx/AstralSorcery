/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.network;

import net.minecraft.world.World;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.network.packet.client.PktAttuneConstellation;
import hellfirepvp.astralsorcery.common.network.packet.client.PktBurnParchment;
import hellfirepvp.astralsorcery.common.network.packet.client.PktDiscoverConstellation;
import hellfirepvp.astralsorcery.common.network.packet.client.PktEngraveGlass;
import hellfirepvp.astralsorcery.common.network.packet.client.PktRequestSeed;
import hellfirepvp.astralsorcery.common.network.packet.client.PktRequestTeleport;
import hellfirepvp.astralsorcery.common.network.packet.client.PktRotateTelescope;
import hellfirepvp.astralsorcery.common.network.packet.client.PktUnlockPerk;
import hellfirepvp.astralsorcery.common.network.packet.server.PktAttunementAltarState;
import hellfirepvp.astralsorcery.common.network.packet.server.PktCraftingTableFix;
import hellfirepvp.astralsorcery.common.network.packet.server.PktDualParticleEvent;
import hellfirepvp.astralsorcery.common.network.packet.server.PktLightningEffect;
import hellfirepvp.astralsorcery.common.network.packet.server.PktOreScan;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.network.packet.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.network.packet.server.PktProgressionUpdate;
import hellfirepvp.astralsorcery.common.network.packet.server.PktSyncAlignmentLevels;
import hellfirepvp.astralsorcery.common.network.packet.server.PktSyncCharge;
import hellfirepvp.astralsorcery.common.network.packet.server.PktSyncConfig;
import hellfirepvp.astralsorcery.common.network.packet.server.PktSyncData;
import hellfirepvp.astralsorcery.common.network.packet.server.PktSyncKnowledge;
import hellfirepvp.astralsorcery.common.network.packet.server.PktSyncMinetweakerChanges;
import hellfirepvp.astralsorcery.common.network.packet.server.PktSyncStepAssist;
import hellfirepvp.astralsorcery.common.network.packet.server.PktUpdateGateways;
import hellfirepvp.astralsorcery.common.network.packet.server.PktUpdateReach;
import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: PacketChannel
 * Created by HellFirePvP
 * Date: 07.05.2016 / 01:11
 */
public class PacketChannel {

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(AstralSorcery.NAME);

    public static void init() {
        int id = 0;

        // (server -> client)
        CHANNEL.registerMessage(PktSyncConfig.class, PktSyncConfig.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktSyncKnowledge.class, PktSyncKnowledge.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktSyncData.class, PktSyncData.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktParticleEvent.class, PktParticleEvent.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktCraftingTableFix.class, PktCraftingTableFix.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktProgressionUpdate.class, PktProgressionUpdate.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktPlayEffect.class, PktPlayEffect.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktRequestSeed.class, PktRequestSeed.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktUpdateReach.class, PktUpdateReach.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktUnlockPerk.class, PktUnlockPerk.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktSyncAlignmentLevels.class, PktSyncAlignmentLevels.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktAttunementAltarState.class, PktAttunementAltarState.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktRotateTelescope.class, PktRotateTelescope.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktLightningEffect.class, PktLightningEffect.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktSyncMinetweakerChanges.class, PktSyncMinetweakerChanges.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(
            PktSyncMinetweakerChanges.Compound.class,
            PktSyncMinetweakerChanges.Compound.class,
            id++,
            Side.CLIENT);
        CHANNEL.registerMessage(PktDualParticleEvent.class, PktDualParticleEvent.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktOreScan.class, PktOreScan.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktSyncCharge.class, PktSyncCharge.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktSyncStepAssist.class, PktSyncStepAssist.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktUpdateGateways.class, PktUpdateGateways.class, id++, Side.CLIENT);
        CHANNEL.registerMessage(PktBurnParchment.class, PktBurnParchment.class, id++, Side.CLIENT);

        // (client -> server)
        CHANNEL.registerMessage(PktDiscoverConstellation.class, PktDiscoverConstellation.class, id++, Side.SERVER);
        CHANNEL.registerMessage(PktRequestSeed.class, PktRequestSeed.class, id++, Side.SERVER);
        CHANNEL.registerMessage(PktUnlockPerk.class, PktUnlockPerk.class, id++, Side.SERVER);
        CHANNEL.registerMessage(PktAttunementAltarState.class, PktAttunementAltarState.class, id++, Side.SERVER);
        CHANNEL.registerMessage(PktAttuneConstellation.class, PktAttuneConstellation.class, id++, Side.SERVER);
        CHANNEL.registerMessage(PktRotateTelescope.class, PktRotateTelescope.class, id++, Side.SERVER);
        CHANNEL.registerMessage(PktRequestTeleport.class, PktRequestTeleport.class, id++, Side.SERVER);
        CHANNEL.registerMessage(PktBurnParchment.class, PktBurnParchment.class, id++, Side.SERVER);
        CHANNEL.registerMessage(PktEngraveGlass.class, PktEngraveGlass.class, id++, Side.SERVER);

        /*
         * Method registerPacket = ReflectionHelper.findMethod(
         * EnumConnectionState.class,
         * EnumConnectionState.PLAY,
         * new String[] { "registerPacket", "func_179245_a", "a" },
         * EnumPacketDirection.class, Class.class);
         * registerPacket.setAccessible(true);
         * try {
         * registerPacket.invoke(EnumConnectionState.HANDSHAKING, EnumPacketDirection.CLIENTBOUND,
         * PktWorldHandlerSyncEarly.class);
         * } catch (Exception e) {}
         */
    }

    public static NetworkRegistry.TargetPoint pointFromPos(World world, BlockPos pos, double range) {
        return new NetworkRegistry.TargetPoint(world.provider.dimensionId, pos.getX(), pos.getY(), pos.getZ(), range);
    }

}
