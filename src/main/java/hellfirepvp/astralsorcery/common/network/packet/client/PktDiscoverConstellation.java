/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.network.packet.client;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: PktDiscoverConstellation
 * Created by HellFirePvP
 * Date: 12.05.2016 / 13:50
 */
public class PktDiscoverConstellation implements IMessage, IMessageHandler<PktDiscoverConstellation, IMessage> {

    private String discoveredConstellation;

    public PktDiscoverConstellation() {
    }

    public PktDiscoverConstellation(String discoveredConstellation) {
        this.discoveredConstellation = discoveredConstellation;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        discoveredConstellation = ByteBufUtils.readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeString(buf, discoveredConstellation);
    }

    @Override
    public IMessage onMessage(PktDiscoverConstellation message, MessageContext ctx) {
        IConstellation received = ConstellationRegistry.getConstellationByName(message.discoveredConstellation);
        if (received == null) {
            AstralSorcery.log.info("Received unknown constellation from client: " + message.discoveredConstellation);
        } else {
            PlayerProgress prog = ResearchManager.getProgress(ctx.getServerHandler().playerEntity, Side.SERVER);
            if(prog != null && received.canDiscover(prog)) {
                ResearchManager.discoverConstellation(received, ctx.getServerHandler().playerEntity);
                ctx.getServerHandler().playerEntity.addChatMessage(
                        new ChatComponentTranslation("progress.discover.constellation.chat",
                                new ChatComponentTranslation(message.discoveredConstellation)
                                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)))
                                .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
            }
        }
        return null;
    }

}
