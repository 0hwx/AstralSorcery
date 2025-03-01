/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.registry;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.render.tile.TESRAttunementAltar;
import hellfirepvp.astralsorcery.common.block.*;
import hellfirepvp.astralsorcery.common.block.fluid.FluidBlockLiquidStarlight;
import hellfirepvp.astralsorcery.common.block.fluid.FluidLiquidStarlight;
import hellfirepvp.astralsorcery.common.block.network.BlockAltar;
import hellfirepvp.astralsorcery.common.block.network.BlockAttunementAltar;
import hellfirepvp.astralsorcery.common.block.network.BlockCelestialCollectorCrystal;
import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystal;
import hellfirepvp.astralsorcery.common.block.network.BlockLens;
import hellfirepvp.astralsorcery.common.block.network.BlockPrism;
import hellfirepvp.astralsorcery.common.block.network.BlockRitualPedestal;
import hellfirepvp.astralsorcery.common.block.network.BlockWell;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockAltar;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockCustomName;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockRitualPedestal;
import hellfirepvp.astralsorcery.common.item.block.ItemCollectorCrystal;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.tile.*;
import hellfirepvp.astralsorcery.common.tile.network.TileCollectorCrystal;
import hellfirepvp.astralsorcery.common.tile.network.TileCrystalLens;
import hellfirepvp.astralsorcery.common.tile.network.TileCrystalPrismLens;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.LinkedList;
import java.util.List;

import static hellfirepvp.astralsorcery.common.lib.BlocksAS.*;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: RegistryBlocks
 * Created by HellFirePvP
 * Date: 07.05.2016 / 18:16
 */
public class RegistryBlocks {

    public static List<Block> defaultItemBlocksToRegister = new LinkedList<>();
    public static List<Block> customNameItemBlocksToRegister = new LinkedList<>();
    public static List<BlockDynamicColor> pendingIBlockColorBlocks = new LinkedList<>();

    public static void init() {
        registerFluids();

        registerBlocks();

        registerTileEntities();
    }

    private static void registerFluids() {
        FluidLiquidStarlight f = new FluidLiquidStarlight();
        FluidRegistry.registerFluid(f);
        fluidLiquidStarlight = FluidRegistry.getFluid(f.getName());
        blockLiquidStarlight = new FluidBlockLiquidStarlight();
//        GameRegistry.register(blockLiquidStarlight.setUnlocalizedName(blockLiquidStarlight.getClass().getSimpleName()).setRegistryName(blockLiquidStarlight.getClass().getSimpleName()));
        fluidLiquidStarlight.setBlock(blockLiquidStarlight);

//        FluidRegistry.addBucketForFluid(BlocksAS.fluidLiquidStarlight);
//        ItemsAS.itemBucketLiquidStarlight = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluidLiquidStarlight);
    }

