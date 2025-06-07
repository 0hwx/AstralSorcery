/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.entities.EntityItemHighlighted;
import hellfirepvp.astralsorcery.common.item.base.ItemHighlighted;
import hellfirepvp.astralsorcery.common.lib.Sounds;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.SoundHelper;
import hellfirepvp.astralsorcery.common.util.WRItemObject;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.WeightedRandom;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemConstellationPaper
 * Created by HellFirePvP
 * Date: 07.05.2016 / 15:16
 */
public class ItemConstellationPaper extends Item implements ItemHighlighted {

    public ItemConstellationPaper() {
        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(RegistryItems.creativeTabAstralSorceryPapers);
        setUnlocalizedName("ItemConstellationPaper");
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.add(new ItemStack(this, 1));

        for (IConstellation c : ConstellationRegistry.getAllConstellations()) {
            if (c instanceof IMinorConstellation) continue;

            ItemStack cPaper = new ItemStack(this, 1);
            setConstellation(cPaper, c);
            subItems.add(cPaper);
        }
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity entity, ItemStack itemstack) {
        EntityItemHighlighted ei = new EntityItemHighlighted(world, entity.posX, entity.posY, entity.posZ, itemstack);
        ei.delayBeforeCanPickup = 10;
        ei.motionX = entity.motionX;
        ei.motionY = entity.motionY;
        ei.motionZ = entity.motionZ;
        return ei;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        IConstellation c = getConstellation(stack);
        if (c != null) {
            tooltip.add(ChatFormatting.BLUE + I18n.format(c.getUnlocalizedName()));
        } else {
            tooltip.add(ChatFormatting.GRAY + I18n.format("constellation.noInformation"));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn) {
        if(world.isRemote && getConstellation(stack) != null) {
            SoundHelper.playSoundClient(Sounds.bookFlip, 1F, 1F);
            AstralSorcery.proxy.openGui(CommonProxy.EnumGuiId.CONSTELLATION_PAPER, playerIn, world, ConstellationRegistry.getConstellationId(getConstellation(stack)), 0, 0);
        }
        return stack;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote || entityIn == null || !(entityIn instanceof EntityPlayer)) return;

        IConstellation cst = getConstellation(stack);

        if(cst != null) {
            PlayerProgress progress = ResearchManager.getProgress((EntityPlayer) entityIn);
            if(progress != null) {
                boolean has = false;
                for (String strConstellation : progress.getSeenConstellations()) {
                    IConstellation c = ConstellationRegistry.getConstellationByName(strConstellation);
                    if(c != null && c.equals(cst)) {
                        has = true;
                        break;
                    }
                }
                if(!has) {
                    if(ResearchManager.memorizeConstellation(cst, (EntityPlayer) entityIn)) {
                        ((EntityPlayer) entityIn).addChatMessage(
                                new ChatComponentTranslation("progress.seen.constellation.chat",
                                        new ChatComponentTranslation(cst.getUnlocalizedName())
                                                .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)))
                                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
                        if(ResearchManager.clientProgress.getSeenConstellations().size() == 1) {
                            ((EntityPlayer) entityIn).addChatMessage(
                                    new ChatComponentTranslation("progress.seen.constellation.first.chat")
                                            .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
                        }
                    }
                }
            }
            return;
        }

        if ((worldIn.getTotalWorldTime() & 7)  == 0) {
            PlayerProgress progress = ResearchManager.getProgress((EntityPlayer) entityIn);
            if (progress != null) {
                List<IConstellation> constellations = new ArrayList<>();
                for (IConstellation c : ConstellationRegistry.getAllConstellations()) {
                    if(c.canDiscover(progress)) {
                        constellations.add(c);
                    }
                }

                for (String strConstellation : progress.getKnownConstellations()) {
                    IConstellation c = ConstellationRegistry.getConstellationByName(strConstellation);
                    if(c != null) {
                        constellations.remove(c);
                    }
                }
                for (String strConstellation : progress.getSeenConstellations()) {
                    IConstellation c = ConstellationRegistry.getConstellationByName(strConstellation);
                    if(c != null) {
                        constellations.remove(c);
                    }
                }

                if (constellations.isEmpty()) {
                    return;
                }

                List<WRItemObject<IConstellation>> wrp = buildWeightedRandomList(constellations);
                WRItemObject<IConstellation> result = (WRItemObject<IConstellation>) WeightedRandom.getRandomItem(worldIn.rand, wrp); //todo if this doesn't work use the one below
                setConstellation(stack, result.getValue());
//                WeightedRandom.Item item = WeightedRandom.getRandomItem(worldIn.rand, wrp);
//
//                if (item instanceof WRItemObject) {
//                    WRItemObject<IConstellation> result = (WRItemObject<IConstellation>) item;
//                    // Now you can use `result` safely
//                } else {
//                    // Handle the case where the item is not of the expected type
//                    // For example, log a warning or throw an exception
//                }
            }
        }
    }

    private List<WRItemObject<IConstellation>> buildWeightedRandomList(List<IConstellation> constellations) {
        List<WRItemObject<IConstellation>> wrc = new ArrayList<>();
        for (IConstellation c : constellations) {
            WRItemObject<IConstellation> i = new WRItemObject<>(1, c);//(int) (tier.getShowupChance() * 100), c);
            wrc.add(i);
        }
        return wrc;
    }

    @Override
    public Color getHightlightColor(ItemStack stack) {
        IConstellation c = getConstellation(stack);
        return c == null ? Color.GRAY : c.getConstellationColor();
    }

    public static IConstellation getConstellation(ItemStack stack) {
        Item i = stack.getItem();
        if (!(i instanceof ItemConstellationPaper)) return null;
        return IConstellation.readFromNBT(NBTHelper.getPersistentData(stack));
    }

    public static void setConstellation(ItemStack stack, IConstellation constellation) {
        Item i = stack.getItem();
        if (!(i instanceof ItemConstellationPaper)) return;
        NBTTagCompound tag = NBTHelper.getPersistentData(stack);
        constellation.writeToNBT(tag);
    }



    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register)
    {
        this.itemIcon = register.registerIcon("astralsorcery:scroll");
    }
}
