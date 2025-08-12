/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.event.listener;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import hellfirepvp.astralsorcery.common.item.crystal.ItemCelestialCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.base.ItemRockCrystalBase;
import hellfirepvp.astralsorcery.common.registry.RegistryAchievements;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EventHandlerAchievements
 * Created by HellFirePvP
 * Date: 13.09.2016 / 20:19
 */
public class EventHandlerAchievements {

    public static void init() {
        FMLCommonHandler.instance()
            .bus()
            .register(new EventHandlerAchievements());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPick(PlayerEvent.ItemPickupEvent event) {
        if (!event.isCanceled()) {
            if (event.pickedUp != null && event.pickedUp.getEntityItem() != null
                && event.pickedUp.getEntityItem()
                    .getItem() instanceof ItemRockCrystalBase) {
                event.player.addStat(RegistryAchievements.achvRockCrystal, 1);
                if (event.pickedUp.getEntityItem()
                    .getItem() instanceof ItemCelestialCrystal) {
                    event.player.addStat(RegistryAchievements.achvCelestialCrystal, 1);
                }
            }
        }
    }

}
