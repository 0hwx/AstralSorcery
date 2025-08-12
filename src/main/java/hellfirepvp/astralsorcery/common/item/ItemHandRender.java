/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemHandRender
 * Created by HellFirePvP
 * Date: 06.02.2017 / 23:21
 */
public interface ItemHandRender {

    @SideOnly(Side.CLIENT)
    public void onRenderWhileInHand(ItemStack stack, float pTicks);

    @Nullable
    default public MovingObjectPosition getLookBlock(Entity e, boolean stopTraceOnLiquids,
        boolean ignoreBlockWithoutBoundingBox, double range) {
        float pitch = e.rotationPitch;
        float yaw = e.rotationYaw;
        Vec3 entityVec = Vec3.createVectorHelper(e.posX, e.posY + e.getEyeHeight(), e.posZ);
        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vec3d1 = entityVec.addVector((double) f6 * range, (double) f5 * range, (double) f7 * range);
        MovingObjectPosition rtr = e.worldObj
            .func_147447_a(entityVec, vec3d1, stopTraceOnLiquids, ignoreBlockWithoutBoundingBox, false);
        if (rtr == null || rtr.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return null;
        }
        return rtr;
    }

}
