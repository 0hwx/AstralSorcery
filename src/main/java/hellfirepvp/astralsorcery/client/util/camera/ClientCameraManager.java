/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.util.camera;

import java.util.EnumSet;
import java.util.TreeSet;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.auxiliary.tick.ITickHandler;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ClientCameraManager
 * Created by HellFirePvP
 * Date: 13.12.2016 / 00:10
 */
public class ClientCameraManager implements ITickHandler {

    private static final ClientCameraManager instance = new ClientCameraManager();

    private ClientCameraManager() {}

    private TreeSet<ICameraTransformer> transformers = new TreeSet<>((t1, t2) -> t1.getPriority() - t2.getPriority());
    private ICameraTransformer lastTransformer = null;

    public static ClientCameraManager getInstance() {
        return instance;
    }

    @Override
    public void tick(TickEvent.Type type, Object... context) {
        if (type == TickEvent.Type.RENDER) {
            float pTicks = (float) context[0];
            if (!transformers.isEmpty()) {
                ICameraTransformer prio = transformers.last();
                if (prio != lastTransformer) {
                    if (lastTransformer != null) {
                        lastTransformer.onStopTransforming(pTicks);
                    }
                    prio.onStartTransforming(pTicks);
                    lastTransformer = prio;
                }
                prio.transformRenderView(pTicks);
                if (prio.needsRemoval()) {
                    prio.onStopTransforming(pTicks);
                    transformers.remove(prio);
                }
            } else {
                if (lastTransformer != null) {
                    lastTransformer.onStopTransforming(pTicks);
                    lastTransformer = null;
                }
            }
        } else {
            if (!transformers.isEmpty()) {
                ICameraTransformer prio = transformers.last();
                prio.onClientTick();
            }
        }
    }

