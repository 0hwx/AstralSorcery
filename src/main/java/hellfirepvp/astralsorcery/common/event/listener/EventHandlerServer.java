/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.event.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.render.tile.TESRTranslucentBlock;
import hellfirepvp.astralsorcery.common.block.BlockCustomOre;
import hellfirepvp.astralsorcery.common.block.BlockMachine;
import hellfirepvp.astralsorcery.common.constellation.distribution.ConstellationSkyHandler;
import hellfirepvp.astralsorcery.common.constellation.perk.ConstellationPerk;
import hellfirepvp.astralsorcery.common.constellation.perk.ConstellationPerks;
import hellfirepvp.astralsorcery.common.data.config.Config;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.data.world.WorldCacheManager;
import hellfirepvp.astralsorcery.common.data.world.data.RockCrystalBuffer;
import hellfirepvp.astralsorcery.common.event.BlockModifyEvent;
import hellfirepvp.astralsorcery.common.event.EntityKnockbackEvent;
import hellfirepvp.astralsorcery.common.item.base.ISpecialInteractItem;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.EnchantmentsAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktCraftingTableFix;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.registry.RegistryAchievements;
import hellfirepvp.astralsorcery.common.registry.RegistryPotions;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.data.TickTokenizedMap;
import hellfirepvp.astralsorcery.common.util.data.TimeoutList;
import hellfirepvp.astralsorcery.common.util.data.TimeoutListContainer;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.data.WorldBlockPos;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EventHandlerServer
 * Created by HellFirePvP
 * Date: 07.05.2016 / 01:09
 */
public class EventHandlerServer {

    private static final Random rand = new Random();

    public static boolean isDataInitialized = false;

    public static TickTokenizedMap<WorldBlockPos, TickTokenizedMap.SimpleTickToken<Double>> spawnDenyRegions = new TickTokenizedMap<>(
        TickEvent.Type.SERVER);
    public static TimeoutListContainer<EntityPlayer, Integer> perkCooldowns = new TimeoutListContainer<EntityPlayer, Integer>(
        new ConstellationPerks.PerkTimeoutHandler(),
        TickEvent.Type.SERVER);
    public static TimeoutList<EntityPlayer> invulnerabilityCooldown = new TimeoutList<>(null, TickEvent.Type.SERVER);

    // @SubscribeEvent(priority = EventPriority.LOWEST)
    // public void onLoad(WorldEvent.Load event) {
    // World w = event.world;
    // int id = w.provider.dimensionId;
    // if(!w.isRemote && !isDataInitialized) {
    // //This is kinda an early point in server startup, when it loads the overworld.
    // //Since the FML Server start events are either too early or too late, we do it here.
    //// ServerData.reloadDataFromSaveHandler(w.getSaveHandler());
    // isDataInitialized = true;
    // }
    // if(DataWorldSkyHandlers.hasWorldHandler(id, w.isRemote ? Side.CLIENT : Side.SERVER) &&
    // !Config.weakSkyRendersWhitelist.contains(w.provider.dimensionId)) {
    // AstralSorcery.log.info("[AstralSorcery] Found worldProvider in Dimension " + id + " : " +
    // w.provider.getClass().getName());
    //// w.provider = new WorldProviderBrightnessInj(w, w.provider);
    // AstralSorcery.log.info("[AstralSorcery] Injected WorldProvider into dimension " + id + " (chaining old
    // provider.)");
    // }
    // }

    @SubscribeEvent
    public void onUnload(WorldEvent.Unload event) {
        World w = event.world;
        ConstellationSkyHandler.getInstance()
            .informWorldUnload(w);
        if (w.isRemote) {
            clientUnload();
        }
    }

    @SideOnly(Side.CLIENT)
    private void clientUnload() {
        AstralSorcery.proxy.scheduleClientside(TESRTranslucentBlock::cleanUp);
    }

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        EntityLivingBase living = event.entityLiving;
        if (living == null || living.worldObj.isRemote) return;

        if (!living.isDead && living instanceof EntityPlayer) {
            if (invulnerabilityCooldown.contains((EntityPlayer) living)) {
                event.setCanceled(true);
                return;
            }
        }

