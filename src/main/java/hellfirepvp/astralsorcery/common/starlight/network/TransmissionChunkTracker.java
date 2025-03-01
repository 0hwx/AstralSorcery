/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.starlight.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;


/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TransmissionChunkTracker
 * Created by HellFirePvP
 * Date: 05.08.2016 / 10:08
 */
public class TransmissionChunkTracker {

    private static final TransmissionChunkTracker instance = new TransmissionChunkTracker();

    private TransmissionChunkTracker() {}

    public static TransmissionChunkTracker getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onChLoad(ChunkEvent.Load event) {
        TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(event.world);
        if(handle != null) {
            Chunk ch = event.getChunk();
            handle.informChunkLoad(new ChunkCoordIntPair(ch.xPosition, ch.zPosition));
        }
    }

    @SubscribeEvent
    public void onChUnload(ChunkEvent.Unload event) {
        TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(event.world);
        if(handle != null) {
            Chunk ch = event.getChunk();
            handle.informChunkUnload(new ChunkCoordIntPair(ch.xPosition, ch.zPosition));
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        StarlightTransmissionHandler.getInstance().informWorldUnload(event.world);
        StarlightUpdateHandler.getInstance().informWorldUnload(event.world);
    }

}