    @Override
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.RENDER, TickEvent.Type.CLIENT);
    }

    @Override
    public boolean canFire(TickEvent.Phase phase) {
        return phase == TickEvent.Phase.START;
    }

    @Override
    public String getName() {
        return "Client Camera Manager";
    }

    public void removeAllAndCleanup() {
        if (!transformers.isEmpty()) {
            ICameraTransformer last = transformers.last();
            last.onStopTransforming(0);
        }
        transformers.clear();
    }

    public void addTransformer(ICameraTransformer transformer) {
        this.transformers.add(transformer);
    }

    public boolean hasActiveTransformer() {
        return !transformers.isEmpty();
    }

    public static interface ICameraTransformer {

        public int getPriority();

        public boolean needsRemoval();

        public void onClientTick();

        public void onStartTransforming(float pTicks);

        public void onStopTransforming(float pTicks);

        public void transformRenderView(float pTicks);

    }

    public static abstract class CameraTransformerSettingsCache implements ICameraTransformer {

        private boolean active = false;

        private boolean viewBobbing = false, hideGui = false, flying = false;
        private int thirdPersonView = 0;

        private Vector3 startPosition;
        private float startYaw, startPitch;

        @Override
        public void onStartTransforming(float pTicks) {
            this.viewBobbing = Minecraft.getMinecraft().gameSettings.viewBobbing;
            this.hideGui = Minecraft.getMinecraft().gameSettings.hideGUI;
            this.thirdPersonView = Minecraft.getMinecraft().gameSettings.thirdPersonView;
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            this.flying = player.capabilities.isFlying;
            this.startPosition = new Vector3(player.posX, player.posY, player.posZ);
            this.startYaw = player.rotationYaw;
            this.startPitch = player.rotationPitch;
            player.setVelocity(0, 0, 0);
            this.active = true;
        }

        @Override
        public void onStopTransforming(float pTicks) {
            if (active) {
                GameSettings settings = Minecraft.getMinecraft().gameSettings;
                settings.viewBobbing = viewBobbing;
                settings.hideGUI = hideGui;
                settings.thirdPersonView = thirdPersonView;
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                player.capabilities.isFlying = flying;
                player.setPositionAndRotation(
                    startPosition.getX(),
                    startPosition.getY(),
                    startPosition.getZ(),
                    startYaw,
                    startPitch);
                player.setVelocity(0, 0, 0);
                this.active = false;
            }
        }

        @Override
        public void transformRenderView(float pTicks) {
            if (!active) return;
            GameSettings settings = Minecraft.getMinecraft().gameSettings;
            settings.hideGUI = true;
            settings.viewBobbing = false;
            settings.thirdPersonView = 0;
            Minecraft.getMinecraft().thePlayer.capabilities.isFlying = true;
            Minecraft.getMinecraft().thePlayer.setVelocity(0, 0, 0);
        }

    }

    public static class CameraTransformerRenderReplacement extends CameraTransformerSettingsCache {

        private final EntityRenderViewReplacement entity;
        private final PersistencyFunction func;

        private EntityClientReplacement clientEntity;

        public CameraTransformerRenderReplacement(EntityRenderViewReplacement renderView, PersistencyFunction func) {
            this.entity = renderView;
            this.func = func;
        }

        @Override
        public void onStartTransforming(float pTicks) {
            super.onStartTransforming(pTicks);

            EntityClientReplacement repl = new EntityClientReplacement();
            NBTTagCompound tag = new NBTTagCompound(); // todo check this
            Minecraft.getMinecraft().thePlayer.writeToNBT(tag);
            repl.readFromNBT(tag);
            Minecraft.getMinecraft().theWorld.spawnEntityInWorld(repl);
            this.clientEntity = repl;

            entity.setAsRenderViewEntity();
        }

        @Override
        public void onStopTransforming(float pTicks) {
            super.onStopTransforming(pTicks);

            if (Minecraft.getMinecraft().theWorld != null) {
                Minecraft.getMinecraft().theWorld.removeEntity(this.clientEntity);
            }

            if (Minecraft.getMinecraft().thePlayer != null) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                player.setPositionAndRotation(
                    this.clientEntity.posX,
                    this.clientEntity.posY,
                    this.clientEntity.posZ,
                    this.clientEntity.rotationYaw,
                    this.clientEntity.rotationPitch);
                player.setVelocity(0, 0, 0);
            }

            RenderingUtils.unsafe_resetCamera();

            if (Minecraft.getMinecraft().theWorld != null) {
                entity.onStopTransforming();
            }
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public void transformRenderView(float pTicks) {
            super.transformRenderView(pTicks);

            Vector3 focus = entity.getCameraFocus();
            if (focus != null) {
                entity.transformToFocusOnPoint(focus, pTicks, true);
            }
        }

        @Override
        public boolean needsRemoval() {
            return func.needsRemoval();
        }

        @Override
        public void onClientTick() {
            entity.ticksExisted++;

            if (clientEntity != null) {
                entity.moveEntityTick(entity, clientEntity, entity.ticksExisted);
            }
        }

    }

    public static interface PersistencyFunction {

        public boolean needsRemoval();

    }

    public static abstract class EntityRenderViewReplacement extends EntityClientPlayerMP {

        private Vector3 cameraFocus = null;

        public EntityRenderViewReplacement() {
            super(
                Minecraft.getMinecraft(),
                Minecraft.getMinecraft().theWorld,
                Minecraft.getMinecraft()
                    .getSession(),
                Minecraft.getMinecraft().thePlayer.sendQueue,
                Minecraft.getMinecraft().thePlayer.getStatFileWriter());
            capabilities.allowFlying = true;
            capabilities.isFlying = true;
        }

        @Nullable
        public Vector3 getCameraFocus() {
            return cameraFocus;
        }

        public void setCameraFocus(@Nullable Vector3 cameraFocus) {
            this.cameraFocus = cameraFocus;
        }

        public void setAsRenderViewEntity() {
            Minecraft.getMinecraft().renderViewEntity = this;
        }

        public void transformToFocusOnPoint(Vector3 toFocus, float pTicks, boolean propagate) {
            Vector3 angles = new Vector3(posX, posY, posZ).subtract(toFocus)
                .copyToPolar();
            Vector3 prevAngles = new Vector3(prevPosX, prevPosY, prevPosZ).subtract(toFocus)
                .copyToPolar();
            double pitch = 90 - angles.getY();
            double pitchPrev = 90 - prevAngles.getY();
            double yaw = -angles.getZ();
            double yawPrev = -prevAngles.getZ();

            if (propagate) {
                RenderingUtils.unsafe_preRenderHackCamera(
                    this,
                    posX,
                    posY,
                    posZ,
                    prevPosX,
                    prevPosY,
                    prevPosZ,
                    yaw,
                    yawPrev,
                    pitch,
                    pitchPrev);
            }
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void setAngles(float yaw, float pitch) {}

        public abstract void moveEntityTick(EntityRenderViewReplacement entity,
            EntityClientReplacement replacementEntity, int ticksExisted);

        public abstract void onStopTransforming();

        // @Override
        // public boolean isSpectator() {
        // return false;
        // }
        //
        // @Override
        // public boolean isCreative() {
        // return false;
        // }
        //
        // @Override
        // public Iterable<ItemStack> getArmorInventoryList() {
        // return Collections.emptyList();
        // }
        //
        // @Nullable
        // @Override
        // public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        // return null;
        // }
        //
        // @Override
        // public void setItemStackToSlot(EntityEquipmentSlot slotIn, @Nullable ItemStack stack) {}
        //
        // @Override
        // public EnumHandSide getPrimaryHand() {
        // return EnumHandSide.RIGHT;
        // }

    }

    public static class EntityClientReplacement extends AbstractClientPlayer {

        public EntityClientReplacement() {
            super(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getGameProfile());
        }

        @Override
        public void addChatMessage(IChatComponent message) {

        }

        @Override
        public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
            return false;
        }

        @Override
        public ChunkCoordinates getPlayerCoordinates() {
            return null;
        }
    }

}
