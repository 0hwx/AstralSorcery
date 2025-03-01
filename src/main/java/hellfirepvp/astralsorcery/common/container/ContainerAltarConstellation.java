/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.container;

import hellfirepvp.astralsorcery.common.tile.TileAltar;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ContainerAltarConstellation
 * Created by HellFirePvP
 * Date: 02.11.2016 / 14:42
 */
public class ContainerAltarConstellation extends ContainerAltarAttunement {

    public ContainerAltarConstellation(InventoryPlayer playerInv, TileAltar tileAltar) {
        super(playerInv, tileAltar);
    }

    @Override
    void bindAltarInventory() {
        super.bindAltarInventory();

        addSlotToContainer(new Slot(invHandler, 13, 102,  11));
        addSlotToContainer(new Slot(invHandler, 14, 138,  11));

        addSlotToContainer(new Slot(invHandler, 15,  84,  29));
        addSlotToContainer(new Slot(invHandler, 16, 156,  29));

        addSlotToContainer(new Slot(invHandler, 17, 84,   65));
        addSlotToContainer(new Slot(invHandler, 18, 156,  65));

        addSlotToContainer(new Slot(invHandler, 19, 102,  83));
        addSlotToContainer(new Slot(invHandler, 20, 138,  83));
    }

}
