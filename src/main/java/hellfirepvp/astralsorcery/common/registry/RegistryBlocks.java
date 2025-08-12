/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.registry;

import static hellfirepvp.astralsorcery.common.lib.BlocksAS.*;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import hellfirepvp.astralsorcery.common.block.*;
import hellfirepvp.astralsorcery.common.block.fluid.FluidBlockLiquidStarlight;
import hellfirepvp.astralsorcery.common.block.fluid.FluidLiquidStarlight;
import hellfirepvp.astralsorcery.common.block.network.BlockAltar;
import hellfirepvp.astralsorcery.common.block.network.BlockAttunementAltar;
import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystal;
import hellfirepvp.astralsorcery.common.block.network.BlockLens;
import hellfirepvp.astralsorcery.common.block.network.BlockPrism;
import hellfirepvp.astralsorcery.common.block.network.BlockRitualPedestal;
import hellfirepvp.astralsorcery.common.block.network.BlockWell;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockAltar;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockCustomName;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockRitualPedestal;
import hellfirepvp.astralsorcery.common.item.block.ItemCollectorCrystal;
import hellfirepvp.astralsorcery.common.tile.*;
import hellfirepvp.astralsorcery.common.tile.network.TileCollectorCrystal;
import hellfirepvp.astralsorcery.common.tile.network.TileCrystalLens;
import hellfirepvp.astralsorcery.common.tile.network.TileCrystalPrismLens;

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
        // GameRegistry.register(blockLiquidStarlight.setUnlocalizedName(blockLiquidStarlight.getClass().getSimpleName()).setRegistryName(blockLiquidStarlight.getClass().getSimpleName()));
        fluidLiquidStarlight.setBlock(blockLiquidStarlight);

        // FluidRegistry.addBucketForFluid(BlocksAS.fluidLiquidStarlight);
        // ItemsAS.itemBucketLiquidStarlight =
        // UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluidLiquidStarlight);
    }

    // Blocks
    private static void registerBlocks() {
        // WorldGen&Related
        customOre = registerBlockCustomName(new BlockCustomOre());

        customSandOre = registerBlockCustomName(new BlockCustomSandOre());

        customFlower = registerBlockCustomName(new BlockCustomFlower());

        blockMarble = registerBlockCustomName(new BlockMarble());

        blockMarbleStairs = registerBlock(new BlockMarbleStairs());

        blockBlackMarble = registerBlockCustomName(new BlockBlackMarble());

        blockVolatileLight = registerBlock(new BlockFlareLight());

        // Mechanics
        blockAltar = registerBlock(new BlockAltar(), ItemBlockAltar.class);

        attunementAltar = registerBlock(new BlockAttunementAltar());

        attunementRelay = registerBlock(new BlockAttunementRelay());

        ritualPedestal = registerBlock(new BlockRitualPedestal(), ItemBlockRitualPedestal.class);

        blockWell = registerBlock(new BlockWell());

        blockIlluminator = registerBlock(new BlockWorldIlluminator());

        blockMachine = registerBlockCustomName(new BlockMachine());

        blockFakeTree = registerBlock(new BlockFakeTree());

        starlightInfuser = registerBlock(new BlockStarlightInfuser());

        ritualLink = registerBlock(new BlockRitualLink());

        treeBeacon = registerBlock(new BlockTreeBeacon());

        translucentBlock = registerBlock(new BlockTranslucentBlock());

        drawingTable = registerBlockCustomName(new BlockMapDrawingTable());

        // celestialOrrery = registerBlock(new BlockCelestialOrrery());
        // queueDefaultItemBlock(celestialOrrery);

        celestialGateway = registerBlock(new BlockCelestialGateway());

        lens = registerBlock(new BlockLens());
        lensPrism = registerBlock(new BlockPrism());

        celestialCrystals = registerBlockCustomName(new BlockCelestialCrystals());

        // Machines&Related
        // stoneMachine = registerBlock(new BlockStoneMachine());
        collectorCrystal = registerBlock(new BlockCollectorCrystal(), ItemCollectorCrystal.class);
        // celestialCollectorCrystal = registerBlock(new BlockCelestialCollectorCrystal(), ItemCollectorCrystal.class);

        blockStructural = registerBlockCustomName(new BlockStructural());

    }

    // Called after items are registered.
    // Necessary for blocks that require different models/renders for different metadata values
    public static void initRenderRegistry() {
        // registerBlockRender(blockMarble);
        // registerBlockRender(blockBlackMarble);
        // registerBlockRender(blockAltar);
        // registerBlockRender(customOre);
        // registerBlockRender(customSandOre);
        // registerBlockRender(customFlower);
        // registerBlockRender(blockStructural);
        // registerBlockRender(blockMachine);
    }

    // Tiles
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
        // registerTile(TileCelestialOrrery.class);
        registerTile(TileCelestialGateway.class);

        registerTile(TileCrystalLens.class);
        registerTile(TileCrystalPrismLens.class);
    }

    private static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemclass, String name) {
        GameRegistry.registerBlock(block, itemclass, name);
        if (block instanceof BlockDynamicColor) {
            pendingIBlockColorBlocks.add((BlockDynamicColor) block);
        }
        return block;
    }

    private static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemclass) {
        return registerBlock(
            block,
            itemclass,
            block.getClass()
                .getSimpleName());
    }

    private static <T extends Block> T registerBlock(T block) {
        return registerBlock(block, ItemBlock.class);
    }

    private static <T extends Block> T registerBlockCustomName(T block) {
        return registerBlock(block, ItemBlockCustomName.class);
    }

    // private static void registerBlockRender(Block block) {
    // if(block instanceof BlockVariants) {
    // for (Block state : ((BlockVariants) block).getValidStates()) {
    // int meta = ((BlockVariants) block).getMeta();
    // String unlocName = ((BlockVariants) block).getBlockName(state);
    // String name = unlocName + "_" + ((BlockVariants) block).getMetaName(meta);
    // AstralSorcery.proxy.registerVariantName(Item.getItemFromBlock(block), name);
    // AstralSorcery.proxy.registerBlockRender(block, meta, name);
    // }
    // } else {
    // AstralSorcery.proxy.registerVariantName(Item.getItemFromBlock(block), block.getUnlocalizedName());
    // AstralSorcery.proxy.registerBlockRender(block, 0, block.getUnlocalizedName());
    // }
    // }

    private static void registerTile(Class<? extends TileEntity> tile, String name) {
        GameRegistry.registerTileEntity(tile, name);
    }

    private static void registerTile(Class<? extends TileEntity> tile) {
        registerTile(tile, tile.getSimpleName());
    }

}
