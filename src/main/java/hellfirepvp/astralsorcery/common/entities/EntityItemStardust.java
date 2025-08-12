/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.entities;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.data.config.Config;
import hellfirepvp.astralsorcery.common.item.crystal.base.ItemRockCrystalBase;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.EntityUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EntityItemStardust
 * Created by HellFirePvP
 * Date: 14.09.2016 / 17:42
 */
public class EntityItemStardust extends EntityItem implements EntityStarlightReacttant {

    private static final AxisAlignedBB boxCraft = AxisAlignedBB.getBoundingBox(-0.6, -0.2, -0.6, 0.6, 0.2, 0.6);

    public static final int TOTAL_MERGE_TIME = 30 * 20;
    private int inertMergeTick = 0;

    public EntityItemStardust(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityItemStardust(World worldIn) {
        super(worldIn);
    }

    public EntityItemStardust(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (age + 5 >= lifespan) {
            age = 0;
        }

        if (Config.craftingLiqCelestialCrystalForm) {
            checkMergeConditions();
        }
    }

    private void checkMergeConditions() {
        if (worldObj.isRemote) {
            if (canCraft()) {
                spawnCraftingParticles();
            }
        } else {
            if (canCraft()) {
                inertMergeTick++;
                if (inertMergeTick >= TOTAL_MERGE_TIME && rand.nextInt(20) == 0) {
                    buildCelestialCrystals();
                }
            } else {
                inertMergeTick = 0;
            }
        }
    }

    private void buildCelestialCrystals() {
        BlockPos pos = new BlockPos(this).getPosition();
        PacketChannel.CHANNEL.sendToAllAround(
            new PktParticleEvent(PktParticleEvent.ParticleEventType.CELESTIAL_CRYSTAL_FORM, posX, posY, posZ),
            PacketChannel.pointFromPos(worldObj, pos, 64));

        worldObj.setBlock(pos.getX(), pos.getY(), pos.getZ(), BlocksAS.celestialCrystals);
        getEntityItem().stackSize--;
        List<Entity> foundItems = worldObj.getEntitiesWithinAABBExcludingEntity(
            this,
            boxCraft.offset(posX, posY, posZ)
                .expand(0.1, 0.1, 0.1),
            (IEntitySelector) EntityUtils.selectItemClassInstaceof(ItemRockCrystalBase.class));
        if (foundItems.size() > 0) {
            EntityItem ei = (EntityItem) foundItems.get(0);
            ItemStack stack = ei.getEntityItem();
            stack.stackSize--;
            if (stack.stackSize <= 0) {
                ei.setDead();
            } else {
                ei.setEntityItemStack(stack);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnCraftingParticles() {
        EntityFXFacingParticle p = EffectHelper.genericFlareParticle(
            posX + rand.nextFloat() * 0.2 * (rand.nextBoolean() ? 1 : -1),
            posY + rand.nextFloat() * 0.2 * (rand.nextBoolean() ? 1 : -1),
            posZ + rand.nextFloat() * 0.2 * (rand.nextBoolean() ? 1 : -1));
        p.motion(
            rand.nextFloat() * 0.05 * (rand.nextBoolean() ? 1 : -1),
            rand.nextFloat() * 0.1 * (rand.nextBoolean() ? 1 : -1),
            rand.nextFloat() * 0.05 * (rand.nextBoolean() ? 1 : -1));
        p.gravity(0.2);
        p.scale(0.2F);
    }

    @SideOnly(Side.CLIENT)
    public static void spawnFormationParticles(PktParticleEvent event) {

    }

    private boolean canCraft() {
        if (!isInLiquidStarlight(this)) return false;

        List<Entity> foundItems = worldObj.getEntitiesWithinAABBExcludingEntity(
            this,
            boxCraft.offset(posX, posY, posZ),
            (IEntitySelector) EntityUtils.selectItemClassInstaceof(ItemRockCrystalBase.class));
        return foundItems.size() > 0;
    }

    // public Vec3 getPosition(float par1)
    // {
    // if (par1 == 1.0F)
    // {
    // return Vec3.createVectorHelper(this.posX, this.posY + getEyeHeight(), this.posZ);
    // }
    // else
    // {
    // double d0 = this.prevPosX + (this.posX - this.prevPosX) * (double)par1;
    // double d1 = this.prevPosY + (this.posY - this.prevPosY) * (double)par1 + getEyeHeight();
    // double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par1;
    // return Vec3.createVectorHelper(d0, d1, d2);
    // }
    // }
    //
    // public float getEyeHeight()
    // {
    // return this.height * 0.85F;
    // }
}
