/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

import javax.annotation.Nullable;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ExtendedChainingPlayerController
 * Created by HellFirePvP
 * Date: 23.12.2016 / 11:32
 */
public class ExtendedChainingPlayerController extends PlayerControllerMP {

    private final PlayerControllerMP delegate;
    private float reachModifier = 0.0F;

    public ExtendedChainingPlayerController(PlayerControllerMP oldCtrl) {
        super(Minecraft.getMinecraft(), oldCtrl.netClientHandler);
        this.delegate = oldCtrl;
    }

    public void setReachModifier(float reachModifier) {
        this.reachModifier = reachModifier;
    }

    @Override
    public void setPlayerCapabilities(EntityPlayer player) {
        delegate.setPlayerCapabilities(player);
    }

//    @Override
//    public boolean isSpectator() {
//        return delegate.isSpectator();
//    }

    @Override
    public void setGameType(WorldSettings.GameType type) {
        delegate.setGameType(type);
    }

    @Override
    public void flipPlayer(EntityPlayer playerIn) {
        delegate.flipPlayer(playerIn);
    }

    @Override
    public boolean shouldDrawHUD() {
        return delegate.shouldDrawHUD();
    }

    @Override
    public boolean onPlayerDestroyBlock(int x, int y, int z, int side) {
        return delegate.onPlayerDestroyBlock(x, y, z, side);
    }

    @Override
    public void clickBlock(int x, int y, int z, int side) {
         delegate.clickBlock(x, y, z, side);
    }

    @Override
    public void resetBlockRemoving() {
        delegate.resetBlockRemoving();
    }

    @Override
    public void onPlayerDamageBlock(int x, int y, int z, int side) {
        delegate.onPlayerDamageBlock(x, y, z, side);
    }

    @Override
    public float getBlockReachDistance() {
        return delegate.getBlockReachDistance() + reachModifier;
    }

    @Override
    public void updateController() {
        delegate.updateController();
    }

//    @Override
//    public boolean getIsHittingBlock() {
//        return delegate.getIsHittingBlock();
//    }

    @Override
    public boolean sendUseItem(EntityPlayer player, World worldIn, ItemStack stack) {
        return delegate.sendUseItem(player, worldIn, stack);
    }

    @Override
    public boolean onPlayerRightClick(EntityPlayer player, World worldIn, @Nullable ItemStack stack, int x, int y, int z, int side, Vec3 hitVector){
        return delegate.onPlayerRightClick(player, worldIn, stack, x, y, z, side, hitVector);
    }

    @Override
    public EntityClientPlayerMP func_147493_a(World worldIn, StatFileWriter statWriter) {
        return delegate.func_147493_a(worldIn, statWriter);
    }

    @Override
    public void attackEntity(EntityPlayer playerIn, Entity targetEntity) {
        delegate.attackEntity(playerIn, targetEntity);
    }

    @Override
    public boolean interactWithEntitySendPacket(EntityPlayer player, Entity target) {
        return delegate.interactWithEntitySendPacket(player, target);
    }

//    @Override
//    public EnumActionResult interactWithEntity(EntityPlayer player, Entity target, RayTraceResult raytrace, @Nullable ItemStack heldItem, EnumHand hand) {
//        return delegate.interactWithEntity(player, target, raytrace, heldItem, hand);
//    }

    @Override
    public ItemStack windowClick(int windowId, int slotId, int mouseButton, int type, EntityPlayer player) {
        return delegate.windowClick(windowId, slotId, mouseButton, type, player);
    }

    @Override
    public void sendEnchantPacket(int windowID, int button) {
        delegate.sendEnchantPacket(windowID, button);
    }

    @Override
    public void sendSlotPacket(ItemStack itemStackIn, int slotId) {
        delegate.sendSlotPacket(itemStackIn, slotId);
    }

    @Override
    public void sendPacketDropItem(ItemStack itemStackIn) {
        delegate.sendPacketDropItem(itemStackIn);
    }

    @Override
    public void onStoppedUsingItem(EntityPlayer playerIn) {
        delegate.onStoppedUsingItem(playerIn);
    }

    @Override
    public boolean gameIsSurvivalOrAdventure() {
        return delegate.gameIsSurvivalOrAdventure();
    }

    @Override
    public boolean isInCreativeMode() {
        return delegate.isInCreativeMode();
    }

    @Override
    public boolean isNotCreative() {
        return delegate.isNotCreative();
    }

    @Override
    public boolean func_110738_j() {
        return delegate.func_110738_j();
    }

//    @Override
//    public boolean isSpectatorMode() {
//        return delegate.isSpectatorMode();
//    }

    @Override
    public boolean extendedReach() {
        return delegate.extendedReach();
    }

//    @Override
//    public GameType getCurrentGameType() {
//        return delegate.getCurrentGameType();
//    }
//
//    @Override
//    public void pickItem(int index) {
//        delegate.pickItem(index);
//    }
}
