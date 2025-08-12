/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.tile.base;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TileInventoryBase
 * Created by HellFirePvP
 * Date: 27.03.2017 / 17:53
 */
public class TileInventoryBase extends TileEntityTick {

    protected int inventorySize;
    private ItemHandlerTile handle;
    private List<ForgeDirection> applicableSides;

    public TileInventoryBase() {
        this(0);
    }

    public TileInventoryBase(int inventorySize) {
        this(inventorySize, ForgeDirection.values());
    }

    public TileInventoryBase(int inventorySize, ForgeDirection... applicableSides) {
        this.inventorySize = inventorySize;
        this.handle = createNewItemHandler();
        this.applicableSides = Arrays.asList(applicableSides);
    }

    protected ItemHandlerTile createNewItemHandler() {
        return new ItemHandlerTile(this);
    }

    @Override
    protected void onFirstTick() {}

    public ItemHandlerTile getInventoryHandler() {
        return handle;
    }

    private boolean hasHandlerForSide(ForgeDirection facing) {
        return facing == null || applicableSides.contains(facing);
    }

    // @Override
    // public boolean hasCapability(Capability<?> capability, ForgeDirection facing) {
    // return hasHandlerForSide(facing) ? capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY :
    // super.hasCapability(capability, facing);
    // }
    //
    // @Override
    // public <T> T getCapability(Capability<T> capability, ForgeDirection facing) {
    // if(hasHandlerForSide(facing)) {
    // if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
    // return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handle);
    // }
    // }
    // return super.getCapability(capability, facing);
    // }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.handle = createNewItemHandler();
        // this.handle.deserializeNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        // compound.setTag("inventory", this.handle.serializeNBT());
    }

    public int getInventorySize() {
        return inventorySize;
    }

    protected void onInventoryChanged(int slotChanged) {}

    public static class ItemHandlerTileFiltered extends ItemHandlerTile {

        public ItemHandlerTileFiltered(TileInventoryBase inv) {
            super(inv);
        }

        // @Override
        // public void setStackInSlot(int slot, ItemStack stack) {
        // if(canInsertItem(slot, stack, getStackInSlot(slot))) {
        // super.setStackInSlot(slot, stack);
        // }
        // }

        @Nonnull
        @Override
        public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
            if (!canInsertItem(aIndex, aStack, getStackInSlot(aIndex))) {
                return true;
            }
            return super.canInsertItem(aIndex, aStack, ordinalSide);
        }

        public boolean canInsertItem(int aIndex, ItemStack toAdd, @Nonnull ItemStack existing) {
            return true;
        }

    }

    public static class ItemHandlerTile implements ISidedInventory {

        private final TileInventoryBase tile;

        public ItemHandlerTile(TileInventoryBase inv) {
            super();
            // super(inv.inventorySize);
            this.tile = inv;
        }

        // @Override
        // public void onContentsChanged(int slot) {
        // tile.onInventoryChanged(slot);
        // tile.markForUpdate();
        // }
        //
        // public void clearInventory() {
        // for (int i = 0; i < getSlots(); i++) {
        // setStackInSlot(i, null);
        // onContentsChanged(i);
        // }
        // }
        //
        // @Override
        // public int getStackLimit(int slot, ItemStack stack) {
        // return super.getStackLimit(slot, stack);
        // }

        @Override
        public int getSizeInventory() {
            return 0;
        }

        @Override
        public ItemStack getStackInSlot(int slotIn) {
            return null;
        }

        @Override
        public ItemStack decrStackSize(int index, int count) {
            return null;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int index) {
            return null;
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
            tile.onInventoryChanged(index);
            tile.markForUpdate();
        }

        @Override
        public String getInventoryName() {
            return "";
        }

        @Override
        public boolean hasCustomInventoryName() {
            return false;
        }

        @Override
        public int getInventoryStackLimit() {
            return 0;
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player) {
            return false;
        }

        @Override
        public void openInventory() {

        }

        @Override
        public void closeInventory() {
            for (int i = 0; i < getSizeInventory(); i++) {
                setInventorySlotContents(i, null);
                getStackInSlotOnClosing(i);
            }
        }

        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return false;
        }

        @Override
        public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
            return new int[0];
        }

        @Override
        public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
            return false;
        }

        @Override
        public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
            return false;
        }
    }

}
