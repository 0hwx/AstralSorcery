/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.base;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.util.ItemUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: LightOreTransmutations
 * Created by HellFirePvP
 * Date: 30.01.2017 / 12:30
 */
public class LightOreTransmutations {

    public static List<Transmutation> mtTransmutations = new LinkedList<>(); // Minetweaker cache
    private static List<Transmutation> registeredTransmutations = new LinkedList<>();

    private static List<Transmutation> localFallback = new LinkedList<>();

    public static void init() {
        // registerTransmutation(new Transmutation(Blocks.MAGMA,Blocks.obsidian,400.0D));
        registerTransmutation(new Transmutation(Blocks.sand, Blocks.clay, 400.0D));
        registerTransmutation(new Transmutation(Blocks.diamond_ore, Blocks.emerald_ore, 1000.0D));
        registerTransmutation(new Transmutation(Blocks.nether_wart, Blocks.soul_sand, 200.0D));
        // registerTransmutation(new Transmutation(Blocks.SEA_LANTERN.getDefaultState(),
        // Blocks.LAPIS_BLOCK.getDefaultState(), 200.0D));
        registerTransmutation(new Transmutation(Blocks.sandstone, Blocks.end_stone, 200.0D));
        registerTransmutation(new Transmutation(Blocks.netherrack, Blocks.nether_brick, 200.0D));

        registerTransmutation(new Transmutation(Blocks.iron_ore, BlocksAS.customOre, 100));
        registerTransmutation(
            new Transmutation(
                Blocks.pumpkin,
                Blocks.cake,
                new ItemStack(Blocks.pumpkin),
                new ItemStack(Items.cake),
                600.0D));

        cacheLocalFallback();
    }

    private static void cacheLocalFallback() {
        if (localFallback.isEmpty()) {
            localFallback.addAll(registeredTransmutations);
        }
    }

    public static void loadFromFallback() {
        registeredTransmutations.clear();
        registeredTransmutations.addAll(localFallback);
    }

    public static Transmutation tryRemoveTransmutation(ItemStack outRemove, boolean matchMeta) {
        Block b = Block.getBlockFromItem(outRemove.getItem());
        if (b != null) {
            for (Transmutation tr : registeredTransmutations) {
                if (tr.output.equals(b)) {

                    if (!matchMeta) { // || tr.output.getMeta(tr.output) == outRemove.getItemDamage()) {
                        registeredTransmutations.remove(tr);
                        return tr;
                    }
                }
            }
        }
        for (Transmutation tr : registeredTransmutations) {
            if (tr.outStack != null && ItemUtils.matchStackLoosely(tr.outStack, outRemove)) {
                registeredTransmutations.remove(tr);
                return tr;
            }
        }
        return null;
    }

    // Will return itself if successful.
    @Nullable
    public static Transmutation registerTransmutation(Transmutation tr) {
        for (Transmutation t : registeredTransmutations) {
            if (t.input.equals(tr.input)) {
                AstralSorcery.log
                    .warn("Tried to register Transmutation that has the same input as an already existing one.");
                return null;
            }
        }
        if (tr.input == null) {
            AstralSorcery.log.warn("Tried to register Transmutation with null input - Skipping!");
            return null;
        }
        if (tr.input.equals(Blocks.crafting_table)) {
            AstralSorcery.log.warn(
                "Cannot register Transmutation of iron workbench -> something. By default occupied by general crafting which is handled differently.");
            return null;
        }
        if (tr.output == null) {
            AstralSorcery.log.warn("Tried to register Transmutation with null output - Skipping!");
            return null;
        }
        registeredTransmutations.add(tr);
        return tr;
    }

    public static List<Transmutation> getRegisteredTransmutations() {
        return Collections.unmodifiableList(registeredTransmutations);
    }

    @Nullable
    public static Transmutation searchForTransmutation(Block tryStateIn) {
        for (Transmutation tr : registeredTransmutations) {
            if (tr.input.equals(tryStateIn)) return tr;
        }
        for (Transmutation tr : mtTransmutations) {
            if (tr.input.equals(tryStateIn)) return tr;
        }
        return null;
    }

    public static class Transmutation {

        public final Block input;
        public final Block output;
        public final double cost;

        @Nullable
        public final ItemStack outStack;
        @Nullable
        public final ItemStack inStack;

        public Transmutation(Block input, Block output, double cost) {
            this(input, output, null, null, cost);
        }

        public Transmutation(Block input, Block output, @Nullable ItemStack inputDisplay,
            @Nullable ItemStack outputDisplay, double cost) {
            this.input = input;
            this.output = output;
            this.cost = cost;
            this.outStack = outputDisplay;
            this.inStack = inputDisplay;
        }

        @Nullable
        public ItemStack getInputDisplayStack() {
            if (inStack != null) {
                return inStack.copy();
            }
            return ItemUtils.createBlockStack(input);
        }

        @Nullable
        public ItemStack getOutputDisplayStack() {
            if (outStack != null) {
                return outStack.copy();
            }
            return ItemUtils.createBlockStack(output);
        }
    }

}
