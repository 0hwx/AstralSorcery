/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.tile.TileStructuralConnector;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockStructural
 * Created by HellFirePvP
 * Date: 30.07.2016 / 21:50
 */
public class BlockStructural extends BlockContainer implements BlockCustomName {

//    public static PropertyEnum<BlockType> BLOCK_TYPE = PropertyEnum.create("blocktype", BlockType.class);
    public static final AxisAlignedBB FULL_BLOCK_AABB = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockStructural() {
        super(Material.air);
        setBlockUnbreakable();
        setBlockName("BlockStructural");
//        setSoundType(SoundType.GLASS);
    }

//    @Override
//    public SoundType getSoundType(Block state, World world, BlockPos pos, @Nullable Entity entity) {
//        switch (state.getValue(BLOCK_TYPE)) {
//            case TELESCOPE_STRUCT:
//                Block downState = world.getBlockState(pos.down());
//                return BlockType.TELESCOPE_STRUCT.getblock().getBlock().getSoundType(downState, world, pos, entity);
//            //case ATTUNEMENT_ALTAR_STRUCT:
//            //    downState = world.getBlockState(pos.down());
//            //    return BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock().getBlock().getSoundType(downState, world, pos, entity);
//        }
//        return super.getSoundType(state, world, pos, entity);
//    }

    @Override
    public String getHarvestTool(int metadata) {
        return super.getHarvestTool(metadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        BlockPos pos = new BlockPos(x, y, z).down();
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                BlockType.TELESCOPE_STRUCT.getblock().addDestroyEffects(world, pos.getX(), pos.getY(), pos.getZ(),meta, effectRenderer);
                return true;
            //case ATTUNEMENT_ALTAR_STRUCT:
            //    BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock().getBlock().addDestroyEffects(world, pos.down(), manager);
            //    return true;
        }
        return super.addDestroyEffects(world, x, y, z, meta, effectRenderer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        int meta = worldIn.getBlockMetadata(x, y, z);
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                return AxisAlignedBB.getBoundingBox(0, -1, 0, 1, 1, 1);
            //case ATTUNEMENT_ALTAR_STRUCT:
            //    return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
        }
        return FULL_BLOCK_AABB;
    }



    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (BlockType bt : BlockType.values()) {
            list.add(new ItemStack(item, 1, bt.ordinal()));
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
       BlockPos pos = new BlockPos(x, y, z).down();
        int meta = worldIn.getBlockMetadata(x, y, z);
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                return BlockType.TELESCOPE_STRUCT.getblock().onBlockActivated(worldIn, pos.getX(), pos.getY(), pos.getZ(), player, side, hitX, hitY, hitZ);
            //case ATTUNEMENT_ALTAR_STRUCT:
            //    return BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock().getBlock().onBlockActivated(worldIn, pos.down(), BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock(), playerIn, hand, heldItem, side, hitX, hitY, hitZ);
        }
        return super.onBlockActivated(worldIn, x, y, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
        List<ItemStack> out = new LinkedList<>();
        int meta = world.getBlockMetadata(x, y, z);
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                out.add(BlockMachine.MachineType.TELESCOPE.asStack());
                break;
            ///case ATTUNEMENT_ALTAR_STRUCT:
            ///    out.add(new ItemStack(BlocksAS.attunementAltar));
            ///    break;
        }
        return (ArrayList<ItemStack>) out;
    }

    @Override
    public float getBlockHardness(World worldIn, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        int meta = worldIn.getBlockMetadata(x, y, z);
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                return BlockType.TELESCOPE_STRUCT.getblock().getBlockHardness(worldIn, pos.down().getX(), pos.down().getY(), pos.down().getZ());
            //case ATTUNEMENT_ALTAR_STRUCT:
            //    return BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock().getBlockHardness(worldIn, pos.down());
        }
        return super.getBlockHardness( worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
        BlockPos pos = new BlockPos(x, y, z).down();
        int meta = world.getBlockMetadata(x, y, z);
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                return BlockType.TELESCOPE_STRUCT.getblock().getBlockHardness(world, pos.getX(), pos.getY(), pos.getZ());
            //case ATTUNEMENT_ALTAR_STRUCT:
            //    return BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock().getBlockHardness(world, pos.down());
        }
        return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        BlockPos pos = new BlockPos(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                return BlockType.TELESCOPE_STRUCT.getblock().getPickBlock(target, world, pos.down().getX(), pos.down().getY(), pos.down().getZ(), player);
            //case ATTUNEMENT_ALTAR_STRUCT:
            //    return BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock().getBlock().getPickBlock(BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock(), target, world, pos.down(), player);
        }
        return super.getPickBlock(target, world, pos.getX(), pos.getY(), pos.getZ(), player);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor) {
        int meta = worldIn.getBlockMetadata(x, y, z);
        BlockPos pos = new BlockPos(x, y, z);
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                if(worldIn.isAirBlock(pos.down().getX(), pos.down().getY(), pos.down().getZ())) {
                    worldIn.setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
                }
                break;
            //case ATTUNEMENT_ALTAR_STRUCT:
            //    if(world.isAirBlock(pos.down())) {
            //        world.setBlockToAir(pos);
            //    }
            //    break;
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        if(!(world instanceof World)) {
            super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
            return;
        }
        BlockPos pos = new BlockPos(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                if(world.isAirBlock(pos.down().getX(), pos.down().getY(), pos.down().getZ())) {
                    ((World) world).setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
                }
                break;
            //case ATTUNEMENT_ALTAR_STRUCT:
            //    if(world.isAirBlock(pos.down())) {
            //        ((World) world).setBlockToAir(pos);
            //    }
            //    break;
        }
    }

