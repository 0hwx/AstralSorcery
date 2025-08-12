package hellfirepvp.astralsorcery.common.network.packet.server;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import hellfirepvp.astralsorcery.common.item.tool.ItemChargedCrystalPickaxe;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: PktOreScan
 * Created by HellFirePvP
 * Date: 12.03.2017 / 14:57
 */
public class PktOreScan implements IMessage, IMessageHandler<PktOreScan, IMessage> {

    private Collection<BlockPos> positions = Lists.newArrayList();
    private boolean tumble = false;

    public PktOreScan() {}

    public PktOreScan(Collection<BlockPos> positions, boolean doTumble) {
        this.positions = positions;
        this.tumble = doTumble;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tumble = buf.readBoolean();
        int size = buf.readInt();
        positions = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            positions.add(ByteBufUtils.readPos(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(tumble);
        buf.writeInt(positions.size());
        for (BlockPos pos : positions) {
            ByteBufUtils.writePos(buf, pos);
        }
    }

    @Override
    public IMessage onMessage(PktOreScan message, MessageContext ctx) {
        ItemChargedCrystalPickaxe.playClientEffects(message.positions, message.tumble);
        return null;
    }

}
