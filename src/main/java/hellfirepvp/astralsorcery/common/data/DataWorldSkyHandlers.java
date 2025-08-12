/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: DataWorldSkyHandlers
 * Created by HellFirePvP
 * Date: 21.11.2016 / 22:03
 */
public class DataWorldSkyHandlers extends AbstractData {

    private List<Integer> activeWorldSkyHandlers = new LinkedList<>();

    public boolean hasWorldHandler(World world) {
        return world != null && hasWorldHandler(world.provider.dimensionId);
    }

    public boolean hasWorldHandler(int dim) {
        return activeWorldSkyHandlers.contains(dim);
    }

    public static boolean hasWorldHandler(int dim, Side side) {
        DataWorldSkyHandlers handle = SyncDataHolder.getData(side, SyncDataHolder.DATA_SKY_HANDLERS);
        return handle.hasWorldHandler(dim);
    }

    public static boolean hasWorldHandler(World world, Side side) {
        DataWorldSkyHandlers handle = SyncDataHolder.getData(side, SyncDataHolder.DATA_SKY_HANDLERS);
        return handle.hasWorldHandler(world);
    }

    @Override
    public void writeAllDataToPacket(NBTTagCompound compound) {
        NBTTagList dims = new NBTTagList();
        for (Integer i : activeWorldSkyHandlers) {
            dims.appendTag(new NBTTagInt(i));
        }
        compound.setTag("dims", dims);
    }

    @Override
    public void writeToPacket(NBTTagCompound compound) {
        writeAllDataToPacket(compound);
    }

    @Override
    public void readRawFromPacket(NBTTagCompound compound) {
        this.activeWorldSkyHandlers.clear();

        NBTTagList dims = compound.getTagList("dims", 3);
        for (int i = 0; i < dims.tagCount(); i++) {
            this.activeWorldSkyHandlers.add(NBTHelper.getIntAt(dims, i));
        }
    }

    @Override
    public void handleIncomingData(AbstractData serverData) {
        if (!(serverData instanceof DataWorldSkyHandlers)) return;

        this.activeWorldSkyHandlers = ((DataWorldSkyHandlers) serverData).activeWorldSkyHandlers;
    }

    public void update(List<Integer> newDimIds) {
        this.activeWorldSkyHandlers = new LinkedList<>(newDimIds);
        markDirty();
    }

    @SideOnly(Side.CLIENT)
    public void updateClient(List<Integer> dimIds) {
        this.activeWorldSkyHandlers = new LinkedList<>(dimIds);
    }

    public void clientClean() {
        this.activeWorldSkyHandlers = new LinkedList<>();
    }

    public List<Integer> getSkyHandlerDimensions() {
        return Collections.unmodifiableList(activeWorldSkyHandlers);
    }

    public static class Provider extends ProviderAutoAllocate<DataWorldSkyHandlers> {

        public Provider(String key) {
            super(key);
        }

        @Override
        public DataWorldSkyHandlers provideNewInstance() {
            return new DataWorldSkyHandlers();
        }

    }

}
