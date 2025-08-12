/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Lists;

import hellfirepvp.astralsorcery.common.constellation.effect.CEffectPositionListGen;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: CropHelper
 * Created by HellFirePvP
 * Date: 08.11.2016 / 13:05
 */
// Intended for mostly Server-Side use
public class CropHelper {

    @Nullable
    public static GrowablePlant wrapPlant(World world, BlockPos pos) {
        Block block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
        if (block instanceof IGrowable) {
            if (block instanceof BlockGrass) return null;
            if (block instanceof BlockTallGrass) return null;
            if (block instanceof BlockDoublePlant) return null;
            return new GrowableWrapper(pos);
        }
        if (block.equals(Blocks.reeds)) {
            if (isReedBase(world, pos)) {
                return new GrowableReedWrapper(pos);
            }
        }
        if (block.equals(Blocks.cactus)) {
            if (isCactusBase(world, pos)) {
                return new GrowableCactusWrapper(pos);
            }
        }
        if (block.equals(Blocks.nether_wart)) {
            return new GrowableNetherwartWrapper(pos);
        }
        return null;
    }

    @Nullable
    public static HarvestablePlant wrapHarvestablePlant(World world, BlockPos pos) {
        GrowablePlant growable = wrapPlant(world, pos);
        if (growable == null) return null; // Every plant has to be growable.
        Block block = world.getBlock(
            growable.getPos()
                .getX(),
            growable.getPos()
                .getY(),
            growable.getPos()
                .getZ());
        if (block.equals(Blocks.reeds) && growable instanceof GrowableReedWrapper) {
            return (GrowableReedWrapper) growable;
        }
        if (block.equals(Blocks.cactus) && growable instanceof GrowableCactusWrapper) {
            return (GrowableCactusWrapper) growable;
        }
        if (block.equals(Blocks.nether_wart) && growable instanceof GrowableNetherwartWrapper) {
            return (GrowableNetherwartWrapper) growable;
        }
        if (block instanceof IPlantable) {
            return new HarvestableWrapper(pos);
        }
        return null;
    }

    private static boolean isReedBase(World world, BlockPos pos) {
        return !world.getBlock(
            pos.down()
                .getX(),
            pos.down()
                .getY(),
            pos.down()
                .getZ())
            .equals(Blocks.reeds);
    }

    private static boolean isCactusBase(World world, BlockPos pos) {
        return !world.getBlock(
            pos.down()
                .getX(),
            pos.down()
                .getY(),
            pos.down()
                .getZ())
            .equals(Blocks.cactus);
    }

    public static interface GrowablePlant extends CEffectPositionListGen.CEffectGenListEntry {

        public boolean isValid(World world, boolean forceChunkLoad);

        public boolean canGrow(World world);

        public boolean tryGrow(World world, Random rand);

    }

    public static interface HarvestablePlant extends GrowablePlant {

        public boolean canHarvest(World world);

        public List<ItemStack> harvestDropsAndReplant(World world, Random rand, int harvestFortune);

    }

    public static class HarvestableWrapper implements HarvestablePlant {

        private final BlockPos pos;

