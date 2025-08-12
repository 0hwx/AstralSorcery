/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Lists;

import hellfirepvp.astralsorcery.common.base.Mods;
import hellfirepvp.astralsorcery.common.integrations.ModIntegrationBotania;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemUtils
 * Created by HellFirePvP
 * Date: 31.07.2016 / 17:51
 */
public class ItemUtils {

    private static final Random rand = new Random();

    public static EntityItem dropItem(World world, double x, double y, double z, ItemStack stack) {
        if (world.isRemote) return null;
        EntityItem ei = new EntityItem(world, x, y, z, stack);
        ei.motionX = 0;
        ei.motionY = 0;
        ei.motionZ = 0;
        world.spawnEntityInWorld(ei);
        ei.delayBeforeCanPickup = 10;
        return ei;
    }

    public static EntityItem dropItemNaturally(World world, double x, double y, double z, ItemStack stack) {
        if (world.isRemote) return null;
        EntityItem ei = new EntityItem(world, x, y, z, stack);
        applyRandomDropOffset(ei);
        world.spawnEntityInWorld(ei);
        ei.delayBeforeCanPickup = 10;
        return ei;
    }

    private static void applyRandomDropOffset(EntityItem item) {
        item.motionX = rand.nextFloat() * 0.3F - 0.15D;
        item.motionY = rand.nextFloat() * 0.3F - 0.15D;
        item.motionZ = rand.nextFloat() * 0.3F - 0.15D;
    }

    @Nullable
    public static ItemStack createBlockStack(Block block) {
        Item i = Item.getItemFromBlock(block);
        if (i == null) return null;
        int meta = block.damageDropped(0);// todo check this
        return new ItemStack(i, 1, meta);
    }

    @Nullable
    public static Block createBlockState(ItemStack stack) {
        Block b = Block.getBlockFromItem(stack.getItem());
        if (b == Blocks.air) return null;
        return b;
        // try {
        // return b.getStateFromMeta(stack.getMetadata());
        // } catch (Exception exc) {
        // return b.getDefaultState();
        // }
    }

    public static Collection<ItemStack> scanInventoryFor(IInventory handler, Item i) {
        List<ItemStack> out = new LinkedList<>();
        for (int j = 0; j < handler.getSizeInventory(); j++) {
            ItemStack s = handler.getStackInSlot(j);
            if (s != null && s.getItem() != null && s.getItem() == i) out.add(copyStackWithSize(s, s.stackSize));
        }
        return out;
    }

    public static Collection<ItemStack> scanInventoryForMatching(IInventory handler, ItemStack match, boolean strict) {
        return findItemsInInventory(handler, match, strict);
    }

    public static Collection<ItemStack> findItemsInPlayerInventory(EntityPlayer player, ItemStack match,
        boolean strict) {
        return findItemsInInventory(player.inventory, match, strict);
    }

    public static Collection<ItemStack> findItemsInInventory(IInventory handler, ItemStack match, boolean strict) {
        List<ItemStack> stacksOut = new LinkedList<>();
        for (int j = 0; j < handler.getSizeInventory(); j++) {
            ItemStack s = handler.getStackInSlot(j);
            if (strict ? matchStacks(s, match) : matchStackLoosely(s, match)) {
                stacksOut.add(copyStackWithSize(s, s.stackSize));
            }
        }
        return stacksOut;
    }

    public static Map<Integer, ItemStack> findItemsIndexedInInventory(IInventory handler, ItemStack match,
        boolean strict) {
        Map<Integer, ItemStack> stacksOut = new HashMap<>();
        for (int j = 0; j < handler.getSizeInventory(); j++) {
            ItemStack s = handler.getStackInSlot(j);
            if (strict ? matchStacks(s, match) : matchStackLoosely(s, match)) {
                stacksOut.put(j, copyStackWithSize(s, s.stackSize));
            }
        }
        return stacksOut;
    }

