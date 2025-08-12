/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.auxiliary.tick.TickManager;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.network.packet.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.tile.network.TileCrystalLens;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.CropHelper;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.TickTokenizedMap;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemColoredLens
 * Created by HellFirePvP
 * Date: 29.11.2016 / 12:35
 */
public class ItemColoredLens extends Item {

    public ItemColoredLens() {
        setMaxStackSize(16);
        setHasSubtypes(true);
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
        setUnlocalizedName("ItemColoredLens");
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (ColorType ct : ColorType.values()) {
            subItems.add(new ItemStack(itemIn, 1, ct.getMeta()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemColoredLens) {
            int dmg = stack.getItemDamage();
            if (dmg >= 0 && dmg < ColorType.values().length) {
                tooltip.add(
                    I18n.format(
                        "item.ItemColoredLens.effect." + ColorType.values()[dmg].name()
                            .toLowerCase() + ".name"));
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack inHand = playerIn.getHeldItem();
            BlockPos pos = new BlockPos(x, y, z);
            ColorType type = null;
            if (inHand != null && inHand.getItem() != null && inHand.getItem() instanceof ItemColoredLens) {
                int dmg = inHand.getItemDamage();
                if (dmg >= 0 && dmg < ColorType.values().length) {
                    type = ColorType.values()[dmg];
                }
            }
            if (type != null) {
                TileCrystalLens lens = MiscUtils.getTileAt(worldIn, pos, TileCrystalLens.class, true);
                if (lens != null) {
                    ColorType oldType = lens.setLensColor(type);
                    if (!playerIn.capabilities.isCreativeMode) {
                        inHand.stackSize--;
                        if (inHand.stackSize <= 0) {
                            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                        }
                    }
                    // SoundHelper.playSoundAround(Sounds.clipSwitch, worldIn, pos, 0.8F, 1.5F);
                    if (oldType != null) {
                        ItemUtils.dropItem(
                            worldIn,
                            pos.getX() + hitX,
                            pos.getY() + hitY,
                            pos.getZ() + hitZ,
                            oldType.asStack());
                    }
                }
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int tintIndex) {
        int dmg = stack.getItemDamage();
        if (dmg < 0 || dmg >= ColorType.values().length) return 0xFFFFFFFF;
        return ColorType.values()[dmg].colorRGB;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        Item i = stack.getItem();
        if (i instanceof ItemCraftingComponent) {
            ColorType type = ColorType.values()[stack.getItemDamage()];
            return super.getUnlocalizedName(stack) + ".effect" + type.getUnlocalizedName();
        }
        return super.getUnlocalizedName(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("astralsorcery:glass_lens_coloured");
    }
    // @Override
    // public int getColorForItemStack(ItemStack stack, int tintIndex) {
    // int dmg = stack.getItemDamage();
    // if(dmg < 0 || dmg >= ColorType.values().length) return 0xFFFFFFFF;
    // return ColorType.values()[dmg].colorRGB;
    // }

    public static enum ColorType {

        FIRE(TargetType.ENTITY, 0xff7f00, 0.07F),
        BREAK(TargetType.BLOCK, 0xffdf00, 0.07F),
        GROW(TargetType.BLOCK, 0x00df00, 0.07F),
        DAMAGE(TargetType.ENTITY, 0xdf0000, 0.07F),
        REGEN(TargetType.ENTITY, 0xff7fbf, 0.07F),
        PUSH(TargetType.ENTITY, 0x00dfff, 0.07F),

        SPECTRAL(TargetType.NONE, 0x7f00bf, 0.25F);

        private static final Map<Integer, TickTokenizedMap<BlockPos, BreakEntry>> breakMap = new HashMap<>();

        public final int colorRGB;
        public final Color wrappedColor;
        private final float flowReduction;
        private final TargetType type;

        private ColorType(TargetType type, int colorRGB, float flowReduction) {
            this.type = type;
            this.colorRGB = colorRGB;
            this.wrappedColor = new Color(colorRGB);
            this.flowReduction = flowReduction;
        }

        public TargetType getType() {
            return type;
        }

        public float getFlowReduction() {
            return flowReduction;
        }

        public String getUnlocalizedName() {
            return name().toLowerCase();
        }

        public ItemStack asStack() {
            return new ItemStack(ItemsAS.coloredLens, 1, getMeta());
        }

        public int getMeta() {
            return ordinal();
        }

        public void onEntityInBeam(Vector3 beamOrigin, Vector3 beamTarget, Entity entity, float percStrength) {
            switch (this) {
                case FIRE:
                    if (itemRand.nextFloat() > percStrength) return;
                    if (entity instanceof EntityItem) {
                        ItemStack current = ((EntityItem) entity).getEntityItem();
                        ItemStack result = FurnaceRecipes.smelting()
                            .getSmeltingResult(current);
                        if (result != null && result.getItem() != null) {
                            Vector3 entityPos = new Vector3(entity);
                            ItemUtils.dropItemNaturally(
                                entity.worldObj,
                                entityPos.getX(),
                                entityPos.getY(),
                                entityPos.getZ(),
                                ItemUtils.copyStackWithSize(result, 1));
                            if (current.stackSize > 1) {
                                current.stackSize--;
                                ((EntityItem) entity).setEntityItemStack(current);
                            } else {
                                entity.setDead();
                            }
                        }
                    } else if (entity instanceof EntityLivingBase) {
                        entity.setFire(1);
                    }
                    break;
                case DAMAGE:
                    if (!(entity instanceof EntityLivingBase)) return;
                    if (itemRand.nextFloat() > percStrength) return;
                    if (entity instanceof EntityPlayer && MinecraftServer.getServer() != null
                        && MinecraftServer.getServer()
                            .isPVPEnabled())
                        return;
                    entity.attackEntityFrom(CommonProxy.dmgSourceStellar, 6.5F);
                    break;
                case REGEN:
                    if (!(entity instanceof EntityLivingBase)) return;
                    if (itemRand.nextFloat() > percStrength) return;
                    ((EntityLivingBase) entity).heal(3.5F);
                    break;
                case PUSH:
                    if (entity instanceof EntityPlayer || itemRand.nextFloat() > percStrength) return;
                    Vector3 dir = beamTarget.clone()
                        .subtract(beamOrigin)
                        .normalize()
                        .multiply(0.5F);
                    entity.motionX = Math.min(1F, entity.motionZ + dir.getX());
                    entity.motionY = Math.min(1F, entity.motionY + dir.getY());
                    entity.motionZ = Math.min(1F, entity.motionZ + dir.getZ());
                    break;
            }
        }

        public void onBlockOccupyingBeam(World world, BlockPos at, Block block, float percStrength) {
            switch (this) {
                case BREAK:
                    float hardness = block.getBlockHardness(world, at.getX(), at.getY(), at.getZ());
                    if (hardness < 0) return;
                    hardness *= 1.5F;
                    addProgress(world, at, hardness, percStrength * 4F);
                    PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.EffectType.BEAM_BREAK, at);
                    pkt.data = Block.getIdFromBlock(block);
                    PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(world, at, 16));
                    break;
                case GROW:
                    if (world.rand.nextFloat() > percStrength) return;
                    CropHelper.GrowablePlant plant = CropHelper.wrapPlant(world, at);
                    if (plant != null) {
                        plant.tryGrow(world, world.rand);
                        PktParticleEvent packet = new PktParticleEvent(
                            PktParticleEvent.ParticleEventType.CE_CROP_INTERACT,
                            at);
                        PacketChannel.CHANNEL.sendToAllAround(packet, PacketChannel.pointFromPos(world, at, 16));
                    }
                    break;
                /*
                 * case HARVEST:
                 * if(world.rand.nextFloat() > percStrength) return;
                 * CropHelper.HarvestablePlant harvest = CropHelper.wrapHarvestablePlant(world, at);
                 * if(harvest != null) {
                 * harvest.tryGrow(world, world.rand);
                 * if(harvest.canHarvest(world)) {
                 * List<ItemStack> drops = harvest.harvestDropsAndReplant(world, world.rand, 4);
                 * for (ItemStack st : drops) {
                 * ItemUtils.dropItemNaturally(world, at.getX() + 0.5, at.getY() + 0.5, at.getZ() + 0.5, st);
                 * }
                 * }
                 * PktParticleEvent packet = new PktParticleEvent(PktParticleEvent.ParticleEventType.CE_CROP_INTERACT,
                 * at);
                 * PacketChannel.CHANNEL.sendToAllAround(packet, PacketChannel.pointFromPos(world, at, 16));
                 * }
                 * break;
                 */
            }
        }

        private void addProgress(World world, BlockPos pos, float expectedHardness, float percStrength) {
            TickTokenizedMap<BlockPos, BreakEntry> map = breakMap.get(world.provider.dimensionId);
            if (map == null) {
                map = new TickTokenizedMap<>(TickEvent.Type.SERVER);
                TickManager.getInstance()
                    .register(map);
                breakMap.put(world.provider.dimensionId, map);
            }

            BreakEntry breakProgress = map.get(pos);
            if (breakProgress == null) {
                breakProgress = new BreakEntry(
                    expectedHardness,
                    world,
                    pos,
                    world.getBlock(pos.getX(), pos.getY(), pos.getZ()));
                map.put(pos, breakProgress);
            }

            breakProgress.breakProgress -= percStrength;
            breakProgress.idleTimeout = 0;
        }

        @SideOnly(Side.CLIENT)
        public static void blockBreakAnimation(PktPlayEffect pktPlayEffect) {
            RenderingUtils.playBlockBreakParticles(pktPlayEffect.pos, Block.getBlockById(pktPlayEffect.data));
        }

    }

    private static class BreakEntry implements TickTokenizedMap.TickMapToken<Float> {

        private float breakProgress;
        private final World world;
        private final BlockPos pos;
        private final Block expected;

        private int idleTimeout;

        public BreakEntry(@Nonnull Float value, World world, BlockPos at, Block expectedToBreak) {
            this.breakProgress = value;
            this.world = world;
            this.pos = at;
            this.expected = expectedToBreak;
        }

        @Override
        public int getRemainingTimeout() {
            return (breakProgress <= 0 || idleTimeout >= 20) ? 0 : 1;
        }

        @Override
        public void tick() {
            idleTimeout++;
        }

        @Override
        public void onTimeout() {
            if (breakProgress > 0) return;

            Block nowAt = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
            int meta = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
            if (nowAt.equals(expected) && nowAt.damageDropped(meta) == expected.damageDropped(meta)) {
                MiscUtils.breakBlockWithoutPlayer((WorldServer) world, pos);
            }
        }

        @Override
        public Float getValue() {
            return breakProgress;
        }

    }

    // Respectively only Entity-checks or only block-checks will be done.
    public static enum TargetType {

        ENTITY,
        BLOCK,
        NONE

    }

}
