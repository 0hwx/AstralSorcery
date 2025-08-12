/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.base;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import hellfirepvp.astralsorcery.common.constellation.effect.GenListEntries;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.BlockStateCheck;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: WorldMeltables
 * Created by HellFirePvP
 * Date: 31.10.2016 / 22:49
 */
public enum WorldMeltables implements MeltInteraction {

    COBBLE(new BlockStateCheck.Block(Blocks.cobblestone), Blocks.flowing_lava, 180),
    STONE(new BlockStateCheck.Block(Blocks.stone), Blocks.flowing_lava, 100),
    OBSIDIAN(new BlockStateCheck.Block(Blocks.obsidian), Blocks.flowing_lava, 75),
    NETHERRACK(new BlockStateCheck.Block(Blocks.netherrack), Blocks.flowing_lava, 40),
    NETHERBRICK(new BlockStateCheck.Block(Blocks.nether_brick), Blocks.flowing_lava, 60),
    // MAGMA( new BlockStateCheck.Blockes(Blocks.MAGMA), Blocks.FLOWING_LAVA.getDefaultState(), 1),
    ICE(new BlockStateCheck.Block(Blocks.ice), Blocks.flowing_water, 1),;
    // FROSTED_ICE(new BlockStateCheck.Blockes(Blocks.FROSTED_ICE), Blocks.FLOWING_WATER.getDefaultState(), 1),
    // PACKED_ICE( new BlockStateCheck.Blockes(Blocks.PACKED_ICE), Blocks.FLOWING_WATER.getDefaultState(), 2);

    private final BlockStateCheck meltableCheck;
    private final Block meltResult;
    private final int meltDuration;

    private WorldMeltables(BlockStateCheck meltableCheck, Block meltResult, int meltDuration) {
        this.meltableCheck = meltableCheck;
        this.meltResult = meltResult;
        this.meltDuration = meltDuration;
    }

    @Override
    public boolean isMeltable(World world, BlockPos pos, Block worldState) {
        return meltableCheck.isStateValid(world, pos.getX(), pos.getY(), pos.getZ(), worldState);
    }

    @Override
    @Nullable
    public Block getMeltResultState() {
        return meltResult;
    }

    @Override
    @Nullable
    public ItemStack getMeltResultStack() {
        return null;
    }

    @Override
    public int getMeltTickDuration() {
        return meltDuration;
    }

    @Nullable
    public static MeltInteraction getMeltable(World world, BlockPos pos) {
        Block state = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
        for (WorldMeltables melt : values()) {
            if (melt.isMeltable(world, pos, state)) return melt;
        }
        ItemStack stack = ItemUtils.createBlockStack(state);
        if (stack != null && stack.getItem() != null) {
            ItemStack out = FurnaceRecipes.smelting()
                .getSmeltingResult(stack);
            if (out != null && out.getItem() != null) {
                return new FurnaceRecipeInteraction(state, out);
            }
        }
        return null;
    }

    public static class ActiveMeltableEntry extends GenListEntries.CounterListEntry {

        public ActiveMeltableEntry(BlockPos pos) {
            super(pos);
        }

        public boolean isValid(World world, boolean forceLoad) {
            if (!forceLoad
                && !MiscUtils.isChunkLoaded(world, new ChunkCoordIntPair(getPos().chunkX(), getPos().chunkZ())))
                return true;
            return getMeltable(world) != null;
        }

        public MeltInteraction getMeltable(World world) {
            return WorldMeltables.getMeltable(world, getPos());
        }

    }

    public static class FurnaceRecipeInteraction implements MeltInteraction {

        private final ItemStack out;
        private final BlockStateCheck.Meta matchInState;

        public FurnaceRecipeInteraction(Block inState, ItemStack outStack) {
            this.matchInState = new BlockStateCheck.Meta(inState, inState.damageDropped(outStack.getItemDamage()));
            this.out = outStack;
        }

        @Override
        public boolean isMeltable(World world, BlockPos pos, Block state) {
            return matchInState.isStateValid(world, pos.getX(), pos.getY(), pos.getZ(), state);
        }

        @Nullable
        @Override
        public Block getMeltResultState() {
            return ItemUtils.createBlockState(out);
        }

        @Nullable
        @Override
        public ItemStack getMeltResultStack() {
            return out;
        }

    }

}