    public static boolean consumeFromPlayerInventory(EntityPlayer player, ItemStack requestingItemStack,
        ItemStack toConsume, boolean simulate) {
        if (toConsume == null || toConsume.getItem() == null) return false; // Uh....

        int consumed = 0;
        if (Mods.BOTANIA.isPresent()) {
            Block consumeState = createBlockState(toConsume);
            if (consumeState != null) {
                Block b = consumeState;
                int meta = b.damageDropped(requestingItemStack.getItemDamage());

                for (int i = 0; i < toConsume.stackSize; i++) {
                    ItemStack res = ModIntegrationBotania
                        .requestFromInventory(player, requestingItemStack, b, meta, simulate);
                    if (res != null && res.getItem() != null) {
                        consumed++;
                    }
                }
            }
        }
        ItemStack tryConsume = copyStackWithSize(toConsume, toConsume.stackSize - consumed);
        return tryConsume == null || tryConsume.getItem() == null
            || consumeFromInventory(player.inventory, tryConsume, simulate);
    }

    public static boolean tryConsumeFromInventory(IInventory handler, ItemStack toConsume, boolean simulate) {
        return consumeFromInventory(handler, toConsume, simulate);
    }

    public static boolean consumeFromInventory(IInventory handler, ItemStack toConsume, boolean simulate) {
        Map<Integer, ItemStack> contents = findItemsIndexedInInventory(handler, toConsume, false);
        if (contents.isEmpty()) return false;

        int cAmt = toConsume.stackSize;
        for (int slot : contents.keySet()) {
            ItemStack inSlot = contents.get(slot);
            int toRemove = cAmt > inSlot.stackSize ? inSlot.stackSize : cAmt;
            cAmt -= toRemove;
            if (!simulate) {
                handler.setInventorySlotContents(slot, copyStackWithSize(inSlot, inSlot.stackSize - toRemove));
            }
            if (cAmt <= 0) {
                break;
            }
        }
        return cAmt <= 0;
    }

