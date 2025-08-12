/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.common.FMLCommonHandler;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.base.Mods;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: MiscUtils
 * Created by HellFirePvP
 * Date: 01.08.2016 / 13:38
 */
public class MiscUtils {

    private static Map<EnumDyeColor, Color> prettierColorMapping = new HashMap<>();

    @Nullable
    public static <T> T getTileAt(IBlockAccess world, BlockPos pos, Class<T> tileClass, boolean forceChunkLoad) {
        if (world == null || pos == null) return null; // Duh.
        if (world instanceof World) {
            if (!((World) world).blockExists(pos.getX(), pos.getY(), pos.getZ()) && !forceChunkLoad) return null;
        }
        TileEntity te = world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
        if (te == null) return null;
        if (tileClass.isInstance(te)) return (T) te;
        return null;
    }

    @Nullable
    public static <T> T getRandomEntry(List<T> list, Random rand) {
        if (list == null || list.isEmpty()) return null;
        return list.get(rand.nextInt(list.size()));
    }

    public static <T, V extends Comparable<V>> V getMaxEntry(Collection<T> elements, Function<T, V> valueFunction) {
        V max = null;
        for (T element : elements) {
            V val = valueFunction.apply(element);
            if (max == null || max.compareTo(val) < 0) {
                max = val;
            }
        }
        return max;
    }

    @Nonnull
    public static Color flareColorFromDye(EnumDyeColor color) {
        Color c = prettierColorMapping.get(color);
        if (c == null) c = Color.WHITE;
        return c;
    }

    @Nonnull
    public static ChatFormatting ChatFormattingForDye(EnumDyeColor color) {
        switch (color) {
            case WHITE:
                return ChatFormatting.WHITE;
            case ORANGE:
                return ChatFormatting.GOLD;
            case MAGENTA:
                return ChatFormatting.DARK_PURPLE;
            case LIGHT_BLUE:
                return ChatFormatting.DARK_AQUA;
            case YELLOW:
                return ChatFormatting.YELLOW;
            case LIME:
                return ChatFormatting.GREEN;
            case PINK:
                return ChatFormatting.LIGHT_PURPLE;
            case GRAY:
                return ChatFormatting.DARK_GRAY;
            case SILVER:
                return ChatFormatting.GRAY;
            case CYAN:
                return ChatFormatting.BLUE;
            case PURPLE:
                return ChatFormatting.DARK_PURPLE;
            case BLUE:
                return ChatFormatting.DARK_BLUE;
            case BROWN:
                return ChatFormatting.GOLD;
            case GREEN:
                return ChatFormatting.DARK_GREEN;
            case RED:
                return ChatFormatting.DARK_RED;
            case BLACK:
                return ChatFormatting.DARK_GRAY; // Black is unreadable. fck that.
            default:
                return ChatFormatting.WHITE;
        }
    }