    //Blocks
    private static void registerBlocks() {
        //WorldGen&Related
        customOre = new BlockCustomOre();
        GameRegistry.registerBlock(customOre, ItemBlockCustomName.class, "blockCustomOre");
//        customOre = registerBlock(new BlockCustomOre());
//        queueCustomNameItemBlock(customOre);
        customSandOre = new BlockCustomSandOre();
        GameRegistry.registerBlock(customSandOre, ItemBlockCustomName.class, "blockCustomSandOre");
//        customSandOre = registerBlock(new BlockCustomSandOre());
//        queueCustomNameItemBlock(customSandOre);
        customFlower = new BlockCustomFlower();
        GameRegistry.registerBlock(customFlower, ItemBlockCustomName.class, "blockCustomFlower");
//        customFlower = registerBlock(new BlockCustomFlower());
//        queueCustomNameItemBlock(customFlower);

        blockMarble = new BlockMarble();
        GameRegistry.registerBlock(blockMarble, ItemBlockCustomName.class, "blockMarble");
//        blockMarble = registerBlock(new BlockMarble());
//        queueCustomNameItemBlock(blockMarble);
        blockMarbleStairs = new BlockMarbleStairs();
        GameRegistry.registerBlock(blockMarbleStairs, ItemBlockCustomName.class, "blockMarbleStairs");
//        blockMarbleStairs = registerBlock(new BlockMarbleStairs());
//        queueDefaultItemBlock(blockMarbleStairs);
        blockBlackMarble = new BlockBlackMarble();
        GameRegistry.registerBlock(blockBlackMarble, ItemBlockCustomName.class, "blockBlackMarble");
//        blockBlackMarble = registerBlock(new BlockBlackMarble());
//        queueCustomNameItemBlock(blockBlackMarble);
        blockVolatileLight = new BlockFlareLight();
        GameRegistry.registerBlock(blockVolatileLight, ItemBlockCustomName.class, "blockVolatileLight");
//        blockVolatileLight = registerBlock(new BlockFlareLight());
//        queueDefaultItemBlock(blockVolatileLight);

        //Mechanics
        blockAltar = new BlockAltar();
        GameRegistry.registerBlock(blockAltar, ItemBlockAltar.class, "BlockAltar");
//        blockAltar = registerBlock(new BlockAltar());
        attunementAltar = registerBlock(new BlockAttunementAltar());
        queueDefaultItemBlock(attunementAltar);
        attunementRelay = registerBlock(new BlockAttunementRelay());
        queueDefaultItemBlock(attunementRelay);
        ritualPedestal = new BlockRitualPedestal();
        GameRegistry.registerBlock(ritualPedestal, ItemBlockRitualPedestal.class,"ritualPedestal");
//        ritualPedestal = registerBlock(new BlockRitualPedestal());
        blockWell = registerBlock(new BlockWell());
        queueDefaultItemBlock(blockWell);
        blockIlluminator = registerBlock(new BlockWorldIlluminator());
        queueDefaultItemBlock(blockIlluminator);
        blockMachine = new BlockMachine();
        GameRegistry.registerBlock(blockMachine, ItemBlockCustomName.class, "blockMachine");
//        blockMachine = registerBlock(new BlockMachine());
//        queueCustomNameItemBlock(blockMachine);
        blockFakeTree = registerBlock(new BlockFakeTree());
        queueDefaultItemBlock(blockFakeTree);
        starlightInfuser = registerBlock(new BlockStarlightInfuser());
        queueDefaultItemBlock(starlightInfuser);
        ritualLink = registerBlock(new BlockRitualLink());
        queueDefaultItemBlock(ritualLink);

        GameRegistry.registerBlock(new BlockTreeBeacon(), ItemBlockCustomName.class, "blockTreeBeacon");
//        treeBeacon = registerBlock(new BlockTreeBeacon());
//        queueDefaultItemBlock(treeBeacon);
        translucentBlock = registerBlock(new BlockTranslucentBlock());
        queueDefaultItemBlock(translucentBlock);
        drawingTable = registerBlock(new BlockMapDrawingTable());
        queueCustomNameItemBlock(drawingTable);
        //celestialOrrery = registerBlock(new BlockCelestialOrrery());
        //queueDefaultItemBlock(celestialOrrery);
        celestialGateway = registerBlock(new BlockCelestialGateway());
        queueDefaultItemBlock(celestialGateway);

        lens = registerBlock(new BlockLens());
        lensPrism = registerBlock(new BlockPrism());
        queueDefaultItemBlock(lens);
        queueDefaultItemBlock(lensPrism);

        celestialCrystals = registerBlock(new BlockCelestialCrystals());
        queueCustomNameItemBlock(celestialCrystals);

        //Machines&Related
        //stoneMachine = registerBlock(new BlockStoneMachine());
        collectorCrystal = new BlockCollectorCrystal();
        GameRegistry.registerBlock(collectorCrystal, ItemCollectorCrystal.class, "blockCollectorCrystal");
        celestialCollectorCrystal = new BlockCelestialCollectorCrystal();
        GameRegistry.registerBlock(celestialCollectorCrystal, ItemCollectorCrystal.class, "celestialCollectorCrystal");
//        collectorCrystal = registerBlock(new BlockCollectorCrystal());
//        celestialCollectorCrystal = registerBlock(new BlockCelestialCollectorCrystal());

        blockStructural = new BlockStructural();
        GameRegistry.registerBlock(blockStructural, ItemBlockCustomName.class, "blockStructural");
//        blockStructural = registerBlock(new BlockStructural());
//        queueCustomNameItemBlock(blockStructural);
    }

