package hellfirepvp.astralsorcery.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlayerUtil {

    public static EntityPlayerMP getPlayerByUUID(UUID theUUID) {
        final List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        EntityPlayerMP entityplayermp;
        for (int i = players.size() - 1; i >= 0; --i) {
            entityplayermp = players.get(i);

            if (entityplayermp.getUniqueID().equals(theUUID)) {
                return entityplayermp;
            }
        }
        return null;
    }

    public static boolean isPlayerOnline(EntityPlayerMP player) {
        return MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
    }
}
