/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.tile.base;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TileEntitySynchronized
 * Created by HellFirePvP
 * Date: 11.05.2016 / 18:17
 */
public abstract class TileEntitySynchronized extends TileEntity {

    protected static final Random rand = new Random();

    public final void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readCustomNBT(compound);
    }

    public void readCustomNBT(NBTTagCompound compound) {}

    public void readNetNBT(NBTTagCompound compound) {}

    public final void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeCustomNBT(compound);

    }

    public void writeCustomNBT(NBTTagCompound compound) {}

    public void writeNetNBT(NBTTagCompound compound) {}

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        super.writeToNBT(nbttagcompound);
        writeCustomNBT(nbttagcompound);
        writeNetNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.func_148857_g());
        readCustomNBT(pkt.func_148857_g());
        readNetNBT(pkt.func_148857_g());
    }

    public void markForUpdate() {
        Block thisState = worldObj.getBlock(xCoord, yCoord, zCoord);
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, thisState);
        markDirty();
    }

}
