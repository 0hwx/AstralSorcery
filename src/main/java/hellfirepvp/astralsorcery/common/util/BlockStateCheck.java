/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockStateCheck
 * Created by HellFirePvP
 * Date: 17.10.2016 / 00:30
 */
public interface BlockStateCheck {

    public boolean isStateValid(World world, BlockPos pos, Block state);

    public static class Blockes implements BlockStateCheck {

        private final List<net.minecraft.block.Block> toCheck;

        public Blockes(net.minecraft.block.Block... toCheck) {
            this.toCheck = Lists.newArrayList(toCheck);
        }

        @Override
        public boolean isStateValid(World world, BlockPos pos, Block block) {
            return toCheck.contains(pos.getBlock(world));
        }
    }

    public static class Meta implements BlockStateCheck {

        private final int toCheck;
        private final net.minecraft.block.Block block;

        public Meta(net.minecraft.block.Block block, int toCheck) {
            this.toCheck = toCheck;
            this.block = block;
        }

        public AnyMeta copyWithAdditionalMeta(int add) {
            AnyMeta ret = new AnyMeta(this.block, Lists.newArrayList(toCheck));
            if (!ret.passableMetadataValues.contains(add)) {
                ret.passableMetadataValues.add(add);
            }
            return ret;
        }

        @Override
        public boolean isStateValid(World world, BlockPos pos, Block state) {
            return state.equals(block) && pos.getMetadata(world) == toCheck; // state.getBlock().getMeta(state) == toCheck;
        }
    }

    public static class AnyMeta implements BlockStateCheck {

        private final Collection<Integer> passableMetadataValues;
        private final net.minecraft.block.Block block;

        public AnyMeta(net.minecraft.block.Block block, int meta) {
            this(block, new int[] { meta });
        }

        public AnyMeta(net.minecraft.block.Block block, int... values) {
            this.passableMetadataValues = new ArrayList<>(values.length);
            for (int val : values) {
                this.passableMetadataValues.add(val);
            }
            this.block = block;
        }

        public AnyMeta(net.minecraft.block.Block block, Integer... values) {
            this.passableMetadataValues = Arrays.asList(values);
            this.block = block;
        }

        public AnyMeta(net.minecraft.block.Block block, Collection<Integer> passableMetadataValues) {
            this.passableMetadataValues = passableMetadataValues;
            this.block = block;
        }

        public AnyMeta copyWithAdditionalMeta(int add) {
            AnyMeta ret = new AnyMeta(this.block, this.passableMetadataValues);
            if (!ret.passableMetadataValues.contains(add)) {
                ret.passableMetadataValues.add(add);
            }
            return ret;
        }

        @Override
        public boolean isStateValid(World world, BlockPos pos, Block block) {
            return block.equals(block) && passableMetadataValues.contains(pos.getMetadata(world));
        }
    }

}
