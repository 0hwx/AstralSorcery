/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.block.material.BlockMachineMaterial;
import hellfirepvp.astralsorcery.common.item.base.IGrindable;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.IVariantTileProvider;
import hellfirepvp.astralsorcery.common.tile.TileGrindstone;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.SwordSharpenHelper;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockStoneMachine
 * Created by HellFirePvP
 * Date: 11.05.2016 / 18:11
 */
public class BlockMachine extends BlockContainer implements BlockCustomName {

    private static final Random rand = new Random();

    // PropertyEnum doesn't exist in 1.7.10 - we use metadata values directly instead

    public BlockMachine() {
        super(new BlockMachineMaterial(MapColor.airColor));
        // Material.BARRIER -> (new Material(MapColor.airColor).setRequiresTool().setImmovableMobility());
        setBlockName("BlockMachine");
        setHardness(3.0F);
        setStepSound(soundTypeStone);
        setResistance(25.0F);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        BlockPos pos = new BlockPos(x, y, z);
        MachineType type = MachineType.values()[meta];
        switch (type) {
            case TELESCOPE:
                RenderingUtils.playBlockBreakParticles(pos.up(), Blocks.planks);
            case GRINDSTONE:
                RenderingUtils.playBlockBreakParticles(pos, Blocks.planks);
                break;
        }
        return false;
    }

    @Override
    public String getHarvestTool(int meta) {
        MachineType t = MachineType.values()[meta];
        switch (t) {
            case TELESCOPE:
                return "axe";
            case GRINDSTONE:
                return "pickaxe";
        }
        return super.getHarvestTool(meta);
    }

    // @Override
    // public SoundType getSoundType(Block state, World world, BlockPos pos, @Nullable Entity entity) {
    // MachineType t = state.getValue(MACHINE_TYPE);
    // switch (t) {
    // case TELESCOPE:
    // return SoundType.WOOD;
    // case GRINDSTONE:
    // return SoundType.STONE;
    // }
    // return super.getSoundType(state, world, pos, entity);
    // }

