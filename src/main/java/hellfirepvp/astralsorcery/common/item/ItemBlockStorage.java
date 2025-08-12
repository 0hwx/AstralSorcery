/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemBlockStorage
 * Created by HellFirePvP
 * Date: 03.03.2017 / 17:08
 */
public abstract class ItemBlockStorage extends Item {

    public static void tryStoreBlock(ItemStack storeIn, World w, BlockPos pos) {
        if (w.getTileEntity(pos.getX(), pos.getY(), pos.getZ()) != null) return;
        Block stateToStore = w.getBlock(pos.getX(), pos.getY(), pos.getZ());
        int meta = w.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
        if (Item.getItemFromBlock(stateToStore) == null) return; // Can't charge the player anyway.
        if (stateToStore.getBlockHardness(w, pos.getX(), pos.getY(), pos.getZ()) == -1) return;
        NBTTagCompound cmp = NBTHelper.getPersistentData(storeIn);
        cmp.setString("storedBlock", stateToStore.getUnlocalizedName());
        cmp.setInteger("storedMeta", stateToStore.damageDropped(meta));
    }

    @Nullable
    public static ItemStack getStoredStateAsStack(ItemStack stack) {
        Block stored = getStoredState(stack, stack.getItemDamage());
        if (stored == null) return null; // Guarantees also that the block has an itemblock.
        return ItemUtils.createBlockStack(stored);
    }

    @Nullable
    public static Block getStoredState(ItemStack stack, int meta) {
        NBTTagCompound persistentTag = NBTHelper.getPersistentData(stack);
        ResourceLocation blockRes;
        if (persistentTag.hasKey("storedBlock")) {
            blockRes = new ResourceLocation(persistentTag.getString("storedBlock"));
        } else {
            blockRes = new ResourceLocation("air");
        }
        Block block = GameRegistry.findBlock("astralsorcery", blockRes.toString());
        // Block b = ForgeRegistries.BLOCKS.getValue(blockRes);
        if (block == null) return null;
        if (Item.getItemFromBlock(block) == null) return null; // Can't charge the user properly anyway...

        boolean hasMeta = persistentTag.hasKey("storedMeta");
        meta = persistentTag.getInteger("storedMeta");
        if (hasMeta) {
            meta = block.damageDropped(meta);
        }
        // else {
        // meta = block.damageDropped(0);
        // }
        return block;
    }
}
