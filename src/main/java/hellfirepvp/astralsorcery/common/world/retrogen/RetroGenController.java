/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.world.retrogen;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.world.ChunkCoordIntPair;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: RetroGenController
 * Created by HellFirePvP
 * Date: 12.01.2017 / 21:18
 */
public class RetroGenController {

    private static List<ChunkCoordIntPair> retroGenActive = new LinkedList<>();

    // @SubscribeEvent
    // public void onChunkLoad(ChunkEvent.Load event) {
    // ChunkCoordIntPair pos = event.getChunk().getChunkCoordIntPair();
    // if(event.world.isRemote || !event.getChunk().isTerrainPopulated || retroGenActive.contains(pos)) return;
    //
    // Integer chunkVersion = -1;
    // if(((AnvilChunkLoader) ((WorldServer) event.world).getChunkProvider().chunkLoader).chunkExists(event.world,
    // pos.chunkXPos, pos.chunkZPos)) {
    // chunkVersion = ChunkVersionController.instance.getGenerationVersion(pos);
    // if(chunkVersion == null) {
    // AstralSorcery.log.info("[AstralSorcery] No ChunkVersion found for Chunk: " + pos.toString() + " - Skipping
    // RetroGen...");
    // return;
    // }
    // }
    // AstralSorcery.log.info("[AstralSorcery] Attempting AstralSorcery retrogen for chunk "+ pos.toString() +
    // " - Version " + chunkVersion + " -> " + AstralWorldGenerator.CURRENT_WORLD_GENERATOR_VERSION +
    // " - Stack: " + retroGenActive.size());
    // retroGenActive.add(pos);
    // CommonProxy.worldGenerator.handleRetroGen(event.world, pos, chunkVersion);
    // retroGenActive.remove(pos);
    // }

}
