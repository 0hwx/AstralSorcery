/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.RenderingRegistry;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.block.BlockAttunementRelay;
import hellfirepvp.astralsorcery.common.block.BlockCustomName;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.IVariantTileProvider;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.BlockStateCheck;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.astralsorcery.common.util.struct.BlockArray;
import hellfirepvp.astralsorcery.common.util.struct.BlockDiscoverer;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockAltar
 * Created by HellFirePvP
 * Date: 01.08.2016 / 20:52
 */
public class BlockAltar extends BlockStarlightNetwork implements BlockCustomName {

    // private static final AxisAlignedBB boxDiscovery = new AxisAlignedBB( 1D / 16D, 0D, 1D / 16D, 15D / 16D, 15D /
    // 16D, 15D / 16D);
    private static final AxisAlignedBB boxAttenuation = AxisAlignedBB
        .getBoundingBox(-(8D / 16D), 0D, -(8D / 16D), 1D + (8D / 16D), 1D + (3D / 16D), 1D + (8D / 16D));
    private static final AxisAlignedBB boxConstellation = AxisAlignedBB
        .getBoundingBox(-(12D / 16D), 0D, -(12D / 16D), 1D + (12D / 16D), 1D + (8D / 16D), 1D + (12D / 16D));
    public static final AxisAlignedBB FULL_BLOCK_AABB = AxisAlignedBB
        .getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    // public static PropertyBool RENDER_FULLY = PropertyBool.create("render");
    // public static PropertyEnum<AltarType> ALTAR_TYPE = PropertyEnum.create("altartype", AltarType.class);

