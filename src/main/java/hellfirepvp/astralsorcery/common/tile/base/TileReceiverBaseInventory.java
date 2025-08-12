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

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TileReceiverBaseInventoried
 * Created by HellFirePvP
 * Date: 21.09.2016 / 23:34
 */
public abstract class TileReceiverBaseInventory extends TileReceiverBase {

    protected int inventorySize;
    private ItemHandlerTile handle;
    private List<ForgeDirection> applicableSides;

    public TileReceiverBaseInventory() {
        this(0);
    }

    public TileReceiverBaseInventory(int inventorySize) {
        this(inventorySize, ForgeDirection.values());
    }

    public TileReceiverBaseInventory(int inventorySize, ForgeDirection... applicableSides) {
        this.inventorySize = inventorySize;
        this.handle = createNewItemHandler();
        this.applicableSides = Arrays.asList(applicableSides);
    }

    protected ItemHandlerTile createNewItemHandler() {
        return new ItemHandlerTile(this);
    }

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

        public ItemHandlerTileFiltered(TileReceiverBaseInventory inv) {
            super(inv);
        }

        @Override
        public void setInventorySlotContents(int slot, ItemStack stack) {
            if (canInsertItem(slot, stack, getStackInSlot(slot))) {
                super.setInventorySlotContents(slot, stack);
            }
        }
        //
        // @Override
        // public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        // if(!canInsertItem(slot, stack, getStackInSlot(slot))) {
        // return stack;
        // }
        // return super.insertItem(slot, stack, simulate);
        // }

        public boolean canInsertItem(int slot, ItemStack toAdd, @Nullable ItemStack existing) {
            return true;
        }

    }

    public static class ItemHandlerTile implements IInventory {

        private final TileReceiverBaseInventory tile;

        private ItemStack[] inventory;

        public ItemHandlerTile(TileReceiverBaseInventory inv) {
            // super(inv.inventorySize);
            this.inventory = new ItemStack[inv.inventorySize];
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
            return inventory.length;
        }

        @Override
        public ItemStack getStackInSlot(int slotIn) {
            return inventory[slotIn];
        }

        @Override
        public ItemStack decrStackSize(int slot, int count) {
            if (inventory[slot] != null) {
                if (inventory[slot].stackSize <= count) {
                    ItemStack stack = inventory[slot];
                    inventory[slot] = null;
                    return stack;
                }
                ItemStack split = inventory[slot].splitStack(count);
                if (inventory[slot].stackSize == 0) {
                    inventory[slot] = null;
                }
                return split;
            } else {
                return null;
            }
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int index) {
            return null;
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
            inventory[index] = stack;
            if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                stack.stackSize = getInventoryStackLimit();
            }
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
            return 64;
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player) {
            return true;
        }

        @Override
        public void openInventory() {

        }

        @Override
        public void closeInventory() {

        }

        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return true;
        }
    }
}
