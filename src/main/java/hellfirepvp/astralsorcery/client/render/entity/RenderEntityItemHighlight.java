/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.entities.EntityItemHighlighted;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: RenderEntityItemHighlight
 * Created by HellFirePvP
 * Date: 13.05.2016 / 13:59
 */
public class RenderEntityItemHighlight extends Render {

    private final RenderItem renderItem = new RenderItem();

    public RenderEntityItemHighlight() {
        renderItem.setRenderManager(RenderManager.instance);
    }

    @Override
    public void doRender(Entity itemHighlighted, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityItemHighlighted entity = (EntityItemHighlighted) itemHighlighted;
        RenderingUtils.renderLightRayEffects(x, y + 0.20, z, entity.getHighlightColor(), 16024L, entity.age, 16, 20, 5);

        GL11.glPushMatrix();
        ItemStack stack = entity.getEntityItem();
        if (stack != null) {
            EntityItem ei = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, stack);
            ei.age = entity.age;
            ei.hoverStart = entity.hoverStart;

            renderItem.doRender(ei, x, y, z, entityYaw, partialTicks);
        }
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
