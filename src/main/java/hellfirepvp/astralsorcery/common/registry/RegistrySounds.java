/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.registry;

import static hellfirepvp.astralsorcery.common.lib.Sounds.*;

import net.minecraft.util.ResourceLocation;

import hellfirepvp.astralsorcery.AstralSorcery;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: RegistrySounds
 * Created by HellFirePvP
 * Date: 06.12.2016 / 12:54
 */
public class RegistrySounds {

    public static void init() {
        clipSwitch = new ResourceLocation(AstralSorcery.MODID, "sounds/clip");
        attunement = new ResourceLocation(AstralSorcery.MODID, "sounds/attunementAltar");
        craftFinish = new ResourceLocation(AstralSorcery.MODID, "sounds/finish");
        bookClose = new ResourceLocation(AstralSorcery.MODID, "sounds/book_close");
        bookFlip = new ResourceLocation(AstralSorcery.MODID, "sounds/book_turn1");
        bookFlip = new ResourceLocation(AstralSorcery.MODID, "sounds/book_turn2");
    }

    /*
     * private static <T extends SoundEvent> T registerSound(String jsonName, SoundCategory predefinedCategory) {
     * ResourceLocation res = new ResourceLocation(AstralSorcery.MODID, jsonName);
     * SoundUtils.LoopableSoundEvent se = new SoundUtils.LoopableSoundEvent(res, predefinedCategory);
     * se.setRegistryName(res);
     * return registerSound((T) se);
     * }
     */

    // private static <T extends SoundEvent> T registerSound(String jsonName, SoundCategory predefinedCategory) {
    // ResourceLocation res = new ResourceLocation(AstralSorcery.MODID, jsonName);
    // SoundUtils.CategorizedSoundEvent se = new SoundUtils.CategorizedSoundEvent(res, predefinedCategory);
    // se.setRegistryName(res);
    // return registerSound((T) se);
    // }
    //
    // private static <T extends SoundEvent> T registerSound(String jsonName) {
    // ResourceLocation res = new ResourceLocation(AstralSorcery.MODID, jsonName);
    // SoundEvent se = new SoundEvent(res);
    // se.setRegistryName(res);
    // return registerSound((T) se);
    // }
    //
    // private static <T extends SoundEvent> T registerSound(T soundEvent) {
    // GameRegistry.register(soundEvent);
    // return soundEvent;
    // }

}
