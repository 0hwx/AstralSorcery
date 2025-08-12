package hellfirepvp.astralsorcery.common.util;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerUtil {

    public static EntityPlayerMP getPlayerByUUID(UUID theUUID) {
        final List<EntityPlayerMP> players = MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList;
        EntityPlayerMP entityplayermp;
        for (int i = players.size() - 1; i >= 0; --i) {
            entityplayermp = players.get(i);

            if (entityplayermp.getUniqueID()
                .equals(theUUID)) {
                return entityplayermp;
            }
        }
        return null;
    }

    public static boolean isPlayerOnline(EntityPlayerMP player) {
        return MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList.contains(player);
    }
}
