/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.effect.EffectHandler;
import hellfirepvp.astralsorcery.client.effect.controller.orbital.OrbitalEffectController;
import hellfirepvp.astralsorcery.client.effect.controller.orbital.OrbitalPropertiesInfuser;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.crafting.IGatedRecipe;
import hellfirepvp.astralsorcery.common.crafting.infusion.AbstractInfusionRecipe;
import hellfirepvp.astralsorcery.common.crafting.infusion.ActiveInfusionTask;
import hellfirepvp.astralsorcery.common.crafting.infusion.InfusionRecipeRegistry;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.entities.EntityFlare;
import hellfirepvp.astralsorcery.common.item.base.IWandInteract;
import hellfirepvp.astralsorcery.common.lib.MultiBlockArrays;
import hellfirepvp.astralsorcery.common.lib.Sounds;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionClassRegistry;
import hellfirepvp.astralsorcery.common.tile.base.TileReceiverBase;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.SoundHelper;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TileStarlightInfuser
 * Created by HellFirePvP
 * Date: 11.12.2016 / 17:11
 */
public class TileStarlightInfuser extends TileReceiverBase implements IWandInteract {

    public static final BlockPos[] offsetsLiquidStarlight = new BlockPos[] { new BlockPos(-2, -1, -1),
        new BlockPos(-2, -1, 0), new BlockPos(-2, -1, 1), new BlockPos(2, -1, -1), new BlockPos(2, -1, 0),
        new BlockPos(2, -1, 1), new BlockPos(-1, -1, -2), new BlockPos(0, -1, -2), new BlockPos(1, -1, -2),
        new BlockPos(-1, -1, 2), new BlockPos(0, -1, 2), new BlockPos(1, -1, 2) };

    private ActiveInfusionTask craftingTask = null;

    private Object clientOrbitalCrafting = null;
    private Object clientOrbitalCraftingMirror = null;

    private ItemStack stack = null;
    private boolean hasMultiblock = false, doesSeeSky = false;