    //Called after items are registered.
    //Necessary for blocks that require different models/renders for different metadata values
    public static void initRenderRegistry() {
        registerBlockRender(blockMarble);
        registerBlockRender(blockBlackMarble);
        registerBlockRender(blockAltar);
        registerBlockRender(customOre);
        registerBlockRender(customSandOre);
        registerBlockRender(customFlower);
        registerBlockRender(blockStructural);
        registerBlockRender(blockMachine);
    }

    //Tiles
    private static void registerTileEntities() {
        registerTile(TileAltar.class);
        registerTile(TileRitualPedestal.class);
        registerTile(TileCollectorCrystal.class);
        registerTile(TileCelestialCrystals.class);
        registerTile(TileWell.class);
        registerTile(TileIlluminator.class);
        registerTile(TileTelescope.class);
        registerTile(TileGrindstone.class);
        registerTile(TileStructuralConnector.class);
        registerTile(TileFakeTree.class);
        registerTile(TileAttunementAltar.class);
        registerTile(TileStarlightInfuser.class);
        registerTile(TileTreeBeacon.class);
        registerTile(TileRitualLink.class);
        registerTile(TileTranslucent.class);
        registerTile(TileAttunementRelay.class);
        registerTile(TileMapDrawingTable.class);
        //registerTile(TileCelestialOrrery.class);
        registerTile(TileCelestialGateway.class);

        registerTile(TileCrystalLens.class);
        registerTile(TileCrystalPrismLens.class);
    }

    private static void queueCustomNameItemBlock(Block block) {
        customNameItemBlocksToRegister.add(block);
    }

    private static void queueDefaultItemBlock(Block block) {
        defaultItemBlocksToRegister.add(block);
    }

    private static <T extends Block> T registerBlock(T block, String name) {
        GameRegistry.registerBlock(block,name);
        if(block instanceof BlockDynamicColor) {
            pendingIBlockColorBlocks.add((BlockDynamicColor) block);
        }
        return block;
    }

    private static <T extends Block> T registerBlock(T block) {
        return registerBlock(block, block.getClass().getSimpleName());
    }

    private static void registerBlockRender(Block block) {
        if(block instanceof BlockVariants) {
            for (Block state : ((BlockVariants) block).getValidStates()) {
                int meta = ((BlockVariants) block).getMeta();
                String unlocName = ((BlockVariants) block).getBlockName(state);
                String name = unlocName + "_" + ((BlockVariants) block).getMetaName(meta);
                AstralSorcery.proxy.registerVariantName(Item.getItemFromBlock(block), name);
                AstralSorcery.proxy.registerBlockRender(block, meta, name);
            }
        } else {
            AstralSorcery.proxy.registerVariantName(Item.getItemFromBlock(block), block.getUnlocalizedName());
            AstralSorcery.proxy.registerBlockRender(block, 0, block.getUnlocalizedName());
        }
    }

    private static void registerTile(Class<? extends TileEntity> tile, String name) {
        GameRegistry.registerTileEntity(tile, name);
    }

    private static void registerTile(Class<? extends TileEntity> tile) {
        registerTile(tile, tile.getSimpleName());
    }

//    public static class FluidCustomModelMapper extends StateMapperBase implements ItemMeshDefinition {
//
//        private final ModelResourceLocation res;
//
//        public FluidCustomModelMapper(Fluid f) {
//            this.res = new ModelResourceLocation(AstralSorcery.MODID.toLowerCase() + ":BlockFluids", f.getName());
//        }
//
//        @Override
//        public ModelResourceLocation getModelLocation(ItemStack stack) {
//            return res;
//        }
//
//        @Override
//        public ModelResourceLocation getModelResourceLocation(Block state) {
//            return res;
//        }
//
//    }

}
