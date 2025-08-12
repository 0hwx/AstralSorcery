/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util.struct;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

import hellfirepvp.astralsorcery.common.block.BlockStructural;
import hellfirepvp.astralsorcery.common.item.ISpecialStackDescriptor;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.BlockStateCheck;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockArray
 * Created by HellFirePvP
 * Date: 30.07.2016 / 16:23
 */
public class BlockArray {

    protected static final Random STATIC_RAND = new Random();

    protected Map<BlockPos, TileEntityCallback> tileCallbacks = new HashMap<>();
    protected Map<BlockPos, BlockInformation> pattern = new HashMap<>();
    private Vec3 min = Vec3.createVectorHelper(0, 0, 0);
    private Vec3 max = Vec3.createVectorHelper(0, 0, 0);
    private Vec3 size = Vec3.createVectorHelper(0, 0, 0);

    public void addBlock(int x, int y, int z, @Nonnull Block block) {
        addBlock(new BlockPos(x, y, z), block, 0);
    }

    public void addBlock(int x, int y, int z, @Nonnull Block block, int metadata) {
        addBlock(new BlockPos(x, y, z), block, metadata);
    }

    public void addBlock(BlockPos offset, @Nonnull Block block, int metadata) {
        pattern.put(offset, new BlockInformation(block, metadata));
        updateSize(offset);
    }

    public void addBlock(int x, int y, int z, @Nonnull Block block, int metadata, BlockStateCheck match) {
        addBlock(new BlockPos(x, y, z), block, metadata, match);
    }

    public void addBlock(BlockPos offset, @Nonnull Block block, int metadata, BlockStateCheck match) {
        pattern.put(offset, new BlockInformation(block, metadata, match));
        updateSize(offset);
    }

    public void addAll(BlockArray other) {
        addAll(other, null);
    }

    public void addAll(BlockArray other, @Nullable Function<BlockPos, BlockPos> positionTransform) {
        for (Map.Entry<BlockPos, BlockInformation> patternEntry : other.pattern.entrySet()) {
            BlockPos to = patternEntry.getKey();
            if (positionTransform != null) {
                to = positionTransform.apply(to);
            }
            pattern.put(to, patternEntry.getValue());
            updateSize(to);
        }
        for (Map.Entry<BlockPos, TileEntityCallback> patternEntry : other.tileCallbacks.entrySet()) {
            BlockPos to = patternEntry.getKey();
            if (positionTransform != null) {
                to = positionTransform.apply(to);
            }
            tileCallbacks.put(to, patternEntry.getValue());
        }
    }

    public void addTileCallback(BlockPos pos, TileEntityCallback callback) {
        tileCallbacks.put(pos, callback);
    }

    public boolean hasBlockAt(BlockPos pos) {
        return pattern.containsKey(pos);
    }

    public boolean isEmpty() {
        return pattern.isEmpty();
    }

    public Vec3 getMax() {
        return max;
    }

    public Vec3 getMin() {
        return min;
    }

    public Vec3 getSize() {
        return size;
    }

    private void updateSize(BlockPos addedPos) {
        if (addedPos.getX() < min.xCoord) {
            min = Vec3.createVectorHelper(addedPos.getX(), min.yCoord, min.zCoord);
        }
        if (addedPos.getX() > max.xCoord) {
            max = Vec3.createVectorHelper(addedPos.getX(), max.yCoord, max.zCoord);
        }
        if (addedPos.getY() < min.yCoord) {
            min = Vec3.createVectorHelper(min.xCoord, addedPos.getY(), min.zCoord);
        }
        if (addedPos.getY() > max.yCoord) {
            max = Vec3.createVectorHelper(max.xCoord, addedPos.getY(), max.zCoord);
        }
        if (addedPos.getZ() < min.zCoord) {
            min = Vec3.createVectorHelper(min.xCoord, min.yCoord, addedPos.getZ());
        }
        if (addedPos.getZ() > max.zCoord) {
            max = Vec3.createVectorHelper(max.xCoord, max.yCoord, addedPos.getZ());
        }
        size = Vec3
            .createVectorHelper(max.xCoord - min.xCoord + 1, max.yCoord - min.yCoord + 1, max.zCoord - min.zCoord + 1);
    }

    public Map<BlockPos, BlockInformation> getPattern() {
        return pattern;
    }

    public Map<BlockPos, TileEntityCallback> getTileCallbacks() {
        return tileCallbacks;
    }

