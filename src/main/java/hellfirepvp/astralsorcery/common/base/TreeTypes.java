package hellfirepvp.astralsorcery.common.base;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.BlockStateCheck;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TreeTypes
 * Created by HellFirePvP
 * Date: 11.03.2017 / 12:01
 */
public enum TreeTypes {

    OAK("minecraft", "log", "leaves", "sapling", new int[] {0, 4, 8, 12}, new int[] {0, 4, 8, 12}, 0),
    SPRUCE("minecraft", "log", "leaves", "sapling", new int[] {1, 5, 9, 13}, new int[] {1, 5, 9, 13}, 1),
    BIRCH("minecraft", "log", "leaves", "sapling", new int[] {2, 6, 10, 14}, new int[] {2, 6, 10, 14}, 2),
    JUNGLE("minecraft", "log", "leaves", "sapling", new int[] {3, 7, 11, 15}, new int[] {3, 7, 11, 15}, 3),
    ACACIA("minecraft", "log2", "leaves2", "sapling", new int[] {0, 4, 8, 12}, new int[] {0, 4, 8, 12}, 4),
    DARK_OAK("minecraft", "log2", "leaves2", "sapling", new int[] {1, 5, 9, 13}, new int[] {1, 5, 9, 13}, 5),

    SLIME(Mods.TICONSTRUCT, "slime_congealed", "slime_leaves", "slime_sapling", null, null, null);

    private String parentModId;
    private ResourceLocation resBlockName;
    private ResourceLocation resLeavesName;
    private ResourceLocation resSaplingName;

    private boolean exists = false;

    private Collection<Block> logStates = Lists.newArrayList();
    private Collection<Block> leaveStates = Lists.newArrayList();
    private Block saplingState = null;

    private BlockStateCheck logCheck;
    private BlockStateCheck leavesCheck;
    private BlockStateCheck saplingCheck;

    TreeTypes(Mods parentMod, String resBlockName, String resLeavesName, String resSaplingName, @Nullable int[] logMeta, @Nullable int[] leaveMeta, @Nullable Integer saplingMeta) {
        this(parentMod.modId, new ResourceLocation(parentMod.modId, resBlockName), new ResourceLocation(parentMod.modId, resLeavesName), new ResourceLocation(parentMod.modId, resSaplingName), logMeta, leaveMeta, saplingMeta);
    }

    TreeTypes(Mods parentMod, ResourceLocation resBlockName, ResourceLocation resLeavesName, ResourceLocation resSaplingName, @Nullable int[] logMeta, @Nullable int[] leaveMeta, @Nullable Integer saplingMeta) {
        this(parentMod.modId, resBlockName, resLeavesName, resSaplingName, logMeta, leaveMeta, saplingMeta);
    }

    TreeTypes(String parentModId, String resBlockName, String resLeavesName, String resSaplingName, @Nullable int[] logMeta, @Nullable int[] leaveMeta, @Nullable Integer saplingMeta) {
        this(parentModId, new ResourceLocation(parentModId, resBlockName), new ResourceLocation(parentModId, resLeavesName), new ResourceLocation(parentModId, resSaplingName), logMeta, leaveMeta, saplingMeta);
    }

    TreeTypes(String parentModId, ResourceLocation resBlockName, ResourceLocation resLeavesName, ResourceLocation resSaplingName, @Nullable int[] logMeta, @Nullable int[] leaveMeta, @Nullable Integer saplingMeta) {
        this.parentModId = parentModId;
        this.resBlockName = resBlockName;
        this.resLeavesName = resLeavesName;
        this.resSaplingName = resSaplingName;

        load(logMeta, leaveMeta, saplingMeta);
    }

    private void load(@Nullable int[] logMeta, @Nullable int[] leaveMeta, @Nullable Integer saplingMeta) {
        if (!Loader.isModLoaded(this.parentModId) && !this.parentModId.equals("minecraft")) {
            AstralSorcery.log.info("Not loading tree type " + name() + " as the mod " + this.parentModId + " is not loaded.");
            return;
        }

        // Use GameRegistry.findBlock() to find blocks by ResourceLocation in Minecraft 1.7.10
        Block log = GameRegistry.findBlock(this.parentModId, resBlockName.getResourcePath());
        Block leaf = GameRegistry.findBlock(this.parentModId, resLeavesName.getResourcePath());
        Block sapling = GameRegistry.findBlock(this.parentModId, resSaplingName.getResourcePath());

        if (isEmpty(log) || isEmpty(leaf) || isEmpty(sapling)) {
            AstralSorcery.log.info("Not loading tree type " + name() + " as its blocks don't exist in the currently loaded mods.");
            return;
        }

        logCheck = logMeta == null ? new BlockStateCheck.Blockes(log) : new BlockStateCheck.AnyMeta(log, logMeta);
        leavesCheck = leaveMeta == null ? new BlockStateCheck.Blockes(leaf) : new BlockStateCheck.AnyMeta(leaf, leaveMeta);
        saplingCheck = saplingMeta == null ? new BlockStateCheck.Blockes(sapling) : new BlockStateCheck.Meta(sapling, saplingMeta);

        if (logMeta == null) {
            this.logStates.add(log);
        } else {
            for (int m : logMeta) {
                Block block = log;
                int meta = block.damageDropped(m);
                if (!this.logStates.contains(meta)) {
                    this.logStates.add(block);
                }
            }
        }

        if (leaveMeta == null) {
            this.leaveStates.add(leaf);
        } else {
            for (int m : leaveMeta) {
                Block block = leaf;
                int meta = block.damageDropped(m);
                if (!this.leaveStates.contains(meta)) {
                    this.leaveStates.add(block);
                }
            }
        }

        if (saplingMeta == null) {
            this.saplingState = sapling;
        }
//        else {
//            this.saplingState = sapling.getStateFromMeta(saplingMeta);
//        }

        exists = true;
        AstralSorcery.log.info("Loaded " + name() + " of " + this.parentModId + " into tree registry.");
    }

    private boolean isEmpty(@Nullable Block block) {
        return block == null || block.equals(Blocks.air);
    }

    public boolean exists() {
        return exists;
    }

    public Collection<Block> getLeaveStates() {
        return leaveStates;
    }

    public Collection<Block> getLogStates() {
        return logStates;
    }

    public Block getSaplingState() {
        return saplingState;
    }

    public BlockStateCheck getLogCheck() {
        return logCheck;
    }

    public BlockStateCheck getLeavesCheck() {
        return leavesCheck;
    }

    public BlockStateCheck getSaplingCheck() {
        return saplingCheck;
    }

    @Nullable
    public static TreeTypes getTree(World world, BlockPos pos) {
        return getTree(world, pos, world.getBlock(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Nullable
    public static TreeTypes getTree(World world, BlockPos pos, Block blockToTest) {
        for (TreeTypes type : values()) {
            if (type.exists() && (type.logCheck.isStateValid(world, pos, blockToTest) || type.leavesCheck.isStateValid(world, pos, blockToTest))) {
                return type;
            }
        }
        return null;
    }

    public static void init() {} //Well... all static here.

}
