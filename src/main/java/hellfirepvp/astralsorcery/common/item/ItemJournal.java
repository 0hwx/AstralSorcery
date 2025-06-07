/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.container.ContainerJournal;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemJournal
 * Created by HellFirePvP
 * Date: 11.08.2016 / 18:33
 */
public class ItemJournal extends Item {

    public ItemJournal() {
        setMaxStackSize(1);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
        setUnlocalizedName("ItemJournal");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn) {
        if(world.isRemote && !playerIn.isSneaking()) {
            AstralSorcery.proxy.openGui(CommonProxy.EnumGuiId.JOURNAL, playerIn, world, 0, 0, 0);
        } else if(!world.isRemote && playerIn.isSneaking()) {
            AstralSorcery.proxy.openGui(CommonProxy.EnumGuiId.JOURNAL_STORAGE, playerIn, world, 0, 0, 0);
        }
        return stack;
    }

    @Nullable
    public static ContainerJournal getContainer(InventoryPlayer playerInv, ItemStack stack, int journalIndex) {
        if(stack == null || !(stack.getItem() instanceof ItemJournal)) return null;
        return new ContainerJournal(playerInv, stack, journalIndex);
    }

    @Nullable
    public static IInventory getJournalStorage(ItemStack stack) {
        InventoryBasic i = new InventoryBasic("Journal", false, 27);
        ItemStack[] toFill = getStoredConstellationStacks(stack);
        for (int i1 = 0; i1 < toFill.length; i1++) {
            ItemStack item = toFill[i1];
            i.setInventorySlotContents(i1, item);
        }
        return i;
    }

    public static ItemStack[] getStoredConstellationStacks(ItemStack stack) {
        List<IConstellation> out = getStoredConstellations(stack);
        ItemStack[] items = new ItemStack[out.size()];
        for (int i = 0; i < out.size(); i++) {
            IConstellation c = out.get(i);
            ItemStack paper = new ItemStack(ItemsAS.constellationPaper);
            ItemConstellationPaper.setConstellation(paper, c);
            items[i] = paper;
        }
        return items;
    }

    public static List<IConstellation> getStoredConstellations(ItemStack stack) {
        NBTTagCompound cmp = NBTHelper.getPersistentData(stack);
        NBTTagList constellationPapers = cmp.getTagList("constellations", 8);
        LinkedList<IConstellation> out = new LinkedList<>();
        for (int i = 0; i < constellationPapers.tagCount(); i++) {
            IConstellation c = ConstellationRegistry.getConstellationByName(constellationPapers.getStringTagAt(i));
            if(c != null) {
                out.add(c);
            }
        }
        out.sort(Comparator.comparing(IConstellation::getSimpleName));

        return out;
    }

    public static void setStoredConstellations(ItemStack parentJournal, LinkedList<IConstellation> saveConstellations) {
        NBTTagCompound cmp = NBTHelper.getPersistentData(parentJournal);
        NBTTagList list = new NBTTagList();
        for (IConstellation c : saveConstellations) {
            list.appendTag(new NBTTagString(c.getUnlocalizedName()));
        }
        cmp.setTag("constellations", list);
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register)
    {
       this.itemIcon = register.registerIcon("astralsorcery:journal");
    }
}
