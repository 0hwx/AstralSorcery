/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.tile;

import java.awt.*;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;

import com.google.common.collect.Lists;

import hellfirepvp.astralsorcery.common.item.tool.ItemChargedCrystalShovel;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktDualParticleEvent;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;
import hellfirepvp.astralsorcery.common.util.BlockDropCaptureAssist;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.nbt.NBTUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TileFakeTree
 * Created by HellFirePvP
 * Date: 11.11.2016 / 20:34
 */
public class TileFakeTree extends TileEntityTick {

    private TickAction ta;
    private Block fakedState;
    private int metadata;

    @Override
    public void tick() {
        super.tick();

        if (!worldObj.isRemote) {
            if (ticksExisted > 5 && ticksExisted % 4 == 0) {
                if (ta != null) {
                    ta.update(this);
                }
                if (fakedState == null || fakedState.equals(Blocks.air)) {
                    cleanUp();
                }
            }
        }
    }

    private void cleanUp() {
        if (fakedState != null) {
            worldObj.setBlock(xCoord, yCoord, zCoord, fakedState);
        } else {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
    }

    @Override
    protected void onFirstTick() {}

    public void setupTile(BlockPos treeBeaconRef, Block fakedState) {
        this.ta = new TreeBeaconRef(treeBeaconRef);
        this.fakedState = fakedState;
        markForUpdate();
    }

    public void setupTile(EntityPlayer breakingPlayer, ItemStack usedAxe, Block fakedState) {
        this.ta = new PlayerHarvestRef(breakingPlayer, usedAxe);
        this.fakedState = fakedState;
        markForUpdate();
    }

    public Block getFakedState() {
        return fakedState;
    }

    @Nullable
    public BlockPos getReference() {
        return ta instanceof TreeBeaconRef ? ((TreeBeaconRef) ta).ref : null;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        int index = compound.getInteger("type");
        if (index == 0) {
            this.ta = new TreeBeaconRef(null);
            ta.read(compound);
        } else {
            this.ta = new ClearAction();
        }

        if (compound.hasKey("Block") && compound.hasKey("Data")) {
            metadata = compound.getInteger("Data");
            Block b = Block.getBlockFromName(compound.getString("Block"));
            if (b != null) {
                metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
                // fakedState = b.getStateFromMeta(data);
            }
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        if (ta instanceof TreeBeaconRef) {
            compound.setInteger("type", 0);
        } else if (ta instanceof PlayerHarvestRef) {
            compound.setInteger("type", 1);
        }
        if (ta != null) {
            ta.write(compound);
        }
        if (fakedState != null) {
            compound.setString(
                "Block",
                Block.blockRegistry.getNameForObject(fakedState)
                    .toString());
            compound.setInteger("Data", metadata);
        }
    }

    @Override
    public void writeNetNBT(NBTTagCompound compound) {
        super.writeNetNBT(compound);

        compound.setInteger("type", 0);
    }

    private static interface TickAction {

        public void update(TileFakeTree tft);

        public void write(NBTTagCompound cmp);

        public void read(NBTTagCompound cmp);

    }

    private static class ClearAction implements TickAction {

        @Override
        public void update(TileFakeTree tft) {
            tft.worldObj.setBlockToAir(tft.xCoord, tft.yCoord, tft.zCoord);
        }

        @Override
        public void write(NBTTagCompound cmp) {}

        @Override
        public void read(NBTTagCompound cmp) {}

    }

    private static class PlayerHarvestRef implements TickAction {

        private EntityPlayer player;
        private ItemStack usedTool;

        private PlayerHarvestRef(EntityPlayer player, ItemStack usedAxe) {
            this.player = player;
            if (usedAxe != null) {
                this.usedTool = usedAxe.copy();
                Map<Integer, Integer> levels = EnchantmentHelper.getEnchantments(this.usedTool);
                if (levels.containsKey(Enchantment.fortune.effectId)) {
                    levels.put(Enchantment.fortune.effectId, levels.get(Enchantment.fortune.effectId) + 2);
                } else {
                    levels.put(Enchantment.fortune.effectId, 2);
                }
                EnchantmentHelper.setEnchantments(levels, this.usedTool);
            } else {
                this.usedTool = null;
            }
        }

        @Override
        public void update(TileFakeTree tft) {
            if (tft.ticksExisted <= 10) return;
            if (player != null && player instanceof EntityPlayerMP
                && !MiscUtils.isPlayerFakeMP((EntityPlayerMP) player)
                && tft.fakedState != null) {
                List<ItemStack> out = Lists.newArrayList();
                harvestAndAppend(tft, out);
                if (rand.nextBoolean()) {
                    harvestAndAppend(tft, out);
                }
                Vector3 plPos = new Vector3(player);
                for (ItemStack stack : out) {
                    ItemUtils.dropItemNaturally(
                        player.getEntityWorld(),
                        plPos.getX() + rand.nextFloat() - rand.nextFloat(),
                        plPos.getY() + rand.nextFloat(),
                        plPos.getZ() + rand.nextFloat() - rand.nextFloat(),
                        stack);
                }
                PktDualParticleEvent ev = new PktDualParticleEvent(
                    PktDualParticleEvent.DualParticleEventType.CHARGE_HARVEST,
                    new Vector3(tft),
                    new Vector3(player));
                if (usedTool != null && usedTool.getItem() instanceof ItemChargedCrystalShovel) {
                    ev.setAdditionalData(
                        Color.GRAY.brighter()
                            .getRGB());
                } else {
                    ev.setAdditionalData(Color.GREEN.getRGB());
                }
                BlockPos pos = new BlockPos(tft.xCoord, tft.yCoord, tft.zCoord);
                PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(tft.worldObj, pos, 24));
            }
            tft.worldObj.setBlockToAir(tft.xCoord, tft.yCoord, tft.zCoord);
            // }
        }

        private void harvestAndAppend(TileFakeTree tft, List<ItemStack> out) {
            BlockDropCaptureAssist.startCapturing(false);
            tft.getFakedState()
                .harvestBlock(player.getEntityWorld(), player, tft.xCoord, tft.yCoord, tft.zCoord, tft.blockMetadata);
            // tft.getFakedState().getBlock().harvestBlock(player.getEntityWorld(), player, tft.getPos(),
            // tft.getFakedState(),
            out.addAll(BlockDropCaptureAssist.getCapturedStacksAndStop());
        }

        @Override
        public void write(NBTTagCompound cmp) {}

        @Override
        public void read(NBTTagCompound cmp) {}

    }

    private static class TreeBeaconRef implements TickAction {

        private BlockPos ref;

        private TreeBeaconRef(BlockPos ref) {
            this.ref = ref;
        }

        @Override
        public void update(TileFakeTree tft) {
            if (MiscUtils.isChunkLoaded(tft.worldObj, new ChunkCoordIntPair(ref.chunkX(), ref.chunkZ()))) {
                TileTreeBeacon beacon = MiscUtils.getTileAt(tft.worldObj, ref, TileTreeBeacon.class, true);
                if (beacon == null || beacon.isInvalid()) {
                    tft.cleanUp();
                }
            }
        }

        @Override
        public void write(NBTTagCompound cmp) {
            if (ref != null) {
                NBTUtils.writeBlockPosToNBT(ref, cmp);
            }
        }

        @Override
        public void read(NBTTagCompound cmp) {
            ref = NBTUtils.readBlockPosFromNBT(cmp);
        }
    }

}
