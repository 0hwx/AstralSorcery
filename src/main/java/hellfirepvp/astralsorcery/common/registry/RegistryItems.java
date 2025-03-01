/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.block.MaterialAirish;
import hellfirepvp.astralsorcery.common.item.*;
import hellfirepvp.astralsorcery.common.item.base.IItemVariants;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockAltar;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockCustomName;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockRitualPedestal;
import hellfirepvp.astralsorcery.common.item.block.ItemCollectorCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.ItemCelestialCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.ItemRockCrystalSimple;
import hellfirepvp.astralsorcery.common.item.crystal.ItemTunedCelestialCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.ItemTunedRockCrystal;
import hellfirepvp.astralsorcery.common.item.tool.*;
import hellfirepvp.astralsorcery.common.item.wand.ItemArchitectWand;
import hellfirepvp.astralsorcery.common.item.wand.ItemExchangeWand;
import hellfirepvp.astralsorcery.common.item.wand.ItemIlluminationWand;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;

import java.util.LinkedList;
import java.util.List;

import static hellfirepvp.astralsorcery.common.lib.ItemsAS.*;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: RegistryItems
 * Created by HellFirePvP
 * Date: 07.05.2016 / 15:03
 */
public class RegistryItems {

    public static List<ItemDynamicColor> pendingDynamicColorItems = new LinkedList<>();

    public static Item.ToolMaterial crystalToolMaterial;
    public static EnumRarity rarityCelestial;
    public static Material materialTransparentReplaceable;

    public static CreativeTabs creativeTabAstralSorcery,
            creativeTabAstralSorceryPapers,
            creativeTabAstralSorceryTunedCrystals;

    public static void setupDefaults() {
        creativeTabAstralSorcery = new CreativeTabs(AstralSorcery.MODID) {
            @Override
            public Item getTabIconItem() {
                return ItemsAS.journal;
            }
        };
        creativeTabAstralSorceryPapers = new CreativeTabs(AstralSorcery.MODID + ".papers") {
            @Override
            public Item getTabIconItem() {
                return ItemsAS.constellationPaper;
            }
        };
        creativeTabAstralSorceryTunedCrystals = new CreativeTabs(AstralSorcery.MODID + ".crystals") {
            @Override
            public Item getTabIconItem() {
                return ItemsAS.tunedRockCrystal;
            }
        };

        crystalToolMaterial = EnumHelper.addToolMaterial("CRYSTAL", 3, 1000, 20.0F, 5.5F, 40);
        crystalToolMaterial.customCraftingMaterial = null;

        rarityCelestial = EnumHelper.addRarity("CELESTIAL", EnumChatFormatting.BLUE, "Celestial");
        materialTransparentReplaceable = new MaterialAirish();
    }

    public static void init() {
        registerItems();

        registerBlockItems();
    }

    //"Normal" items
    private static void registerItems() {
        GameRegistry.registerItem(new ItemCraftingComponent(), "crafting_component");
        GameRegistry.registerItem(new ItemConstellationPaper(), "constellation_paper");

        GameRegistry.registerItem(new ItemInfusedGlass(), "infused_glass");

        GameRegistry.registerItem(new ItemRockCrystalSimple(), "rock_crystal");
        GameRegistry.registerItem(new ItemTunedRockCrystal(), "tuned_rock_crystal");


        GameRegistry.registerItem(new ItemCelestialCrystal(), "celestial_crystal");
        GameRegistry.registerItem(new ItemTunedCelestialCrystal(), "tuned_celestial_crystal");


        GameRegistry.registerItem(new ItemJournal(), "journal");


        GameRegistry.registerItem(new ItemHandTelescope(), "hand_telescope");


        GameRegistry.registerItem(new ItemLinkingTool(),"linkingTool");
        GameRegistry.registerItem(new ItemWand(),"wand");
        GameRegistry.registerItem(new ItemIlluminationWand(),"illuminationWand");
        GameRegistry.registerItem(new ItemColoredLens(),"colored_lens");
        GameRegistry.registerItem(new ItemSkyResonator(),"skyResonator");
        GameRegistry.registerItem(new ItemShiftingStar(), "shifting_star");
        //roseBranchBow = registerItem(new ItemRoseBranchBow());

        GameRegistry.registerItem(new ItemArchitectWand(),"architectWand");
        GameRegistry.registerItem(new ItemExchangeWand(),"exchangeWand");
        GameRegistry.registerItem(new ItemIlluminationPowder(),"illuminationPowder");

        GameRegistry.registerItem(new ItemCrystalPickaxe(),"crystalPickaxe");
        GameRegistry.registerItem(new ItemCrystalShovel(),"crystalShovel");
        GameRegistry.registerItem(new ItemCrystalAxe(),"crystalAxe");
        GameRegistry.registerItem(new ItemCrystalSword(),"crystalSword");
        GameRegistry.registerItem(new ItemChargedCrystalAxe(),"chargedCrystalAxe");
        GameRegistry.registerItem(new ItemChargedCrystalSword(),"chargedCrystalSword");
        GameRegistry.registerItem(new ItemChargedCrystalPickaxe(),"chargedCrystalPickaxe");
        GameRegistry.registerItem(new ItemChargedCrystalShovel(),"chargedCrystalShovel");
    }