        DamageSource source = event.source;
        lblIn: if (source.getSourceOfDamage() != null) {
            EntityPlayer p;
            if (source.getSourceOfDamage() instanceof EntityPlayer) {
                p = (EntityPlayer) source.getSourceOfDamage();
            } else if (source.getSourceOfDamage() instanceof EntityArrow) {
                Entity shooter = ((EntityArrow) source.getSourceOfDamage()).shootingEntity;
                if (shooter != null && shooter instanceof EntityPlayer) {
                    p = (EntityPlayer) shooter;
                } else {
                    break lblIn;
                }
            } else {
                break lblIn;
            }
            PlayerProgress prog = ResearchManager.getProgress(p);
            if (prog != null) {
                float dmg = event.ammount;
                Map<ConstellationPerk, Integer> perks = prog.getAppliedPerks();
                for (ConstellationPerk perk : perks.keySet()) {
                    if (!prog.isPerkActive(perk)) continue;
                    if (perk.mayExecute(ConstellationPerk.Target.ENTITY_ATTACK)) {
                        dmg = perk.onEntityAttack(p, event.entityLiving, dmg);
                    }
                }
                event.ammount = dmg;
            }
        }
        if (event.entityLiving != null && event.entityLiving instanceof EntityPlayer) {
            EntityPlayer hurt = (EntityPlayer) event.entityLiving;
            PlayerProgress prog = ResearchManager.getProgress(hurt);
            if (prog != null) {
                float dmg = event.ammount;
                Map<ConstellationPerk, Integer> perks = prog.getAppliedPerks();
                for (ConstellationPerk perk : perks.keySet()) {
                    if (!prog.isPerkActive(perk)) continue;
                    if (perk.mayExecute(ConstellationPerk.Target.ENTITY_HURT)) {
                        dmg = perk.onEntityHurt(hurt, source, dmg);
                    }
                }
                event.ammount = dmg;
            }
        }
    }

    @SubscribeEvent
    public void onTarget(LivingSetAttackTargetEvent event) {
        EntityLivingBase living = event.target;
        if (living != null && !living.isDead && living instanceof EntityPlayer) {
            if (invulnerabilityCooldown.contains((EntityPlayer) living)) {
                event.entityLiving.setRevengeTarget(null);
                if (event.entityLiving instanceof EntityLiving) {
                    ((EntityLiving) event.entityLiving).setAttackTarget(null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onKnockback(EntityKnockbackEvent event) {
        Entity attacker = event.getAttacker();
        if (attacker == null || attacker.worldObj.isRemote) return;

        if (attacker instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) attacker;
            PlayerProgress prog = ResearchManager.getProgress(p);
            if (prog != null) {
                Map<ConstellationPerk, Integer> perks = prog.getAppliedPerks();
                for (ConstellationPerk perk : perks.keySet()) {
                    if (!prog.isPerkActive(perk)) continue;
                    if (perk.mayExecute(ConstellationPerk.Target.ENTITY_KNOCKBACK)) {
                        perk.onEntityKnockback(p, event.entityLiving);
                    }
                }
            }
        }
    }

    // @SubscribeEvent
    // public void onContainerOpen(PlayerOpenContainerEvent event) {
    // if(event.getContainer() instanceof ContainerWorkbench && !event.getEntityPlayer().world.isRemote &&
    // event.getEntityPlayer() instanceof EntityPlayerMP) {
    // PacketChannel.CHANNEL.sendTo(new PktCraftingTableFix(((ContainerWorkbench) event.getContainer()).pos),
    // (EntityPlayerMP) event.getEntityPlayer());
    // }
    // }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDeath(LivingDeathEvent event) {
        if (phoenixProtect(event.entityLiving)) {
            event.setCanceled(true);
        } else {
            if (event.entityLiving == null || event.entityLiving.worldObj.isRemote) return;

            DamageSource source = event.source;
            if (source.getEntity() != null && source.getEntity() instanceof EntityPlayer) {
                EntityPlayer p = (EntityPlayer) source.getEntity();
                PlayerProgress prog = ResearchManager.getProgress(p);
                if (prog != null) {
                    Map<ConstellationPerk, Integer> perks = prog.getAppliedPerks();
                    for (ConstellationPerk perk : perks.keySet()) {
                        if (!prog.isPerkActive(perk)) continue;
                        if (perk.mayExecute(ConstellationPerk.Target.ENTITY_KILL)) {
                            perk.onEntityKilled(p, event.entityLiving);
                        }
                    }
                }
            }
        }
    }

    private boolean phoenixProtect(EntityLivingBase entity) {
        PotionEffect pe = entity.getActivePotionEffect(RegistryPotions.potionCheatDeath);
        if (pe != null) {
            int level = pe.getAmplifier();
            phoenixEffects(entity, level);
            return true;
        }
        return false;
    }

    private void phoenixEffects(EntityLivingBase entity, int level) {
        entity.setHealth(6 + level * 2);
        entity.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 200, 2, false));
        entity.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), 500, 1, false));
        List<EntityLivingBase> others = entity.worldObj
            .getEntitiesWithinAABB(EntityLivingBase.class, entity.boundingBox.expand(3, 3, 3)); // , (e) -> !e.isDead &&
                                                                                                // e != entity);
        for (EntityLivingBase lb : others) {
            lb.setFire(16);
            lb.knockBack(entity, 2F, lb.posX - entity.posX, lb.posZ - entity.posZ);
        }
        PktParticleEvent ev = new PktParticleEvent(
            PktParticleEvent.ParticleEventType.PHOENIX_PROC,
            new Vector3(entity.posX, entity.posY, entity.posZ));
        BlockPos getPosition = new BlockPos(entity).getPosition();; // getPosition
        PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(entity.worldObj, getPosition, 32));

        MinecraftServer server = MinecraftServer.getServer();
        if (server != null) {
            entity.removePotionEffect(RegistryPotions.potionCheatDeath.getId());
        }
    }

    @SubscribeEvent
    public void onSpawnTest(LivingSpawnEvent.CheckSpawn event) {
        if (event.getResult() == Event.Result.DENY) return; // Already denied anyway.

        EntityLivingBase toTest = event.entityLiving;
        Vector3 at = new Vector3(toTest);
        boolean mayDeny = Config.doesMobSpawnDenyDenyEverything
            || toTest.isCreatureType(EnumCreatureType.monster, false);
        if (mayDeny) {
            for (Map.Entry<WorldBlockPos, TickTokenizedMap.SimpleTickToken<Double>> entry : spawnDenyRegions
                .entrySet()) {
                if (!entry.getKey()
                    .getWorld()
                    .equals(toTest.worldObj)) continue;
                if (at.distance(entry.getKey()) <= entry.getValue()
                    .getValue()) {
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {
        ItemStack hand = event.entityPlayer.getHeldItem();
        // if(event.getHand() == EnumHand.OFF_HAND) {
        // hand = event.entityPlayer.getHeldItem(EnumHand.MAIN_HAND);
        // }
        if (hand == null) return;
        if (hand.getItem() instanceof ISpecialInteractItem) {
            ISpecialInteractItem i = (ISpecialInteractItem) hand.getItem();
            BlockPos pos = new BlockPos(event.x, event.y, event.z);
            if (i.needsSpecialHandling(event.world, pos, event.entityPlayer, hand)) {
                i.onRightClick(event.world, pos, event.entityPlayer, event.face, hand);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRightClickLast(PlayerInteractEvent event) {
        if (!event.world.isRemote) {
            Block interacted = event.world.getBlock(event.x, event.y, event.z);
            if (interacted instanceof BlockWorkbench) {
                BlockPos at = new BlockPos(event.x, event.y, event.z);
                PktCraftingTableFix fix = new PktCraftingTableFix(at);
                PacketChannel.CHANNEL.sendTo(fix, (EntityPlayerMP) event.entityPlayer);
            }
        }
    }

    @SubscribeEvent
    public void onCraft(PlayerEvent.ItemCraftedEvent event) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server != null) {
            ResearchManager.informCraftingGridCompletion(event.player, event.crafting);

            Item crafted = event.crafting.getItem();
            Block blockCrafted = Block.getBlockFromItem(crafted);
            if (blockCrafted instanceof BlockMachine) {
                if (event.crafting.getItemDamage() == BlockMachine.MachineType.TELESCOPE.getMeta()) {
                    event.player.addStat(RegistryAchievements.achvBuildActTelescope, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (Config.giveJournalFirst) {
            EntityPlayer pl = event.player;
            if (!ResearchManager.doesPlayerFileExist(pl)) {
                pl.inventory.addItemStackToInventory(new ItemStack(ItemsAS.journal));
            }
        }
        ResearchManager.loadPlayerKnowledge(event.player);
        ResearchManager.savePlayerKnowledge(event.player);
    }

    @SubscribeEvent
    public void onSave(WorldEvent.Save event) {
        WorldCacheManager.getInstance()
            .doSave(event.world);
    }

    @SubscribeEvent
    public void onChange(BlockModifyEvent event) {
        if (event.getWorld().isRemote) return;
        if (!Loader.instance()
            .hasReachedState(LoaderState.SERVER_ABOUT_TO_START)) {
            return; // Thanks BuildCraft.
        }
        BlockPos at = event.getPos();
        WorldNetworkHandler.getNetworkHandler(event.getWorld())
            .informBlockChange(at);

        if (event.getNewBlock()
            .equals(Blocks.crafting_table)) {
            if (!event.getOldBlock()
                .equals(Blocks.crafting_table)) {
                WorldNetworkHandler.getNetworkHandler(event.getWorld())
                    .informTablePlacement(at);
            }
        }
        if (event.getOldBlock()
            .equals(Blocks.crafting_table)) {
            if (!event.getNewBlock()
                .equals(Blocks.crafting_table)) {
                WorldNetworkHandler.getNetworkHandler(event.getWorld())
                    .informTableRemoval(at);
            }
        }
        if (event.getOldBlock()
            .equals(BlocksAS.customOre)) {
            Block oldState = event.getOldBlock();
            if (oldState.equals(BlockCustomOre.OreType.ROCK_CRYSTAL)) {// oldState.getValue(BlockCustomOre.ORE_TYPE).equals(BlockCustomOre.OreType.ROCK_CRYSTAL))
                                                                       // {
                ((RockCrystalBuffer) WorldCacheManager
                    .getOrLoadData(event.getWorld(), WorldCacheManager.SaveKey.ROCK_CRYSTAL)).removeOre(event.getPos());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlace(BlockEvent.PlaceEvent event) {
        if (event.world.isRemote) return;
        BlockPos at = new BlockPos(event.x, event.y, event.z);

        if (event.placedBlock.equals(Blocks.crafting_table)) {
            WorldNetworkHandler.getNetworkHandler(event.world)
                .informTablePlacement(at);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreak(BlockEvent.BreakEvent event) {
        if (event.world.isRemote) return;
        BlockPos at = new BlockPos(event.x, event.y, event.z);

        if (event.block.equals(Blocks.crafting_table)) {
            WorldNetworkHandler.getNetworkHandler(event.world)
                .informTableRemoval(at);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onHarvest(BlockEvent.HarvestDropsEvent event) {
        if (event.harvester != null && !event.isSilkTouching) {
            ItemStack main = event.harvester.getHeldItem();
            if (main != null && main.getItem() != null) {
                if (EnchantmentHelper.getEnchantmentLevel(EnchantmentsAS.enchantmentScorchingHeat.effectId, main) > 0) {
                    int fortuneLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, main);
                    List<ItemStack> dropsCopy = new LinkedList<>();
                    dropsCopy.addAll(event.drops);
                    event.drops.clear();
                    for (ItemStack stack : dropsCopy) {
                        ItemStack out = FurnaceRecipes.smelting()
                            .getSmeltingResult(stack);
                        if (out != null && out.getItem() != null) {
                            ItemStack furnaced = ItemUtils.copyStackWithSize(out, 1);
                            event.drops.add(furnaced);
                            furnaced.onCrafting(event.world, event.harvester, 1);
                            FMLCommonHandler.instance()
                                .firePlayerSmeltedEvent(event.harvester, furnaced);
                            if (fortuneLvl > 0 && !(out.getItem() instanceof ItemBlock)) {
                                for (int i = 0; i < fortuneLvl; i++) {
                                    if (rand.nextFloat() < 0.5F) {
                                        event.drops.add(ItemUtils.copyStackWithSize(out, 1));
                                    }
                                }
                            }
                        } else {
                            event.drops.add(stack);
                        }
                    }
                }
            }
        }
    }

    /*
     * @SubscribeEvent
     * public void onJoin(EntityJoinWorldEvent event) {
     * if (event.getWorld().isRemote) return;
     * Entity joined = event.getEntity();
     * if (joined instanceof EntityItem && !(joined instanceof EntityItemHighlighted)) {
     * EntityItem ei = (EntityItem) joined;
     * if (ei.getEntityItem() != null && (ei.getEntityItem().getAttItem() instanceof ItemHighlighted)) {
     * ei.setDead();
     * EntityItemHighlighted newItem = new EntityItemHighlighted(ei.world, ei.posX, ei.posY, ei.posZ,
     * ei.getEntityItem());
     * ItemHighlighted i = (ItemHighlighted) ei.getEntityItem().getAttItem();
     * newItem.applyColor(i.getHightlightColor(ei.getEntityItem()));
     * newItem.motionX = ei.motionX;
     * newItem.motionY = ei.motionY;
     * newItem.motionZ = ei.motionZ;
     * newItem.hoverStart = ei.hoverStart;
     * newItem.setPickupDelay(40);
     * event.getWorld().spawnEntityInWorld(newItem);
     * event.setCanceled(true);
     * }
     * }
     * }
     */

}