    public BlockAltar() {
        super("BlockAltar", Material.rock);
        setHardness(3.0F);
        setStepSound(soundTypeStone);
        setResistance(25.0F);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
        // setDefaultState(this.blockState.getBaseState().withProperty(ALTAR_TYPE, AltarType.ALTAR_1));
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            BlockPos pos = new BlockPos(x, y, z);
            TileAltar ta = MiscUtils.getTileAt(worldIn, pos, TileAltar.class, true);
            if (ta != null) {
                switch (ta.getAltarLevel()) {
                    case DISCOVERY:
                        AstralSorcery.proxy.openGui(
                            CommonProxy.EnumGuiId.ALTAR_DISCOVERY,
                            player,
                            worldIn,
                            pos.getX(),
                            pos.getY(),
                            pos.getZ());
                        return true;
                    case ATTUNEMENT:
                        AstralSorcery.proxy.openGui(
                            CommonProxy.EnumGuiId.ALTAR_ATTUNEMENT,
                            player,
                            worldIn,
                            pos.getX(),
                            pos.getY(),
                            pos.getZ());
                        return true;
                    case CONSTELLATION_CRAFT:
                        AstralSorcery.proxy.openGui(
                            CommonProxy.EnumGuiId.ALTAR_CONSTELLATION,
                            player,
                            worldIn,
                            pos.getX(),
                            pos.getY(),
                            pos.getZ());
                        return true;
                    case TRAIT_CRAFT:
                        AstralSorcery.proxy.openGui(
                            CommonProxy.EnumGuiId.ALTAR_TRAIT,
                            player,
                            worldIn,
                            pos.getX(),
                            pos.getY(),
                            pos.getZ());
                        return true;
                    case ENDGAME:
                        break;
                }
            }
        }
        return true;
    }

    // @Override
    // public Block getActualState(Block state, IBlockAccess worldIn, BlockPos pos) {
    // return state.withProperty(RENDER_FULLY, false);
    // }

    /*
     * @Override
     * @SideOnly(Side.CLIENT)
     * public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
     * RenderingUtils.playBlockBreakParticles(pos,
     * BlocksAS.blockMarble.getDefaultState()
     * .withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.RAW));
     * return true;
     * }
     * @Override
     * @SideOnly(Side.CLIENT)
     * public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
     * return true;
     * }
     */

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (AltarType type : AltarType.values()) {
            if (type == AltarType.ALTAR_4 || type == AltarType.ALTAR_5) continue;
            ItemStack stack = new ItemStack(item, 1, type.ordinal());
            NBTTagCompound pers = NBTHelper.getPersistentData(stack);
            pers.setInteger("exp", 0);
            pers.setInteger("lvl", type.ordinal());
            list.add(stack);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, int x, int y, int z) {
        startSearchForRelayUpdate(worldIn, x, y, z);
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);

        startSearchForRelayUpdate(worldIn, x, y, z);
    }

    public static void startSearchForRelayUpdate(World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        Thread searchThread = new Thread(() -> {
            BlockArray relaysAndAltars = BlockDiscoverer
                .searchForBlocksAround(world, pos, 16, new BlockStateCheck.Block(BlocksAS.attunementRelay));
            for (Map.Entry<BlockPos, BlockArray.BlockInformation> entry : relaysAndAltars.getPattern()
                .entrySet()) {
                BlockAttunementRelay.startSearchRelayLinkThreadAt(world, entry.getKey(), false);
            }
        });
        searchThread.setName("AttRelay UpdateFinder at " + pos.toString());
        searchThread.start();
    }

    // @Override
    // public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
    // /*
    // * TileAltar ta = MiscUtils.getTileAt(source, pos, TileAltar.class, true);
    // * if(ta != null) {
    // * TileAltar.AltarLevel al = ta.getAltarLevel();
    // * switch (al) {
    // * case DISCOVERY:
    // * return boxDiscovery;
    // * case ATTENUATION:
    // * return boxAttenuation;
    // * case CONSTELLATION_CRAFT:
    // * return boxConstellation;
    // * case TRAIT_CRAFT:
    // * break;
    // * case ENDGAME:
    // * break;
    // * }
    // * }
    // */
    // // AltarType type = state.getValue(ALTAR_TYPE);
    // // if(type != null) {
    // // AxisAlignedBB box = type.getBox();
    // // if(box != null) {
    // // return box;
    // // }
    // // }
    // return FULL_BLOCK_AABB;
    // }

    // @Override
    // public TileEntity createTileEntity(World world, int metadata) {
    // AltarType type = AltarType.values()[metadata];
    // if (type == null) return null;
    // return type.provideTileEntity(world, metadata);
    // }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        AltarType type = AltarType.values()[metadata];
        if (type == null) return null;
        return type.provideTileEntity(world, metadata);
    }
    // @Override
    // public Block getStateFromMeta(int meta) {
    // return meta < AltarType.values().length ? getDefaultState().withProperty(ALTAR_TYPE, AltarType.values()[meta]) :
    // getDefaultState();
    // }
    //
    // @Override
    // public int getMeta(Block state) {
    // AltarType type = state.getValue(ALTAR_TYPE);
    // return type == null ? 0 : type.ordinal();
    // }
    //
    // @Override
    // protected BlockStateContainer createBlockState() {
    // return new BlockStateContainer(this, ALTAR_TYPE, RENDER_FULLY);
    // }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        NBTTagCompound pers = NBTHelper.getPersistentData(stack);
        BlockPos pos = new BlockPos(x, y, z);
        int exp = pers.getInteger("exp");
        int lvl = pers.getInteger("lvl");
        TileAltar ta = MiscUtils.getTileAt(worldIn, pos, TileAltar.class, true);
        if (ta != null) {
            ta.onPlace(exp, TileAltar.AltarLevel.values()[lvl]);
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(worldIn, player, x, y, z, meta);
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (!worldIn.isRemote && te != null && te instanceof TileAltar) {
            ItemStack out = new ItemStack(BlocksAS.blockAltar, 1, damageDropped(meta));
            int exp = ((TileAltar) te).getExperience();
            int levelOrdinal = ((TileAltar) te).getAltarLevel()
                .ordinal();
            NBTTagCompound tag = NBTHelper.getPersistentData(out);
            tag.setInteger("exp", exp);
            tag.setInteger("lvl", levelOrdinal);
            ItemUtils.dropItemNaturally(worldIn, x + 0.5, y + 0.5, z + 0.5, out);
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        /*
         * ItemStack stack = new ItemStack(BlocksAS.blockAltar, 1, damageDropped(state));
         * TileAltar ta = MiscUtils.getTileAt(world, pos, TileAltar.class);
         * if(ta != null) {
         * int exp = ta.getExperience();
         * int levelOrdinal = ta.getAltarLevel().ordinal();
         * NBTTagCompound tag = ItemNBTHelper.getPersistentData(stack);
         * tag.setInteger("exp", exp);
         * tag.setInteger("lvl", levelOrdinal);
         * }
         */
        return new ArrayList<>();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        Block actState = world.getBlock(x, y, z);
        BlockPos pos = new BlockPos(x, y, z);
        ItemStack stack = super.getPickBlock(target, world, x, y, z, player); // Waila fix. wtf. why waila. why.
        TileAltar te = MiscUtils.getTileAt(world, pos, TileAltar.class, true);
        if (te != null) {
            int exp = te.getExperience();
            int levelOrdinal = te.getAltarLevel()
                .ordinal();
            NBTTagCompound tag = NBTHelper.getPersistentData(stack);
            tag.setInteger("exp", exp);
            tag.setInteger("lvl", levelOrdinal);
        }
        return stack;
    }

    // @Override
    // public Block getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float
    // hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
    // return getStateFromMeta(meta);
    // }

    // @Override

    public int getRenderType() {
        return RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    // @Override
    // public int getRenderType() {
    // return RenderingRegistry.getNextAvailableRenderId();
    // }

    @Override
    public String getIdentifierForMeta(int meta) {
        return AltarType.values()[meta].getName();
    }

    // @Override
    // public List<Block> getValidStates() {
    // List<Block> ret = new LinkedList<>();
    // for (AltarType type : AltarType.values()) {
    //// ret.add(getDefaultState().withProperty(ALTAR_TYPE, type));
    // }
    // return ret;
    // }
    //
    // @Override
    // public String getMetaName(int meta) {
    // return "";
    // }
    //
    // @Override
    // public int getMeta() {
    // return 0;
    // }

    public static enum AltarType implements IVariantTileProvider {

        ALTAR_1((world, metadata) -> new TileAltar(TileAltar.AltarLevel.DISCOVERY)),
        ALTAR_2((world, metadata) -> new TileAltar(TileAltar.AltarLevel.ATTUNEMENT)),
        ALTAR_3((world, metadata) -> new TileAltar(TileAltar.AltarLevel.CONSTELLATION_CRAFT)),
        ALTAR_4((world, metadata) -> new TileAltar(TileAltar.AltarLevel.TRAIT_CRAFT)),
        ALTAR_5((world, metadata) -> new TileAltar(TileAltar.AltarLevel.ENDGAME));

        // Ugly workaround to make constructors nicer
        private final IVariantTileProvider provider;

        private AltarType(IVariantTileProvider provider) {
            this.provider = provider;
        }

        @Override
        public TileEntity provideTileEntity(World world, int metadata) {
            return provider.provideTileEntity(world, metadata);
        }

        public String getName() {
            return name().toLowerCase();
        }

        @Override
        public String toString() {
            return getName();
        }

        public AxisAlignedBB getBox() {
            switch (this) {
                case ALTAR_1:
                    return FULL_BLOCK_AABB;
                case ALTAR_2:
                    return FULL_BLOCK_AABB;
                // return boxAttenuation;
                case ALTAR_3:
                    return FULL_BLOCK_AABB;
                // return boxConstellation;
                case ALTAR_4:
                    return null;
                case ALTAR_5:
                    return null;
            }
            return null;
        }
    }
}
