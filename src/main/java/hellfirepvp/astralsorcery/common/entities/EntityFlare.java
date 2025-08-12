/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.entities;

import java.awt.Color;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.effect.EffectHandler;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingSprite;
import hellfirepvp.astralsorcery.client.util.SpriteLibrary;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.constellation.distribution.ConstellationSkyHandler;
import hellfirepvp.astralsorcery.common.data.config.Config;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EntityFlare
 * Created by HellFirePvP
 * Date: 07.02.2017 / 12:15
 */
public class EntityFlare extends EntityFlying {

    private static final int strollRange = 31;

    public Object texSprite = null;
    private BlockPos moveTarget = null;
    private boolean isAmbient = false;

    public EntityFlare(World worldIn) {
        super(worldIn);
        setSize(0.7F, 0.7F);
    }

    public EntityFlare(World worldIn, double x, double y, double z) {
        super(worldIn);
        setSize(0.7F, 0.7F);
        this.setPositionAndRotation(x, y, z, 0, 0);
    }

    public EntityFlare setAmbient(boolean ambient) {
        this.isAmbient = ambient;
        return this;
    }

    public static void spawnAmbient(World world, Vector3 at) {
        if (world.isRemote) return;
        if (Config.ambientFlareChance <= 0) return;
        float nightPerc = ConstellationSkyHandler.getInstance()
            .getCurrentDaytimeDistribution(world);
        if (world.rand.nextInt(Config.ambientFlareChance) == 0
            && world.isAirBlock((int) at.getX(), (int) at.getY(), (int) at.getZ())
            && world.rand.nextFloat() < nightPerc) {
            world.spawnEntityInWorld(new EntityFlare(world, at.getX(), at.getY(), at.getZ()).setAmbient(true));
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(1D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (worldObj.isRemote) {
            if (texSprite == null) {
                setupSprite();
            }
            clientUpdate();
        } else {
            if (entityAge > 300 && rand.nextInt(700) == 0) {
                damageEntity(DamageSource.magic, 20F);
            }

            if (!isDead) {

                if (Config.flareKillsBats && entityAge % 70 == 0 && rand.nextBoolean()) {
                    Entity closest = worldObj
                        .findNearestEntityWithinAABB(EntityBat.class, boundingBox.expand(10, 10, 10), this);
                    if (closest != null && closest instanceof EntityBat
                        && ((EntityBat) closest).getHealth() > 0
                        && !closest.isDead) {
                        closest.attackEntityFrom(CommonProxy.dmgSourceStellar, 40F);
                        PktParticleEvent ev = new PktParticleEvent(
                            PktParticleEvent.ParticleEventType.FLARE_PROC,
                            new Vector3(posX, posY + this.height / 2, posZ));
                        BlockPos getPosition = new BlockPos(this).getPosition();
                        PacketChannel.CHANNEL
                            .sendToAllAround(ev, PacketChannel.pointFromPos(worldObj, getPosition, 16));
                        AstralSorcery.proxy
                            .fireLightning(worldObj, new Vector3(this), new Vector3(closest), new Color(0, 0, 216));
                    }
                }

                if (isAmbient) {
                    if ((moveTarget == null
                        || getDistanceSq(moveTarget.getX(), moveTarget.getY(), moveTarget.getZ()) < 5D)
                        && rand.nextInt(260) == 0) {
                        BlockPos getPosition = new BlockPos(this).getPosition()
                            .add(-strollRange / 2, -strollRange / 2, -strollRange / 2)
                            .add(rand.nextInt(strollRange), rand.nextInt(strollRange), rand.nextInt(strollRange));
                        moveTarget = getPosition;
                    }

                    if (moveTarget != null && (moveTarget.getY() <= 1
                        || !worldObj.isAirBlock(moveTarget.getX(), moveTarget.getY(), moveTarget.getZ())
                        || getDistanceSq(moveTarget.getX(), moveTarget.getY(), moveTarget.getZ()) < 5D)) {
                        moveTarget = null;
                    }
                } else if (getAttackTarget() != null) {
                    if (getAttackTarget().isDead) {
                        if (rand.nextInt(30) == 0) {
                            damageEntity(DamageSource.magic, 20F);
                        }
                    } else {
                        moveTarget = new Vector3(getAttackTarget()).toBlockPos();
                    }
                    if (moveTarget != null && (moveTarget.getY() <= 1
                        || getDistanceSq(moveTarget.getX(), moveTarget.getY(), moveTarget.getZ()) < 3D)) {
                        moveTarget = null;
                    }
                }

                if (getAttackTarget() != null && !getAttackTarget().isDead
                    && getAttackTarget().getDistanceToEntity(this) < 10
                    && rand.nextInt(40) == 0) {
                    getAttackTarget().attackEntityFrom(CommonProxy.dmgSourceStellar, 4F);
                    PktParticleEvent ev = new PktParticleEvent(
                        PktParticleEvent.ParticleEventType.FLARE_PROC,
                        new Vector3(posX, posY + this.height / 2, posZ));
                    BlockPos getPosition = new BlockPos(this).getPosition();
                    PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(worldObj, getPosition, 16));
                    AstralSorcery.proxy.fireLightning(
                        worldObj,
                        new Vector3(this),
                        new Vector3(getAttackTarget()),
                        new Color(0, 0, 216));
                }

                if (moveTarget != null) {
                    this.motionX += (Math.signum(moveTarget.getX() + 0.5D - this.posX) * 0.5D - this.motionX)
                        * (isAmbient ? 0.01D : 0.02D);
                    this.motionY += (Math.signum(moveTarget.getY() + 0.5D - this.posY) * 0.7D - this.motionY)
                        * (isAmbient ? 0.01D : 0.02D);
                    this.motionZ += (Math.signum(moveTarget.getZ() + 0.5D - this.posZ) * 0.5D - this.motionZ)
                        * (isAmbient ? 0.01D : 0.02D);
                    this.moveForward = 0.2F;
                }
            }
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean getCanSpawnHere() {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return super.getBoundingBox();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        setHealth(0F);
    }

    @Nullable
    @Override
    protected String getHurtSound() {
        return null;
    }

    @Nullable
    @Override
    protected String getDeathSound() {
        return null;
    }

    @Override
    protected void onDeathUpdate() {
        setDead();

        if (worldObj.isRemote) {
            deathEffectsEnd();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("isSpawnedAmbient", this.isAmbient);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.isAmbient = compound.getBoolean("isSpawnedAmbient");
    }

    @SideOnly(Side.CLIENT)
    private void deathEffectsOngoing() {
        for (int i = 0; i < 3; i++) {
            EntityFXFacingParticle particle = EffectHelper.genericFlareParticle(posX, posY, posZ);
            particle
                .motion(-0.05 + rand.nextFloat() * 0.1, -0.05 + rand.nextFloat() * 0.1, -0.05 + rand.nextFloat() * 0.1);
            particle.scale(0.1F + rand.nextFloat() * 0.2F)
                .gravity(-0.02);
            if (rand.nextBoolean()) {
                particle.setColor(Color.WHITE);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void deathEffectsEnd() {
        EntityFXFacingSprite p = (EntityFXFacingSprite) texSprite;
        if (p != null) {
            p.requestRemoval();
        }
        for (int i = 0; i < 29; i++) {
            EntityFXFacingParticle particle = EffectHelper.genericFlareParticle(posX, posY + this.height / 2, posZ);
            particle
                .motion(-0.1 + rand.nextFloat() * 0.2, -0.1 + rand.nextFloat() * 0.2, -0.1 + rand.nextFloat() * 0.2);
            particle.scale(0.1F + rand.nextFloat() * 0.2F)
                .gravity(-0.02);
            if (rand.nextBoolean()) {
                particle.setColor(Color.WHITE);
            }
        }
        for (int i = 0; i < 35; i++) {
            EntityFXFacingParticle particle = EffectHelper.genericFlareParticle(posX, posY, posZ);
            particle.offset(
                -0.2 + rand.nextFloat() * 0.4,
                (this.height / 2) - 0.2 + rand.nextFloat() * 0.4,
                -0.2 + rand.nextFloat() * 0.4);
            particle.scale(0.1F + rand.nextFloat() * 0.2F)
                .gravity(0.004);
            if (rand.nextBoolean()) {
                particle.setColor(Color.WHITE);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void clientUpdate() {
        EntityFXFacingSprite p = (EntityFXFacingSprite) texSprite;
        if (p.isRemoved()) {
            EffectHandler.getInstance()
                .registerFX(p);
        }

        if (rand.nextBoolean()) {
            EntityFXFacingParticle particle = EffectHelper.genericFlareParticle(posX, posY, posZ);
            particle.offset(
                -0.3 + rand.nextFloat() * 0.6,
                (this.height / 2) - 0.3 + rand.nextFloat() * 0.6,
                -0.3 + rand.nextFloat() * 0.6);
            particle.scale(0.1F + rand.nextFloat() * 0.2F)
                .gravity(-0.02);
            if (rand.nextBoolean()) {
                particle.setColor(Color.WHITE);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void setupSprite() {
        EntityFXFacingSprite p = EntityFXFacingSprite
            .fromSpriteSheet(SpriteLibrary.spriteFlare1, posX, posY, posZ, 0.8F, 2);
        p.setPositionUpdateFunction((fx, v, m) -> new Vector3(this, true).addY(this.height / 2));
        p.setRefreshFunc(() -> !isDead);
        EffectHandler.getInstance()
            .registerFX(p);
        this.texSprite = p;
    }

    @SideOnly(Side.CLIENT)
    public static void playParticles(PktParticleEvent pktParticleEvent) {
        Random rand = new Random();
        Vector3 at = pktParticleEvent.getVec();
        for (int i = 0; i < 17; i++) {
            EntityFXFacingParticle particle = EffectHelper.genericFlareParticle(at.getX(), at.getY(), at.getZ());
            particle
                .motion(-0.05 + rand.nextFloat() * 0.1, -0.05 + rand.nextFloat() * 0.1, -0.05 + rand.nextFloat() * 0.1);
            particle.scale(0.1F + rand.nextFloat() * 0.2F)
                .gravity(-0.02);
            if (rand.nextBoolean()) {
                particle.setColor(Color.WHITE);
            }
        }
    }

}