    @Override
    public void tick() {
        super.tick();

        if ((ticksExisted & 15) == 0) {
            updateSkyState();
        }

        if ((ticksExisted & 31) == 0) {
            updateMultBlock();
        }

        if (!worldObj.isRemote) {
            if (doTryCraft()) {
                markForUpdate();
            }
        } else {
            if (craftingTask != null) {
                doClientCraftEffects();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void doClientCraftEffects() {
        craftingTask.getRecipeToCraft()
            .onCraftClientTick(this, ClientScheduler.getClientTick(), rand);

        if (clientOrbitalCrafting == null || ((OrbitalEffectController) clientOrbitalCrafting).isRemoved()) {
            OrbitalPropertiesInfuser prop = new OrbitalPropertiesInfuser(this, false);
            OrbitalEffectController ctrl = EffectHandler.getInstance()
                .orbital(prop, prop, null);
            ctrl.setOffset(new Vector3(this).add(0.5, 0, 0.5));
            ctrl.setOrbitAxis(Vector3.RotAxis.Y_AXIS);
            ctrl.setOrbitRadius(2);
            ctrl.setTicksPerRotation(80);
            clientOrbitalCrafting = ctrl;
        }
        if (clientOrbitalCraftingMirror == null
            || ((OrbitalEffectController) clientOrbitalCraftingMirror).isRemoved()) {
            OrbitalPropertiesInfuser prop = new OrbitalPropertiesInfuser(this, true);
            OrbitalEffectController ctrl = EffectHandler.getInstance()
                .orbital(prop, prop, null);
            ctrl.setOffset(new Vector3(this).add(0.5, 0, 0.5));
            ctrl.setOrbitAxis(Vector3.RotAxis.Y_AXIS);
            ctrl.setTicksPerRotation(80);
            ctrl.setTickOffset(ctrl.getMaxAge() / 2);
            ctrl.setOrbitRadius(2);
            clientOrbitalCraftingMirror = ctrl;
        }
    }

    private boolean doTryCraft() {
        if (craftingTask == null) return false;

        AbstractInfusionRecipe altarRecipe = craftingTask.getRecipeToCraft();
        if (!altarRecipe.matches(this)) {
            abortCrafting();
            return true;
        }
        if (craftingTask.isFinished()) {
            finishCrafting();
            return true;
        }
        craftingTask.tick(this);
        craftingTask.getRecipeToCraft()
            .onCraftServerTick(this, craftingTask.getTicksCrafting(), rand);
        return false;
    }

    private void finishCrafting() {
        if (craftingTask == null) return;

        AbstractInfusionRecipe altarRecipe = craftingTask.getRecipeToCraft();
        ItemStack out = altarRecipe.getOutput(this);
        if (out != null) {
            out = ItemUtils.copyStackWithSize(out, out.stackSize);
        }

        if (altarRecipe.mayDeleteInput(this)) {
            this.stack = null;
        } else {
            altarRecipe.handleInputDecrement(this);
        }

        if (out != null) {
            if (out.stackSize > 0) {
                ItemUtils.dropItem(worldObj, xCoord + 0.5, yCoord + 1.3, zCoord + 0.5, out).age = -6000;
            }
        }
        int size = offsetsLiquidStarlight.length;
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        while (size > 0) {
            BlockPos offset = offsetsLiquidStarlight[indexes.get(size - 1)];
            size--;
            if (worldObj.rand.nextFloat() < craftingTask.getRecipeToCraft()
                .getLiquidStarlightConsumptionChance()) {
                BlockPos pos = new BlockPos(xCoord, yCoord, zCoord).add(offset);
                worldObj.setBlockToAir(pos.getX(), pos.getY(), pos.getZ());
                EntityFlare.spawnAmbient(
                    worldObj,
                    new Vector3(this).add(-3 + rand.nextFloat() * 7, 0.6, -3 + rand.nextFloat() * 7));
                if (!altarRecipe.doesConsumeMultiple()) break;
            }
        }
        craftingTask.getRecipeToCraft()
            .onCraftServerFinish(this, rand);
        ResearchManager.informCraftingInfusionCompletion(this, craftingTask);
        SoundHelper.playSoundAround(Sounds.craftFinish, worldObj, xCoord, yCoord, zCoord, 1F, 1.7F);
        EntityFlare
            .spawnAmbient(worldObj, new Vector3(this).add(-3 + rand.nextFloat() * 7, 0.6, -3 + rand.nextFloat() * 7));
        craftingTask = null;
    }

    private void updateMultBlock() {
        BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);
        boolean found = MultiBlockArrays.patternStarlightInfuser.matches(worldObj, pos);
        boolean update = hasMultiblock != found;
        this.hasMultiblock = found;
        if (update) {
            markForUpdate();
        }
    }

    private void updateSkyState() {
        boolean seesSky = worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord);
        boolean update = doesSeeSky != seesSky;
        this.doesSeeSky = seesSky;
        if (update) {
            markForUpdate();
        }
    }

    public ItemStack getInputStack() {
        return stack;
    }

    public void setStack(@Nullable ItemStack stack) {
        this.stack = stack;
    }

    public boolean hasMultiblock() {
        return hasMultiblock;
    }

    public boolean doesSeeSky() {
        return doesSeeSky;
    }

    private void findRecipe(EntityPlayer crafter) {
        if (craftingTask != null) return;

        AbstractInfusionRecipe recipe = InfusionRecipeRegistry.findMatchingRecipe(this);
        if (recipe instanceof IGatedRecipe) {
            if (!((IGatedRecipe) recipe).hasProgressionServer(crafter)) return;
        }
        if (recipe != null) {
            this.craftingTask = new ActiveInfusionTask(recipe, crafter.getUniqueID());
            markForUpdate();
        }
    }

    public void abortCrafting() {
        this.craftingTask = null;
        markForUpdate();
    }

    public ActiveInfusionTask getCraftingTask() {
        return craftingTask;
    }

    @SideOnly(Side.CLIENT)
    public OrbitalEffectController getClientOrbitalCrafting() {
        if (clientOrbitalCrafting == null) return null;
        return (OrbitalEffectController) clientOrbitalCrafting;
    }

    @SideOnly(Side.CLIENT)
    public OrbitalEffectController getClientOrbitalCraftingMirror() {
        if (clientOrbitalCraftingMirror == null) return null;
        return (OrbitalEffectController) clientOrbitalCraftingMirror;
    }

    public boolean canCraft() {
        return hasMultiblock() && !isInvalid() && doesSeeSky();
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.stack = NBTHelper.getStack(compound, "stack");
        this.hasMultiblock = compound.getBoolean("mbState");
        this.doesSeeSky = compound.getBoolean("seesSky");

        boolean wasNull = this.craftingTask == null;
        this.craftingTask = null;
        if (compound.hasKey("recipeId") && compound.hasKey("recipeTick")) {
            int recipeId = compound.getInteger("recipeId");
            AbstractInfusionRecipe recipe = InfusionRecipeRegistry.getRecipe(recipeId);
            if (recipe == null) {
                AstralSorcery.log.info(
                    "Recipe with unknown/invalid ID found: " + recipeId
                        + " for Starlight Infuser at "
                        + xCoord
                        + ", "
                        + yCoord
                        + ", "
                        + zCoord);
            } else {
                UUID uuidCraft = NBTHelper.getUUID(compound, "crafterUUID"); // compound.getUniqueId("crafterUUID");
                int tick = compound.getInteger("recipeTick");
                this.craftingTask = new ActiveInfusionTask(recipe, uuidCraft);
                this.craftingTask.forceTick(tick);
            }
        }
        if (!wasNull && this.craftingTask == null) {
            clientOrbitalCrafting = null;
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        if (this.stack != null) {
            NBTHelper.setStack(compound, "stack", stack);
        }
        compound.setBoolean("mbState", hasMultiblock);
        compound.setBoolean("seesSky", doesSeeSky);

        if (craftingTask != null) {
            compound.setInteger(
                "recipeId",
                craftingTask.getRecipeToCraft()
                    .getUniqueRecipeId());
            compound.setInteger("recipeTick", craftingTask.getTicksCrafting());
            NBTHelper.setUUID(compound, "crafterUUID", craftingTask.getPlayerCraftingUUID());
            // compound.setUniqueId("crafterUUID", craftingTask.getPlayerCraftingUUID());
        }
    }

    @Nullable
    @Override
    public String getUnLocalizedDisplayName() {
        return "tile.BlockStarlightInfuser.name";
    }

    private void receiveStarlight(IWeakConstellation type, double amount) {}

    @Override
    @Nonnull
    public ITransmissionReceiver provideEndpoint(BlockPos at) {
        return new TransmissionReceiverStarlightInfuser(at);
    }

    public void onInteract(EntityPlayer playerIn, @Nullable ItemStack heldItem) {
        if (!playerIn.getEntityWorld().isRemote) {
            if (playerIn.isSneaking()) {
                if (stack != null) {
                    ItemUtils
                        .dropItemNaturally(playerIn.getEntityWorld(), xCoord + 0.5, yCoord + 1, zCoord + 0.5, stack);
                    stack = null;
                    worldObj.playSound(
                        xCoord,
                        yCoord,
                        zCoord,
                        "SoundEvents.ENTITY_ITEM_PICKUP",
                        0.5F,
                        worldObj.rand.nextFloat() * 0.2F + 0.8F,
                        false);
                    markForUpdate();
                }
            } else {
                if (heldItem != null) {
                    if (stack == null) {
                        heldItem.stackSize--;
                        this.stack = ItemUtils.copyStackWithSize(heldItem, 1);
                        if (heldItem.stackSize <= 0) {
                            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                        }
                        worldObj.playSound(
                            xCoord,
                            yCoord,
                            zCoord,
                            "SoundEvents.ENTITY_ITEM_PICKUP",
                            0.5F,
                            worldObj.rand.nextFloat() * 0.2F + 0.8F,
                            false);
                        markForUpdate();
                    } /*
                       * else if(heldItem.getItem() instanceof ItemWand) {
                       * findRecipe(playerIn);
                       * }
                       */
                }
            }
        }
    }

    @Override
    public void onInteract(World world, BlockPos pos, EntityPlayer player, int side, boolean sneak) {
        if (!world.isRemote) {
            findRecipe(player);
        }
    }

    public static class TransmissionReceiverStarlightInfuser extends SimpleTransmissionReceiver {

        public TransmissionReceiverStarlightInfuser(BlockPos thisPos) {
            super(thisPos);
        }

        @Override
        public void onStarlightReceive(World world, boolean isChunkLoaded, IWeakConstellation type, double amount) {
            if (isChunkLoaded) {
                TileStarlightInfuser ta = MiscUtils.getTileAt(world, getPos(), TileStarlightInfuser.class, false);
                if (ta != null) {
                    ta.receiveStarlight(type, amount);
                }
            }
        }

        @Override
        public TransmissionClassRegistry.TransmissionProvider getProvider() {
            return new StarlightInfuserReceiverProvider();
        }

    }

    public static class StarlightInfuserReceiverProvider implements TransmissionClassRegistry.TransmissionProvider {

        @Override
        public TransmissionReceiverStarlightInfuser provideEmptyNode() {
            return new TransmissionReceiverStarlightInfuser(null);
        }

        @Override
        public String getIdentifier() {
            return AstralSorcery.MODID + ":TransmissionReceiverStarlightInfuser";
        }

    }
}