    public static String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return String.valueOf(Character.toTitleCase(str.charAt(0))) + str.substring(1);
    }

    public static boolean breakBlockWithPlayer(BlockPos pos, EntityPlayerMP playerMP) {
        return playerMP.theItemInWorldManager.tryHarvestBlock(pos.getX(), pos.getY(), pos.getZ());
    }

    // Copied from ForgeHooks.onBlockBreak & PlayerInteractionManager.tryHarvestBlock
    // Duplicate break functionality without a active player.
    // Emulates a FakePlayer - attempts without a player as harvester in case a fakeplayer leads to issues.
    public static boolean breakBlockWithoutPlayer(WorldServer world, BlockPos pos) {
        try {
            FakePlayer fp = AstralSorcery.proxy.getASFakePlayerServer(world);
            Block block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            int meta = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ()); // todo fix
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                world,
                block,
                meta,
                fp);
            MinecraftForge.EVENT_BUS.post(event);
            int exp = event.getExpToDrop();
            if (event.isCanceled()) return false;

            // Block Block = world.getBlockState(pos);
            // TileEntity tileentity = world.getTileEntity(x, y, z);
            world.playAuxSFX(2001, pos.getX(), pos.getY(), pos.getZ(), Block.getIdFromBlock(block));
            boolean flag = block.canHarvestBlock(fp, meta);
            boolean flag1 = block.removedByPlayer(world, fp, pos.getX(), pos.getY(), pos.getZ(), flag);
            if (flag1) {
                block.onBlockDestroyedByPlayer(world, pos.getX(), pos.getY(), pos.getZ(), meta);
            }
            if (flag1 && flag) {
                block.harvestBlock(world, fp, pos.getX(), pos.getY(), pos.getZ(), meta);
            }
            if (flag1 && exp > 0) {
                block.dropXpOnBlockBreak(world, pos.getX(), pos.getY(), pos.getZ(), exp);
            }
            return flag1;
        } catch (Exception ignored) {} // Silently fail and propagate it as "can't break this block"
        return false;
    }

    public static void transferEntityTo(Entity entity, int targetDimId, BlockPos targetPos) {
        if (entity.worldObj.isRemote) return; // No transfers on clientside.
        if (entity.worldObj.provider.dimensionId != targetDimId) {
            if (entity instanceof EntityPlayerMP) {
                FMLCommonHandler.instance()
                    .getMinecraftServerInstance()
                    .getConfigurationManager()
                    .transferPlayerToDimension(
                        (EntityPlayerMP) entity,
                        targetDimId,
                        new NoOpTeleporter(((EntityPlayerMP) entity).getServerForPlayer()));
            } else {
                entity.travelToDimension(targetDimId);
            }
        }
        entity.setLocationAndAngles(
            targetPos.getX() + 0.5,
            targetPos.getY(),
            targetPos.getZ() + 0.5,
            entity.rotationYaw,
            entity.rotationPitch);
        entity.worldObj.updateEntityWithOptionalForce(entity, false);
        // entity.setPositionAndUpdate(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);// todo check
        // this
    }

    public static List<Vector3> getCirclePositions(Vector3 centerOffset, Vector3 axis, double radius,
        int amountOfPointsOnCircle) {
        List<Vector3> out = new LinkedList<>();
        Vector3 circleVec = axis.clone()
            .perpendicular()
            .normalize()
            .multiply(radius);
        double degPerPoint = 360D / ((double) amountOfPointsOnCircle);
        for (int i = 0; i < amountOfPointsOnCircle; i++) {
            double deg = i * degPerPoint;
            out.add(
                circleVec.clone()
                    .rotate(Math.toRadians(deg), axis.clone())
                    .add(centerOffset));
        }
        return out;
    }

    @Nullable
    public static MovingObjectPosition rayTraceLook(EntityLivingBase entity, double reachDst) {
        Vec3 pos = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3 lookVec = entity.getLookVec();
        Vec3 end = pos.addVector(lookVec.xCoord * reachDst, lookVec.yCoord * reachDst, lookVec.zCoord * reachDst);
        return entity.worldObj.rayTraceBlocks(pos, end);
    }

    public static Color calcRandomConstellationColor(float perc) {
        return new Color(Color.HSBtoRGB((230F + (50F * perc)) / 360F, 0.8F, 0.8F - (0.3F * perc)));
    }

    public static void applyRandomOffset(Vector3 target, Random rand) {
        applyRandomOffset(target, rand, 1F);
    }

    public static void applyRandomOffset(Vector3 target, Random rand, float multiplier) {
        target.addX(rand.nextFloat() * multiplier * (rand.nextBoolean() ? 1 : -1));
        target.addY(rand.nextFloat() * multiplier * (rand.nextBoolean() ? 1 : -1));
        target.addZ(rand.nextFloat() * multiplier * (rand.nextBoolean() ? 1 : -1));
    }

    public static boolean isChunkLoaded(World world, ChunkCoordIntPair pos) {
        return world.getChunkProvider()
            .loadChunk(pos.chunkXPos, pos.chunkZPos) != null;
    }

    public static boolean isPlayerFakeMP(EntityPlayerMP player) {
        if (player instanceof FakePlayer) return true;

        if (Mods.GALACTICRAFT_CORE.isPresent()) {
            Class<?> plClass = Mods.getGCPlayerClass();
            if (plClass != null) {
                if (player.getClass() != EntityPlayerMP.class && player.getClass() != plClass) return true;
            } else {
                if (player.getClass() != EntityPlayerMP.class) return true;
            }
        } else {
            if (player.getClass() != EntityPlayerMP.class) return true;
        }

        if (player.playerNetServerHandler == null) return true;
        try {
            player.getPlayerIP()
                .length();
            player.playerNetServerHandler.netManager.getSocketAddress()
                .toString();
        } catch (Exception exc) {
            return true;
        }
        if (FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getConfigurationManager() == null) return true;
        return !FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getConfigurationManager()
            .getPlayerList(player.getPlayerIP())
            .contains(player); // todo check this
    }

    public static List<BlockPos> searchAreaFor(World world, BlockPos center, Block blockToSearch, int metaToSearch,
        int radius) {
        List<BlockPos> found = new LinkedList<>();
        for (int xx = -radius; xx <= radius; xx++) {
            for (int yy = -radius; yy <= radius; yy++) {
                for (int zz = -radius; zz <= radius; zz++) {
                    BlockPos pos = center.add(xx, yy, zz);
                    if (isChunkLoaded(world, new ChunkCoordIntPair(pos.chunkX(), pos.chunkZ()))) {
                        Block block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
                        int metadata = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
                        if (block.equals(blockToSearch) && metadata == metaToSearch) {
                            found.add(pos);
                        }
                    }
                }
            }
        }
        return found;
    }

    static {
        prettierColorMapping.put(EnumDyeColor.WHITE, new Color(0xFFFFFF));
        prettierColorMapping.put(EnumDyeColor.ORANGE, new Color(0xFF8C1D));
        prettierColorMapping.put(EnumDyeColor.MAGENTA, new Color(0xEF0EFF));
        prettierColorMapping.put(EnumDyeColor.LIGHT_BLUE, new Color(0x06E5FF));
        prettierColorMapping.put(EnumDyeColor.YELLOW, new Color(0xFFEB00));
        prettierColorMapping.put(EnumDyeColor.LIME, new Color(0x93FF10));
        prettierColorMapping.put(EnumDyeColor.PINK, new Color(0xFF18D9));
        prettierColorMapping.put(EnumDyeColor.GRAY, new Color(0x5E5E5E));
        prettierColorMapping.put(EnumDyeColor.SILVER, new Color(0xBDBDBD));
        prettierColorMapping.put(EnumDyeColor.CYAN, new Color(0x5498B4));
        prettierColorMapping.put(EnumDyeColor.PURPLE, new Color(0xB721F7));
        prettierColorMapping.put(EnumDyeColor.BLUE, new Color(0x3C00FF));
        prettierColorMapping.put(EnumDyeColor.BROWN, new Color(0xB77109));
        prettierColorMapping.put(EnumDyeColor.GREEN, new Color(0x00AA00));
        prettierColorMapping.put(EnumDyeColor.RED, new Color(0xFF0000));
        prettierColorMapping.put(EnumDyeColor.BLACK, new Color(0x000000));
    }

}