    //Items associated to blocks/itemblocks
    private static void registerBlockItems() {
//        RegistryBlocks.defaultItemBlocksToRegister.forEach(RegistryItems::registerDefaultItemBlock);
//        RegistryBlocks.customNameItemBlocksToRegister.forEach(RegistryItems::registerCustomNameItemBlock);

//        registerItem(new ItemBlockRitualPedestal());
//        registerItem(new ItemBlockAltar());

//        registerItem(new ItemCollectorCrystal(BlocksAS.collectorCrystal));
//        registerItem(new ItemCollectorCrystal(BlocksAS.celestialCollectorCrystal));
    }

    private static <T extends Block> void registerCustomNameItemBlock(T block) {
//        registerItem(new ItemBlockCustomName(block), block.getClass().getSimpleName());
    }

//    private static <T extends Block> void registerDefaultItemBlock(T block) {
//        registerDefaultItemBlock(block, block.getClass().getSimpleName());
//    }

//    private static <T extends Block> void registerDefaultItemBlock(T block, String name) {
//        registerItem(new ItemBlock(block), name);
//    }

    private static <T extends Item> T registerItem(T item, String name) {
        item.setUnlocalizedName(name);
        register(item, name);
        return item;
    }

//    private static <T extends Item> T registerItem(T item) {
//        String simpleName = item.getClass().getSimpleName();
//        if (item instanceof ItemBlock) {
//            simpleName = ((ItemBlock) item).getClass().getSimpleName();
//        }
//        return registerItem(item, simpleName);
//    }

    /*private static <T extends IForgeRegistryEntry> T registerItem(String modId, T item) {
        return registerItem(modId, item, item.getClass().getSimpleName());
    }


    private static <T extends IForgeRegistryEntry> T registerItem(String modId, T item, String name) {
        try {
            LoadController modController = (LoadController) Loader.class.getField("modController").get(Loader.instance());
            Object oldMod = modController.getClass().getField("activeContainer").get(modController);
            modController.getClass().getField("activeContainer").set(modController, Loader.instance().getIndexedModList().get(modId));

            register(item, name);

            modController.getClass().getField("activeContainer").set(modController, oldMod);
            return item;
        } catch (Exception exc) {
            AstralSorcery.log.error("Could not register item with name " + name);
            return null;
        }
    }*/

    private static <T extends Item> void register(T item, String name) {
//        GameRegistry.registerItem(item,name);
        if (item instanceof Item) {
            registerItemInformations((Item) item, name);
            if(item instanceof ItemDynamicColor) {
                pendingDynamicColorItems.add((ItemDynamicColor) item);
            }
        }
    }

    private static <T extends Item> void registerItemInformations(T item, String name) {
        if (item instanceof IItemVariants) {
            for (int i = 0; i < ((IItemVariants) item).getVariants().length; i++) {
                int m = i;
                if (((IItemVariants) item).getVariantMetadatas() != null) {
                    m = ((IItemVariants) item).getVariantMetadatas()[i];
                }
                String vName = name + "_" + ((IItemVariants) item).getVariants()[i];
                if (((IItemVariants) item).getVariants()[i].equals("*")) {
                    vName = name;
                }
                AstralSorcery.proxy.registerItemRender(item, m, vName, true);
            }
        } else {
            AstralSorcery.proxy.registerFromSubItems(item, name);
        }
    }

}
