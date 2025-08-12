package hellfirepvp.astralsorcery.client.effect.controller;

import java.util.Random;

import net.minecraft.util.MathHelper;

import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: RenderOffsetControllerFornax
 * Created by HellFirePvP
 * Date: 22.04.2017 / 15:57
 */
public class RenderOffsetControllerFornax implements EntityComplexFX.RenderOffsetController {

    @Override
    public Vector3 changeRenderPosition(EntityComplexFX fx, Vector3 currentRenderPos, Vector3 currentMotion,
        float pTicks) {
        Vector3 perp = currentMotion.clone()
            .perpendicular()
            .normalize()
            .multiply(0.05);
        Random r = new Random(fx.id); // LUL tho...

        int interv = (int) ((r.nextInt() + ClientScheduler.getClientTick()) % 9);
        float part = interv + pTicks;
        float perc = part / 10F;

        float sinPart = MathHelper.sin(perc * ((float) Math.PI) * 2F);
        return currentRenderPos.clone()
            .add(
                perp.rotate(r.nextFloat() * 360F, currentMotion)
                    .multiply(sinPart));
    }

}
