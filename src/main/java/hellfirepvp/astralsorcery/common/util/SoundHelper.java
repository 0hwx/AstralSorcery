/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.util.PositionedLoopSound;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: SoundHelper
 * Created by HellFirePvP
 * Date: 06.12.2016 / 12:45
 */
public class SoundHelper {

    public static void playSoundAround(ResourceLocation sound, World world, Vec3 position, float volume, float pitch) {
        playSoundAround(sound, world, position.xCoord, position.yCoord, position.zCoord, volume, pitch);
    }

    // public static void playSoundAround(ResourceLocation sound, World world, Vec3 position, float volume, float pitch)
    // {
    // playSoundAround(sound, world, position.xCoord, position.yCoord, position.zCoord, volume, pitch);
    // }

    public static void playSoundAround(ResourceLocation sound, World world, Vector3 position, float volume,
        float pitch) {
        playSoundAround(sound, world, position.getX(), position.getY(), position.getZ(), volume, pitch);
    }

    // public static void playSoundAround(ResourceLocation sound, World world, Vector3 position, float volume, float
    // pitch) {
    // playSoundAround(sound, world, position.getX(), position.getY(), position.getZ(), volume, pitch);
    // }

    public static void playSoundAround(ResourceLocation sound, World world, double posX, double posY, double posZ,
        float volume, float pitch) {
        // if(sound instanceof SoundUtils.CategorizedSoundEvent) {
        // category = ((SoundUtils.CategorizedSoundEvent) sound).getCategory();
        // }
        world.playSound(posX, posY, posZ, sound.toString(), volume, pitch, false);
    }

    @SideOnly(Side.CLIENT)
    public static PositionedLoopSound playSoundLoopClient(ResourceLocation sound, Vector3 pos, float volume,
        float pitch, PositionedLoopSound.ActivityFunction func) {
        // if(sound instanceof SoundUtils.CategorizedSoundEvent) {
        // cat = ((SoundUtils.CategorizedSoundEvent) sound).getCategory();
        // }
        PositionedLoopSound posSound = new PositionedLoopSound(sound, volume, pitch, pos);
        posSound.setRefreshFunction(func);
        Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(posSound);
        return posSound;
    }

    @SideOnly(Side.CLIENT)
    public float getSoundVolume(SoundCategory cat) {
        return Minecraft.getMinecraft().gameSettings.getSoundLevel(cat);
    }

    @SideOnly(Side.CLIENT)
    public static void playSoundClient(ResourceLocation sound, float volume, float pitch) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        player.playSound(sound.toString(), volume, pitch);
    }

    // @SideOnly(Side.CLIENT)
    // public static void playSoundClientWorld(ResourceLocation sound, BlockPos pos, float volume, float pitch) {
    // playSoundClientWorld(sound, pos, volume, pitch);
    // }
    //
    @SideOnly(Side.CLIENT)
    public static void playSoundClientWorld(ResourceLocation sound, int x, int y, int z, float volume, float pitch) {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().theWorld.playSound(x, y, z, sound.toString(), volume, pitch, false);
        }
    }

}