    public void addBlockCube(@Nonnull Block block, int metadata, int ox, int oy, int oz, int tx, int ty, int tz) {
        addBlockCube(block, metadata, new BlockStateCheck.Meta(block, metadata), ox, oy, oz, tx, ty, tz);
    }

    public void addBlockCube(@Nonnull Block block, int ox, int oy, int oz, int tx, int ty, int tz) {
        addBlockCube(block, 0, new BlockStateCheck.Meta(block, 0), ox, oy, oz, tx, ty, tz);
    }

    public void addBlockCube(@Nonnull Block block, int metadata, BlockStateCheck match, int ox, int oy, int oz, int tx,
        int ty, int tz) {
        int lx, ly, lz;
        int hx, hy, hz;
        if (ox < tx) {
            lx = ox;
            hx = tx;
        } else {
            lx = tx;
            hx = ox;
        }
        if (oy < ty) {
            ly = oy;
            hy = ty;
        } else {
            ly = ty;
            hy = oy;
        }
        if (oz < tz) {
            lz = oz;
            hz = tz;
        } else {
            lz = tz;
            hz = oz;
        }

        for (int xx = lx; xx <= hx; xx++) {
            for (int zz = lz; zz <= hz; zz++) {
                for (int yy = ly; yy <= hy; yy++) {
                    addBlock(new BlockPos(xx, yy, zz), block, metadata, match);
                }
            }
        }
    }

    public Map<BlockPos, BlockInformation> placeInWorld(World world, BlockPos center) {
        Map<BlockPos, BlockInformation> result = new HashMap<>();
        for (Map.Entry<BlockPos, BlockInformation> entry : pattern.entrySet()) {
            BlockInformation info = entry.getValue();
            BlockPos at = center.add(entry.getKey());
            world.setBlock(at.getX(), at.getY(), at.getZ(), info.type, info.metadata, 3);
            result.put(at, info);

            if (info.type instanceof BlockLiquid || info.type instanceof BlockFluidBase) {
                world.notifyBlockChange(at.getX(), at.getY(), at.getZ(), info.type);
            }

            TileEntity placed = world.getTileEntity(at.getX(), at.getY(), at.getZ());
            if (tileCallbacks.containsKey(entry.getKey())) {
                TileEntityCallback callback = tileCallbacks.get(entry.getKey());
                if (callback.isApplicable(placed)) {
                    callback.onPlace(world, at, placed);
                }
            }
        }
        return result;
    }

    public List<ItemStack> getAsDescriptiveStacks() {
        List<ItemStack> out = new LinkedList<>();
        for (BlockInformation info : pattern.values()) {
            int meta = info.metadata;
            ItemStack s = null;
            if (info.type instanceof BlockFluidBase) {
                // Skip fluids for now in 1.7.10 - bucket handling is different
                // s = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket,
                // ((BlockFluidBase) info.type).getFluid());
                continue;
            } else if (info.type instanceof BlockStructural) {
                continue;
                // Skip structural blocks for now
                // IBlockState otherState = info.state.getValue(BlockStructural.BLOCK_TYPE).getSupportedState();
                // Item i = Item.getItemFromBlock(otherState.getBlock());
                // if(i == null) continue;
                // s = new ItemStack(i, 1, otherState.getBlock().getMetaFromState(otherState));
            } else if (info.type instanceof ISpecialStackDescriptor) {
                s = ((ISpecialStackDescriptor) info.type).getDecriptor(info.type, meta);
            } else {
                Item i = Item.getItemFromBlock(info.type);
                if (i == null) continue;
                s = new ItemStack(i, 1, meta);
            }
            if (s != null && s.getItem() != null) {
                boolean found = false;
                for (ItemStack stack : out) {
                    if (stack.getItem() == s.getItem() && stack.getItemDamage() == s.getItemDamage()) {
                        stack.stackSize += 1;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    out.add(s);
                }
            }
        }
        return out;
    }

    public static interface TileEntityCallback {

        public boolean isApplicable(TileEntity te);

        public void onPlace(IBlockAccess access, BlockPos at, TileEntity te);

    }

    public static class BlockInformation {

        public final Block type;
        public final int metadata;
        public final BlockStateCheck matcher;

        protected BlockInformation(Block type, int metadata) {
            this(type, metadata, new BlockStateCheck.Meta(type, metadata));
        }

        protected BlockInformation(Block type, int metadata, BlockStateCheck matcher) {
            this.type = type;
            this.metadata = metadata;
            this.matcher = matcher;
        }

    }

}
