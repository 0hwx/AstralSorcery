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
        this.handle.deserializeNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setTag("inventory", this.handle.serializeNBT());
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
                onContentsChanged(slot);
            }
        }

        public boolean canInsertItem(int slot, ItemStack toAdd, @Nullable ItemStack existing) {
            return true;
        }

    }

    public static class ItemHandlerTile implements IInventory {

        private final TileReceiverBaseInventory tile;
        private ItemStack[] inventory;

        public ItemHandlerTile(TileReceiverBaseInventory inv) {
            this.inventory = new ItemStack[inv.inventorySize];
            this.tile = inv;
        }

        public void onContentsChanged(int slot) {
            tile.onInventoryChanged(slot);
            tile.markForUpdate();
        }

        public void clearInventory() {
            for (int i = 0; i < getSizeInventory(); i++) {
                setInventorySlotContents(i, null);
                onContentsChanged(i);
            }
        }

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
            onContentsChanged(index);
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
            tile.markForUpdate();
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

        // NBT serialization methods for 1.7.10
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            for (int i = 0; i < inventory.length; i++) {
                if (inventory[i] != null) {
                    NBTTagCompound itemNBT = new NBTTagCompound();
                    inventory[i].writeToNBT(itemNBT);
                    nbt.setTag("item_" + i, itemNBT);
                }
            }
            return nbt;
        }

        public void deserializeNBT(NBTTagCompound nbt) {
            for (int i = 0; i < inventory.length; i++) {
                if (nbt.hasKey("item_" + i)) {
                    inventory[i] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item_" + i));
                } else {
                    inventory[i] = null;
                }
            }
        }
    }
}