        public HarvestableWrapper(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public boolean canHarvest(World world) {
            Block at = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            if (!(at instanceof IGrowable)) return false;
            return !((IGrowable) at).func_149851_a(world, pos.getX(), pos.getY(), pos.getZ(), false);
        }

        @Override
        public List<ItemStack> harvestDropsAndReplant(World world, Random rand, int harvestFortune) {
            List<ItemStack> drops = Lists.newLinkedList();
            if (canHarvest(world)) {
                BlockPos pos = getPos();
                Block at = world.getBlock(getPos().getX(), getPos().getY(), getPos().getZ());
                if (at instanceof IPlantable) {
                    drops.addAll(
                        at.getDrops(world, pos.getX(), pos.getY(), pos.getZ(), pos.getMetadata(world), harvestFortune));
                    world.setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
                    world.setBlock(
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        ((IPlantable) at).getPlant(world, pos.getX(), pos.getY(), pos.getZ()));
                }
            }
            return drops;
        }

        @Override
        public BlockPos getPos() {
            return pos;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {}

        @Override
        public void writeToNBT(NBTTagCompound nbt) {}

        @Override
        public boolean isValid(World world, boolean forceChunkLoad) {
            if (!forceChunkLoad
                && !MiscUtils.isChunkLoaded(world, new ChunkCoordIntPair(getPos().chunkX(), getPos().chunkZ())))
                return true; // We stall until it's loaded.
            HarvestablePlant plant = wrapHarvestablePlant(world, getPos());
            return plant != null && plant instanceof HarvestableWrapper;
        }

        @Override
        public boolean canGrow(World world) {
            Block at = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            return at instanceof IGrowable
                && ((IGrowable) at).func_149851_a(world, pos.getX(), pos.getY(), pos.getZ(), false);
        }

        @Override
        public boolean tryGrow(World world, Random rand) {
            Block at = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            if (at instanceof IGrowable) {
                if (((IGrowable) at).func_149851_a(world, pos.getX(), pos.getY(), pos.getZ(), false)) {
                    ((IGrowable) at).func_149853_b(world, rand, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
            return false;
        }

    }

    public static class GrowableNetherwartWrapper implements HarvestablePlant {

        private final BlockPos pos;

        public GrowableNetherwartWrapper(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public boolean isValid(World world, boolean forceChunkLoad) {
            if (!forceChunkLoad && !MiscUtils.isChunkLoaded(world, new ChunkCoordIntPair(pos.chunkX(), pos.chunkZ())))
                return true; // We stall until it's loaded.
            return world.getBlock(pos.getX(), pos.getY(), pos.getZ())
                .equals(Blocks.nether_wart);
        }

        @Override
        public boolean canGrow(World world) {
            Block at = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            int metadata = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
            return at.equals(Blocks.nether_wart) && metadata < 3;
        }

        @Override
        public boolean tryGrow(World world, Random rand) {
            if (rand.nextBoolean()) {
                Block current = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
                int metadata = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
                world.setBlock(pos.getX(), pos.getY(), pos.getZ(), current, (Math.min(3, metadata + 1)), 3); // world.setBlockState(pos,
                                                                                                             // current.withProperty(BlockNetherWart.AGE,
                                                                                                             // (Math.min(3,
                                                                                                             // current.getValue(BlockNetherWart.AGE)
                                                                                                             // + 1))),
                                                                                                             // 3);
                return true;
            }
            return false;
        }

        @Override
        public boolean canHarvest(World world) {
            Block current = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            int metadata = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
            return current.equals(Blocks.nether_wart) && metadata >= 3;
        }

        @Override
        public List<ItemStack> harvestDropsAndReplant(World world, Random rand, int harvestFortune) {
            Block current = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            int metadata = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
            List<ItemStack> drops = current
                .getDrops(world, pos.getX(), pos.getY(), pos.getZ(), metadata, harvestFortune);
            world.setBlock(pos.getX(), pos.getY(), pos.getZ(), Blocks.nether_wart, 1, 3);
            return drops;
        }

        @Override
        public BlockPos getPos() {
            return pos;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {}

        @Override
        public void writeToNBT(NBTTagCompound nbt) {}

    }

    public static class GrowableCactusWrapper implements HarvestablePlant {

        private final BlockPos pos;

        public GrowableCactusWrapper(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public boolean canHarvest(World world) {
            return world.getBlock(
                pos.up()
                    .getX(),
                pos.up()
                    .getY(),
                pos.up()
                    .getZ())
                .equals(Blocks.cactus);
        }

        @Override
        public List<ItemStack> harvestDropsAndReplant(World world, Random rand, int harvestFortune) {
            List<ItemStack> drops = Lists.newLinkedList();
            for (int i = 2; i > 0; i--) {
                BlockPos bp = pos.up(i);
                Block at = world.getBlock(bp.getX(), bp.getY(), bp.getZ());
                int meta = world.getBlockMetadata(bp.getX(), bp.getY(), bp.getZ());
                if (at.equals(Blocks.cactus)) {
                    drops.addAll(at.getDrops(world, bp.getX(), bp.getY(), bp.getZ(), meta, harvestFortune));
                    world.setBlockToAir(bp.getX(), bp.getY(), bp.getZ());
                }
            }
            return drops;
        }

        @Override
        public boolean isValid(World world, boolean forceChunkLoad) {
            if (!forceChunkLoad && !MiscUtils.isChunkLoaded(world, new ChunkCoordIntPair(pos.chunkX(), pos.chunkZ())))
                return true; // We stall until it's loaded.
            return world.getBlock(pos.getX(), pos.getY(), pos.getZ())
                .equals(Blocks.cactus);
        }

        @Override
        public boolean canGrow(World world) {
            BlockPos cache = pos;
            for (int i = 1; i < 3; i++) {
                cache = cache.up();
                if (world.isAirBlock(cache.getX(), cache.getY(), cache.getZ())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean tryGrow(World world, Random rand) {
            BlockPos cache = pos;
            for (int i = 1; i < 3; i++) {
                cache = cache.up();
                if (world.isAirBlock(cache.getX(), cache.getY(), cache.getZ())) {
                    if (rand.nextBoolean()) {
                        world.setBlock(cache.getX(), cache.getY(), cache.getZ(), Blocks.cactus);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }

        @Override
        public BlockPos getPos() {
            return pos;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {}

        @Override
        public void writeToNBT(NBTTagCompound nbt) {}

    }

    public static class GrowableReedWrapper implements HarvestablePlant {

        private final BlockPos pos;

        public GrowableReedWrapper(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public boolean canHarvest(World world) {
            return world.getBlock(
                pos.up()
                    .getX(),
                pos.up()
                    .getY(),
                pos.up()
                    .getZ())
                .equals(Blocks.reeds);
        }

        @Override
        public List<ItemStack> harvestDropsAndReplant(World world, Random rand, int harvestFortune) {
            List<ItemStack> drops = Lists.newLinkedList();
            for (int i = 2; i > 0; i--) {
                BlockPos bp = pos.up(i);
                Block at = world.getBlock(bp.getX(), bp.getY(), bp.getZ());
                int meta = world.getBlockMetadata(bp.getX(), bp.getY(), bp.getZ());
                if (at.equals(Blocks.reeds)) {
                    drops.addAll(at.getDrops(world, bp.getX(), bp.getY(), bp.getZ(), meta, harvestFortune));
                    world.setBlockToAir(bp.getX(), bp.getY(), bp.getZ());
                }
            }
            return drops;
        }

        @Override
        public boolean isValid(World world, boolean forceChunkLoad) {
            if (!forceChunkLoad && !MiscUtils.isChunkLoaded(world, new ChunkCoordIntPair(pos.chunkX(), pos.chunkZ())))
                return true; // We stall until it's loaded.
            return world.getBlock(pos.getX(), pos.getY(), pos.getZ())
                .equals(Blocks.reeds);
        }

        @Override
        public boolean canGrow(World world) {
            BlockPos cache = pos;
            for (int i = 1; i < 3; i++) {
                cache = cache.up();
                if (world.isAirBlock(cache.getX(), cache.getY(), cache.getZ())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean tryGrow(World world, Random rand) {
            BlockPos cache = pos;
            for (int i = 1; i < 3; i++) {
                cache = cache.up();
                if (world.isAirBlock(cache.getX(), cache.getY(), cache.getZ())) {
                    if (rand.nextBoolean()) {
                        world.setBlock(cache.getX(), cache.getY(), cache.getZ(), Blocks.reeds);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }

        @Override
        public BlockPos getPos() {
            return pos;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {}

        @Override
        public void writeToNBT(NBTTagCompound nbt) {}

    }

    public static class GrowableWrapper implements GrowablePlant {

        private final BlockPos pos;

        public GrowableWrapper(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public BlockPos getPos() {
            return pos;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {}

        @Override
        public void writeToNBT(NBTTagCompound nbt) {}

        @Override
        public boolean isValid(World world, boolean forceChunkLoad) {
            if (!forceChunkLoad && !MiscUtils.isChunkLoaded(world, new ChunkCoordIntPair(pos.chunkX(), pos.chunkZ())))
                return true; // We stall until it's loaded.
            GrowablePlant res = wrapPlant(world, pos);
            return res != null && res instanceof GrowableWrapper;
        }

        @Override
        public boolean canGrow(World world) {
            Block at = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            return at instanceof IGrowable
                && (((IGrowable) at).func_149851_a(world, pos.getX(), pos.getY(), pos.getZ(), false)
                    || (at instanceof BlockStem && !stemHasCrop(world)));
        }

        private boolean stemHasCrop(World world) {
            for (ForgeDirection enumfacing : ForgeDirection.VALID_DIRECTIONS) {
                BlockPos pos = this.pos.offset(enumfacing);
                Block offset = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
                if (offset.equals(Blocks.melon_block) || offset.equals(Blocks.pumpkin)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean tryGrow(World world, Random rand) {
            Block at = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            if (at instanceof IGrowable) {
                if (((IGrowable) at).func_149851_a(world, pos.getX(), pos.getY(), pos.getZ(), false)) {
                    if (!((IGrowable) at).func_149852_a(world, rand, pos.getX(), pos.getY(), pos.getZ())) {
                        if (world.rand.nextInt(40) != 0) return true; // Returning true to say it could've been
                                                                      // potentially grown - So this doesn't invalidate
                                                                      // caches.
                    }
                    ((IGrowable) at).func_149853_b(world, rand, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
                if (at instanceof BlockStem) {
                    for (int i = 0; i < 10; i++) {
                        at.updateTick(world, pos.getX(), pos.getY(), pos.getZ(), rand);
                    }
                    return true;
                }
            }
            return false;
        }
    }

}
