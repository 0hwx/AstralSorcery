/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.tile;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.MiscUtils;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: TileGrindstone
 * Created by HellFirePvP
 * Date: 10.11.2016 / 22:28
 */
public class TileGrindstone extends TileEntitySynchronized implements ITickable {

    public static final int TICKS_WHEEL_ROTATION = 20;

    private ItemStack grindingItem = null;
    public int tickWheelAnimation = 0, prevTickWheelAnimation = 0;
    private boolean repeat = false; // Used for repeat after effect went off..~

    @Override
    public void tick() {
        if (worldObj.isRemote) {
            if (tickWheelAnimation > 0) {
                prevTickWheelAnimation = tickWheelAnimation;
                tickWheelAnimation--;
                if (tickWheelAnimation <= 0 && repeat) {
                    tickWheelAnimation = TICKS_WHEEL_ROTATION;
                    prevTickWheelAnimation = TICKS_WHEEL_ROTATION + 1;
                    repeat = false;
                }
            } else {
                prevTickWheelAnimation = 0;
                tickWheelAnimation = 0;
            }
        }
    }

    public void playWheelEffect() {
        BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);
        PktPlayEffect effect = new PktPlayEffect(PktPlayEffect.EffectType.GRINDSTONE_WHEEL, pos);
        if (worldObj.isRemote) {
            playWheelAnimation(effect);
        } else {
            PacketChannel.CHANNEL.sendToAllAround(effect, PacketChannel.pointFromPos(worldObj, pos, 32));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void playWheelAnimation(PktPlayEffect pktPlayEffect) {
        TileGrindstone tgr = MiscUtils
            .getTileAt(Minecraft.getMinecraft().theWorld, pktPlayEffect.pos, TileGrindstone.class, false);
        if (tgr != null) {
            if (tgr.tickWheelAnimation == 0) {
                tgr.tickWheelAnimation = TICKS_WHEEL_ROTATION;
            } else if (tgr.tickWheelAnimation * 2 <= TICKS_WHEEL_ROTATION) {
                tgr.repeat = true;
            }
        }
    }

    public void setGrindingItem(@Nullable ItemStack stack) {
        this.grindingItem = stack;
        markForUpdate();
    }

    @Nullable
    public ItemStack getGrindingItem() {
        return grindingItem;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        grindingItem = ItemStack.loadItemStackFromNBT(compound);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        if (grindingItem != null) {
            grindingItem.writeToNBT(compound);
        }
    }

}
