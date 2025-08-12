/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.entities;

import java.awt.*;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.util.BlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EntityIlluminationSpark
 * Created by HellFirePvP
 * Date: 08.04.2017 / 00:24
 */
public class EntityIlluminationSpark extends EntityThrowable {

    public EntityIlluminationSpark(World worldIn) {
        super(worldIn);
    }

    public EntityIlluminationSpark(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityIlluminationSpark(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        setThrowableHeading(throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0F, 0.7F, 1.0F);
        // setHeadingFromThrower(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0F, 0.7F, 1.0F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (worldObj.isRemote) {
            playEffects();
        }
    }

    @SideOnly(Side.CLIENT)
    private void playEffects() {
        EntityFXFacingParticle particle;
        for (int i = 0; i < 6; i++) {
            particle = EffectHelper.genericFlareParticle(posX, posY, posZ);
            particle
                .motion(
                    rand.nextFloat() * 0.04F - rand.nextFloat() * 0.08F,
                    rand.nextFloat() * 0.04F - rand.nextFloat() * 0.08F,
                    rand.nextFloat() * 0.04F - rand.nextFloat() * 0.08F)
                .scale(0.25F);
            randomizeColor(particle);
        }
        particle = EffectHelper.genericFlareParticle(posX, posY, posZ);
        particle.scale(0.6F);
        randomizeColor(particle);
        particle = EffectHelper.genericFlareParticle(posX + motionX / 2F, posY + motionY / 2F, posZ + motionZ / 2F);
        particle.scale(0.6F);
        randomizeColor(particle);
    }

    @SideOnly(Side.CLIENT)
    private void randomizeColor(EntityFXFacingParticle particle) {
        switch (rand.nextInt(3)) {
            case 0:
                particle.setColor(Color.WHITE);
                break;
            case 1:
                particle.setColor(new Color(0xFEFF9E));
                break;
            case 2:
                particle.setColor(new Color(0xFFE539));
                break;
        }
    }

    @Override
    protected void onImpact(MovingObjectPosition result) {
        if (!worldObj.isRemote) {
            if (result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                ForgeDirection dir = ForgeDirection.getOrientation(result.sideHit);
                BlockPos pos = new BlockPos(result.blockX, result.blockY, result.blockZ).offset(dir);
                int meta = worldObj.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
                if (worldObj.canPlaceEntityOnSide(
                    BlocksAS.blockVolatileLight,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    false,
                    result.sideHit,
                    null,
                    null)) {
                    worldObj.setBlock(pos.getX(), pos.getY(), pos.getZ(), BlocksAS.blockVolatileLight, meta, 3);
                }
            } else if (result.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                if (result.entityHit.equals(getThrower())) {
                    return;
                }
            }
            setDead();
        }
    }

}