    // @Override
    // public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
    // int meta = worldIn.getBlockMetadata(x, y, z);
    // MachineType type = MachineType.values()[meta];
    // switch (type) {
    // case TELESCOPE:
    // return AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 2, 1);
    // }
    // return super.getCollisionBoundingBoxFromPool(worldIn, x, y, z);
    // }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        int meta = worldIn.getBlockMetadata(x, y, z);
        MachineType type = MachineType.values()[meta];
        switch (type) {
            case TELESCOPE:
                return AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 2, 1);
        }
        return super.getSelectedBoundingBoxFromPool(worldIn, x, y, z);
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (MachineType type : MachineType.values()) {
            list.add(type.asStack());
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        MachineType type = MachineType.values()[meta];
        if (type == null) return null;
        return type.provideTileEntity(world, type.getMeta());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        MachineType type = MachineType.values()[meta];
        if (type == null) return null;
        return type.provideTileEntity(world, type.getMeta());
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        MachineType type = MachineType.values()[meta];
        if (type == null || type != MachineType.GRINDSTONE) return;
        BlockPos pos = new BlockPos(x, y, z);
        TileGrindstone tgr = MiscUtils.getTileAt(worldIn, pos, TileGrindstone.class, true);
        if (tgr == null || tgr.getGrindingItem() == null) return;
        ItemUtils
            .dropItemNaturally(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, tgr.getGrindingItem());
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        BlockPos pos = new BlockPos(x, y, z);
        int meta = worldIn.getBlockMetadata(x, y, z);
        ItemStack stack = player.getHeldItem();
        MachineType type = MachineType.values()[meta];
        if (type == null) return true;
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        switch (type) {
            case TELESCOPE:
                if (player.worldObj.isRemote) {
                    AstralSorcery.proxy
                        .openGui(CommonProxy.EnumGuiId.TELESCOPE, player, player.worldObj, posX, posY, posZ);
                }
                break;
            case GRINDSTONE:
                TileGrindstone tgr = MiscUtils.getTileAt(worldIn, pos, TileGrindstone.class, true);
                if (tgr != null) {
                    if (!worldIn.isRemote) {
                        ItemStack grind = tgr.getGrindingItem();
                        if (grind != null) {
                            if (player.isSneaking()) {
                                ItemUtils.dropItem(worldIn, posX + 0.5F, posY + 1F, posZ + 0.5F, grind);

                                tgr.setGrindingItem(null);
                            } else {
                                Item i = grind.getItem();
                                if (i instanceof IGrindable) {
                                    IGrindable.GrindResult result = ((IGrindable) i).grind(tgr, grind, rand);
                                    switch (result.getType()) {
                                        case SUCCESS:
                                            tgr.setGrindingItem(grind); // Update
                                            break;
                                        case ITEMCHANGE:
                                            tgr.setGrindingItem(result.getStack());
                                            break;
                                        case FAIL_BREAK_ITEM:
                                            tgr.setGrindingItem(null);
                                            // world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_ITEM_BREAK,
                                            // SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.2F + 0.8F);
                                            break;
                                    }
                                    tgr.playWheelEffect();
                                } else if (SwordSharpenHelper.isSharpenableItem(grind)) {
                                    if (rand.nextInt(40) == 0) {
                                        SwordSharpenHelper.setSwordSharpened(grind);
                                    }
                                    tgr.playWheelEffect();
                                }
                            }
                        } else {
                            if (stack != null) {
                                Item trySet = stack.getItem();
                                if (trySet instanceof IGrindable && ((IGrindable) trySet).canGrind(tgr, stack)) {
                                    ItemStack toSet = stack.copy();
                                    toSet.stackSize = 1;
                                    tgr.setGrindingItem(toSet);
                                    // world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_ITEM_PICKUP,
                                    // SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.2F + 0.8F);

                                    if (!player.capabilities.isCreativeMode) {
                                        stack.stackSize--;
                                    }
                                } else if (SwordSharpenHelper.isSharpenableItem(stack)
                                    && !SwordSharpenHelper.isSwordSharpened(stack)) {
                                        ItemStack toSet = stack.copy();
                                        toSet.stackSize = 1;
                                        tgr.setGrindingItem(toSet);
                                        // world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_ITEM_PICKUP,
                                        // SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.2F + 0.8F);

                                        if (!player.capabilities.isCreativeMode) {
                                            stack.stackSize--;
                                        }
                                    }
                            }
                        }
                    } else {
                        ItemStack grind = tgr.getGrindingItem();
                        if (grind != null && !player.isSneaking()) {
                            Item i = grind.getItem();
                            if (i instanceof IGrindable) {
                                if (((IGrindable) i).canGrind(tgr, grind)) {
                                    for (int j = 0; j < 8; j++) {
                                        worldIn.spawnParticle(
                                            "crit",
                                            posX + 0.5,
                                            posY + 0.8,
                                            posZ + 0.4,
                                            (rand.nextBoolean() ? 1 : -1) * rand.nextFloat() * 0.3,
                                            (rand.nextBoolean() ? 1 : -1) * rand.nextFloat() * 0.3,
                                            (rand.nextBoolean() ? 1 : -1) * rand.nextFloat() * 0.3);
                                    }
                                }
                            } else if (SwordSharpenHelper.canBeSharpened(grind)
                                && !SwordSharpenHelper.isSwordSharpened(grind)) {
                                    for (int j = 0; j < 8; j++) {
                                        worldIn.spawnParticle(
                                            "crit",
                                            posX + 0.5,
                                            posY + 0.8,
                                            posZ + 0.4,
                                            (rand.nextBoolean() ? 1 : -1) * rand.nextFloat() * 0.3,
                                            (rand.nextBoolean() ? 1 : -1) * rand.nextFloat() * 0.3,
                                            (rand.nextBoolean() ? 1 : -1) * rand.nextFloat() * 0.3);
                                    }
                                }
                        }
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        BlockPos pos = new BlockPos(x, y, z).up();
        System.out.println(worldIn);
        System.out.println(pos);
        System.out.println(placer);
        System.out.println(stack);
        MachineType type = MachineType.values()[stack.getItemDamage()];
        switch (type) {
            case TELESCOPE:
                worldIn.setBlock(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    BlocksAS.blockStructural,
                    BlockStructural.BlockType.TELESCOPE_STRUCT.getMeta(),
                    3);
                break;
        }
        super.onBlockPlacedBy(worldIn, x, y, z, placer, stack);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor) {
        BlockPos pos = new BlockPos(x, y, z);
        int meta = worldIn.getBlockMetadata(x, y, z);
        switch (meta) {
            case 0:// TELESCOPE:
                if (worldIn.isAirBlock(
                    pos.up()
                        .getX(),
                    pos.up()
                        .getY(),
                    pos.up()
                        .getZ())) {
                    worldIn.setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
                }
                break;
        }
        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        if (!(world instanceof World)) {
            super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
            return;
        }
        BlockPos pos = new BlockPos(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        switch (meta) {
            case 0:// TELESCOPE:
                if (world.isAirBlock(
                    pos.up()
                        .getX(),
                    pos.up()
                        .getY(),
                    pos.up()
                        .getZ())) {
                    ((World) world).setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
                }
                break;
        }
    }

    // @Override
    // public Block getStateFromMeta(int meta) {
    // return meta < MachineType.values().length ? getDefaultState().withProperty(MACHINE_TYPE,
    // MachineType.values()[meta]) : getDefaultState();
    // }
    //
    // @Override
    // public int getMeta(Block state) {
    // MachineType type = state.getValue(MACHINE_TYPE);
    // return type == null ? 0 : type.ordinal();
    // }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // public boolean isTranslucent(Block state) {
    // return true;
    // }
    //
    // @Override
    // protected BlockStateContainer createBlockState() {
    // return new BlockStateContainer(this, MACHINE_TYPE);
    // }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public String getIdentifierForMeta(int meta) {
        MachineType type = MachineType.values()[meta];
        return type.getName();
    }

    // @Override
    // public List<Block> getValidStates() {
    // List<Block> ret = new LinkedList<>();
    // for (MachineType type : MachineType.values()) {
    // ret.add(getDefaultState().withProperty(MACHINE_TYPE, type));
    // }
    // return ret;
    // }
    //
    // @Override
    // public String getMetaName(int meta) {
    // MachineType type = MachineType.values()[meta];
    // return type.getName();
    // }
    //
    // @Override
    // public int getMeta() {
    // return 0;
    // }

    public static enum MachineType implements IVariantTileProvider {

        TELESCOPE((world, pos) -> new TileTelescope()),
        GRINDSTONE((world, pos) -> new TileGrindstone());

        private final IVariantTileProvider provider;

        private MachineType(IVariantTileProvider provider) {
            this.provider = provider;
        }

        @Override
        public TileEntity provideTileEntity(World world, int metadata) {
            return provider.provideTileEntity(world, metadata);
        }

        public int getMeta() {
            return ordinal();
        }

        public ItemStack asStack() {
            return new ItemStack(BlocksAS.blockMachine, 1, getMeta());
        }

        public String getName() {
            return name().toLowerCase();
        }

        @Override
        public String toString() {
            return getName();
        }
    }

}
