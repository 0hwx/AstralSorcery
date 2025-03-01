/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.util;

import hellfirepvp.astralsorcery.common.util.SoundUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.SoundEvent;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: PositionedLoopSound
 * Created by HellFirePvP
 * Date: 06.12.2016 / 18:05
 */
@SideOnly(Side.CLIENT)
public class PositionedLoopSound extends PositionedSoundRecord implements ITickableSound {

    private ActivityFunction func = null;
    private boolean hasStoppedPlaying = false;

//    public PositionedLoopSound(SoundUtils.CategorizedSoundEvent sound, float volume, float pitch, Vector3 pos) {
//        this(sound, sound.getCategory(), volume, pitch, pos);
//    }

    public PositionedLoopSound(ResourceLocation soundResource, float volume, float pitch, Vector3 pos) {
//        super(soundResource, volume, pitch, true, 0, AttenuationType.LINEAR, (float) pos.getX(), (float) pos.getY(), (float) pos.getZ());
        super(soundResource, volume, pitch, (float) pos.getX(), (float) pos.getY(), (float) pos.getZ());
    }

    public void setRefreshFunction(ActivityFunction func) {
        this.func = func;
    }

    @Override
    public boolean isDonePlaying() {
        hasStoppedPlaying = func == null || func.shouldStop();
        return hasStoppedPlaying;
    }

    public boolean hasStoppedPlaying() {
        return hasStoppedPlaying || !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(this);
    }

    @Override
    public void update() {}

    @SideOnly(Side.CLIENT)
    public static interface ActivityFunction {

        public boolean shouldStop();

    }
}
