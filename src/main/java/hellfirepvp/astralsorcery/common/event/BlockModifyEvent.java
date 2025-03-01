/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.event;

import cpw.mods.fml.common.eventhandler.Event;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockModifyEvent
 * Created by HellFirePvP
 * Date: 04.08.2016 / 10:49
 */
public class BlockModifyEvent extends Event {

    private final Chunk chunk;
    private final World world;
    private final BlockPos at;
    private final Block oldState, newState;

    public BlockModifyEvent(Chunk chunk, BlockPos at, Block oldState, Block newState) {
        this.at = at;
        this.chunk = chunk;
        this.world = chunk.worldObj;
        this.oldState = oldState;
        this.newState = newState;
    }

    public BlockPos getPos() {
        return at;
    }

    public World getWorld() {
        return world;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Nullable
    public TileEntity getTileEntity() {
        return world.getTileEntity(getPos().getX(), getPos().getY(), getPos().getZ());
    }

//    public Block getOldState() {
//        return oldState;
//    }
//
//    public Block getNewState() {
//        return newState;
//    }

    public Block getOldBlock() {
        return oldState;
    }

    public Block getNewBlock() {
        return newState;
    }

}
