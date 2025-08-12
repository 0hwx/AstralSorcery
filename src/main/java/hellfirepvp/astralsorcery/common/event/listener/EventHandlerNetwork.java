/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.event.listener;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.base.CelestialGatewaySystem;
import hellfirepvp.astralsorcery.common.base.Mods;
import hellfirepvp.astralsorcery.common.constellation.charge.PlayerChargeHandler;
import hellfirepvp.astralsorcery.common.constellation.perk.ConstellationPerkLevelManager;
import hellfirepvp.astralsorcery.common.data.DataWorldSkyHandlers;
import hellfirepvp.astralsorcery.common.data.SyncDataHolder;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.integrations.ModIntegrationCrafttweaker;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktSyncAlignmentLevels;
import hellfirepvp.astralsorcery.common.network.packet.server.PktSyncConfig;
import io.netty.buffer.Unpooled;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EventHandlerNetwork
 * Created by HellFirePvP
 * Date: 07.05.2016 / 01:10
 */
public class EventHandlerNetwork {

    private static final String AS_WORLDHANDLERS_PAYLOAD = "AS|WH";

    public static void init() {
        FMLCommonHandler.instance()
            .bus()
            .register(new EventHandlerNetwork());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayerMP p = (EntityPlayerMP) e.player;
        AstralSorcery.log.info("Synchronizing configuration to " + p.getDisplayName());
        PacketChannel.CHANNEL.sendTo(new PktSyncConfig(), p);
        PacketChannel.CHANNEL.sendTo(new PktSyncAlignmentLevels(ConstellationPerkLevelManager.levelsRequired), p);
        if (Mods.MINETWEAKER.isPresent()) {
            PacketChannel.CHANNEL.sendTo(ModIntegrationCrafttweaker.compileRecipeChangePacket(), p);
        }

        ResearchManager.sendInitClientKnowledge(p);

        CelestialGatewaySystem.instance.syncTo(p);
        SyncDataHolder.syncAllDataTo(p);
    }

    @SubscribeEvent
    public void onLoginEarly(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        List<Integer> dimensions = ((DataWorldSkyHandlers) SyncDataHolder
            .getDataServer(SyncDataHolder.DATA_SKY_HANDLERS)).getSkyHandlerDimensions();
        buf.writeInt(dimensions.size());
        for (int i : dimensions) {
            buf.writeInt(i);
        }
        event.manager.scheduleOutboundPacket(new S3FPacketCustomPayload(AS_WORLDHANDLERS_PAYLOAD, buf));
    }

    public static void clientCatchWorldHandlerPayload(S3FPacketCustomPayload pkt) {
        if (pkt.func_149169_c()
            .equals(AS_WORLDHANDLERS_PAYLOAD)) {
            byte[] buf = pkt.func_149168_d(); // todo check this
            int size = buf.length;
            List<Integer> dimensions = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                dimensions.add(buf.length);
            }

            ((DataWorldSkyHandlers) SyncDataHolder.getDataClient(SyncDataHolder.DATA_SKY_HANDLERS))
                .updateClient(dimensions);
        }
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent e) {
        EntityPlayer player = e.player;

        PlayerChargeHandler.instance.informDisconnect(player);
        // ResearchManager.logoutResetClient(player);
    }

}