//    @Override
//    public Block getStateFromMeta(int meta) {
//        return meta < BlockType.values().length ? getDefaultState().withProperty(BLOCK_TYPE, BlockType.values()[meta]) : getDefaultState();
//    }

//    @Override
//    public int getMeta(Block state) {
//        BlockType type = state.getValue(BLOCK_TYPE);
//        return type == null ? 0 : type.ordinal();
//    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, BLOCK_TYPE);
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public boolean isTranslucent(Block state) {
//        return true;
//    }

    @Override
    public boolean isOpaqueCube() {
        return false; //state.getValue(BLOCK_TYPE).getblock().isOpaqueCube();
    }

    @Override
    public boolean isNormalCube() {
        return false; // state.getValue(BLOCK_TYPE).getblock().isFullCube();
    }

//    @Override
//    public boolean isNormalCube() {
//        switch (state.getValue(BLOCK_TYPE)) {
//            case TELESCOPE_STRUCT:
//                return BlockType.TELESCOPE_STRUCT.getblock().isNormalCube();
//            //case ATTUNEMENT_ALTAR_STRUCT:
//            //    return BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock().isNormalCube();
//        }
//        return super.isNormalCube(state);
//    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        int meta = world.getBlockMetadata(x, y, z);
        BlockPos pos = new BlockPos(x, y, z).down();
        BlockType type = BlockType.values()[meta];
        switch (type) {
            case TELESCOPE_STRUCT:
                return BlockType.TELESCOPE_STRUCT.getblock().isSideSolid(world, pos.getX(), pos.getY(), pos.getZ(), side);
            //case ATTUNEMENT_ALTAR_STRUCT:
            //    return BlockType.ATTUNEMENT_ALTAR_STRUCT.getblock().isSideSolid(world, pos.down(), side);
        }
        return super.isSideSolid(world, pos.getX(), pos.getY(), pos.getZ(), side);
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    @Override
    public String getIdentifierForMeta(int meta) {
        return BlockType.values()[meta].getName();
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List<net.minecraft.util.AxisAlignedBB> list, Entity collider) {
//        Block block = worldIn.getBlockState(pos);
//        BlockType bt = state.getValue(BLOCK_TYPE);
//        if(bt.equals(BlockType.ATTUNEMENT_ALTAR_STRUCT)) return;

        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileStructuralConnector();
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileStructuralConnector();
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    public static enum BlockType {

        TELESCOPE_STRUCT(BlocksAS.blockMachine,0);
        //ATTUNEMENT_ALTAR_STRUCT(BlocksAS.attunementAltar.getDefaultState());

        private final Block block;
        private final int meta;
        private BlockType(Block block, int meta) {
            this.block = block;
            this.meta =meta;
        }

        public Block getblock() {
            return block;
        }

        public int getMeta(){
            return meta;
        }

        public String getName() {
            return name().toLowerCase();
        }

    }

}