    public static void dropInventory(IInventory handle, World worldIn, BlockPos pos) {
        if (worldIn.isRemote) return;
        for (int i = 0; i < handle.getSizeInventory(); i++) {
            ItemStack stack = handle.getStackInSlot(i);
            if (stack == null) continue;
            dropItemNaturally(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        }
    }

    public static ItemStack drainFluidFromItem(ItemStack stack, Fluid fluid, int mbAmount, boolean doDrain) {
        return drainFluidFromItem(stack, new FluidStack(fluid, mbAmount), doDrain);
    }

    public static ItemStack drainFluidFromItem(ItemStack stack, FluidStack fluidStack, boolean doDrain) {
        return stack; // FluidUtil.tryEmptyContainer(stack, FluidHandlerVoid.INSTANCE, fluidStack.amount, null,
                      // doDrain);
    }

    /*
     * public static void decrStackInInventory(ItemStack[] stacks, int slot) {
     * if(slot < 0 || slot >= stacks.length) return;
     * ItemStack st = stacks[slot];
     * if(st == null) return;
     * st.stackSize--;
     * if(st.stackSize <= 0) {
     * stacks[slot] = null;
     * }
     * }
     */

    public static void decrStackInInventory(IInventory handler, int slot) {
        if (slot < 0 || slot >= handler.getSizeInventory()) return;
        ItemStack st = handler.getStackInSlot(slot);
        if (st == null) return;
        st.stackSize--;
        if (st.stackSize <= 0) {
            handler.setInventorySlotContents(slot, null);
        }
    }

    public static boolean tryPlaceItemInInventory(ItemStack stack, ISidedInventory handler) {
        return tryPlaceItemInInventory(stack, handler, 0, handler.getSizeInventory());
    }

    public static boolean tryPlaceItemInInventory(ItemStack stack, ISidedInventory handler, int start, int end) {
        ItemStack toAdd = stack.copy();
        if (!hasInventorySpace(toAdd, handler, start, end)) return false;
        int max = stack.getMaxStackSize();

        for (int i = start; i < end; i++) {
            ItemStack in = handler.getStackInSlot(i);
            if (in == null) {
                int added = Math.min(stack.stackSize, max);
                stack.stackSize -= added;
                handler.canInsertItem(i, copyStackWithSize(stack, added), 0);
                return true;
            } else {
                if (stackEqualsNonNBT(stack, in) && matchTags(stack, in)) {
                    int space = max - in.stackSize;
                    int added = Math.min(stack.stackSize, space);
                    stack.stackSize -= added;
                    handler.getStackInSlot(i).stackSize += added;
                    if (stack.stackSize <= 0) return true;
                }
            }
        }
        return stack.stackSize == 0;
    }

    public static boolean hasInventorySpace(ItemStack stack, IInventory handler, int rangeMin, int rangeMax) {
        int size = stack.stackSize;
        int max = stack.getMaxStackSize();
        for (int i = rangeMin; i < rangeMax && size > 0; i++) {
            ItemStack in = handler.getStackInSlot(i);
            if (in == null) {
                size -= max;
            } else {
                if (stackEqualsNonNBT(stack, in) && matchTags(stack, in)) {
                    int space = max - in.stackSize;
                    size -= space;
                }
            }
        }
        return size <= 0;
    }

    public static boolean stackEqualsNonNBT(ItemStack stack, ItemStack other) {
        if (stack == null && other == null) return true;
        if (stack == null || other == null || stack.getItem() == null || other.getItem() == null) return false;
        Item sItem = stack.getItem();
        Item oItem = other.getItem();
        if (sItem.getHasSubtypes() || oItem.getHasSubtypes()) {
            return sItem.equals(other.getItem()) && (stack.getItemDamage() == other.getItemDamage()
                || stack.getItemDamage() == OreDictionary.WILDCARD_VALUE
                || other.getItemDamage() == OreDictionary.WILDCARD_VALUE);
        } else {
            return sItem.equals(other.getItem());
        }
    }

    public static ItemStack copyStackWithSize(ItemStack stack, int amount) {
        if (stack == null || stack.getItem() == null || amount <= 0) return null;
        ItemStack s = stack.copy();
        s.stackSize = amount;
        return s;
    }

    public static boolean hasOreNamePart(ItemStack stack, String namePart) {
        namePart = namePart.toLowerCase();
        List<String> oreNames = getOreDictNames(stack);
        for (String s : oreNames) {
            if (s.contains(namePart)) return true;
        }
        return false;
    }

    public static boolean hasOreName(ItemStack stack, String name) {
        name = name.toLowerCase();
        return getOreDictNames(stack).contains(name);
    }

    private static List<String> getOreDictNames(ItemStack stack) {
        List<String> out = Lists.newArrayList();
        for (int id : OreDictionary.getOreIDs(stack)) {
            out.add(
                OreDictionary.getOreName(id)
                    .toLowerCase());
        }
        return out;
    }

    public static boolean matchTags(ItemStack stack, ItemStack other) {
        return ItemStack.areItemStackTagsEqual(stack, other);
    }

    public static boolean matchStacksStrict(ItemStack stack, ItemStack other) {
        return ItemStack.areItemStacksEqual(stack, other);
    }

    public static boolean matchStacks(ItemStack stack, ItemStack other) {
        if (!ItemStack.areItemStacksEqual(stack, other)) return false;
        return ItemStack.areItemStackTagsEqual(stack, other);
    }

    public static boolean matchStackLoosely(ItemStack stack, ItemStack other) {
        if (stack == null) return other == null;
        return stack.isItemEqual(other);
    }

    public static boolean matchesOreDict(String oreDictKey, ItemStack other) {
        List<ItemStack> stacks = OreDictionary.getOres(oreDictKey);
        for (ItemStack stack : stacks) {
            if (stack == null) continue;
            if (matchStackLoosely(stack, other)) return true;
        }
        return false;
    }

    private static class FluidHandlerVoid implements IFluidHandler {

        private static FluidHandlerVoid INSTANCE = new FluidHandlerVoid();

        @Override
        public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
            return resource.amount;
        }

        @Override
        public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
            return null;
        }

        @Override
        public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
            return null;
        }

        @Override
        public boolean canFill(ForgeDirection from, Fluid fluid) {
            return false;
        }

        @Override
        public boolean canDrain(ForgeDirection from, Fluid fluid) {
            return false;
        }

        @Override
        public FluidTankInfo[] getTankInfo(ForgeDirection from) {
            return new FluidTankInfo[0];
        }
    }

}
