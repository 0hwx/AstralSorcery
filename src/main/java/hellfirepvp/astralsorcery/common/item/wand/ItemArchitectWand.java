/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.item.wand;

import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.client.event.ClientRenderEventHandler;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.TextureHelper;
import hellfirepvp.astralsorcery.common.base.Mods;
import hellfirepvp.astralsorcery.common.data.config.Config;
import hellfirepvp.astralsorcery.common.integrations.ModIntegrationBotania;
import hellfirepvp.astralsorcery.common.item.ItemAlignmentChargeConsumer;
import hellfirepvp.astralsorcery.common.item.ItemBlockStorage;
import hellfirepvp.astralsorcery.common.item.ItemHandRender;
import hellfirepvp.astralsorcery.common.item.ItemHudRender;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import hellfirepvp.astralsorcery.common.util.BlockPos;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemArchitectWand
 * Created by HellFirePvP
 * Date: 06.02.2017 / 22:49
 */
public class ItemArchitectWand extends ItemBlockStorage implements ItemHandRender, ItemHudRender, ItemAlignmentChargeConsumer {

    private static final double architectRange = 60.0D;

    public ItemArchitectWand() {
        setMaxDamage(0);
        setMaxStackSize(1);
        setUnlocalizedName("ItemArchitectWand");
        setCreativeTab(RegistryItems.creativeTabAstralSorcery);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onRenderInHandHUD(ItemStack lastCacheInstance, float fadeAlpha, float pTicks) {
        ItemStack blockStackStored = getStoredStateAsStack(lastCacheInstance);
        if(blockStackStored == null) return;

        int amtFound = 0;
        if(Mods.BOTANIA.isPresent()) {
            amtFound = ModIntegrationBotania.getItemCount(Minecraft.getMinecraft().thePlayer, lastCacheInstance, ItemUtils.createBlockState(blockStackStored));
        } else {
            Collection<ItemStack> stacks = ItemUtils.scanInventoryForMatching(Minecraft.getMinecraft().thePlayer.inventory, blockStackStored, false);
            for (ItemStack stack : stacks) {
                amtFound += stack.stackSize;
            }
        }

        int height  =  26;
        int width   =  26;
        int offsetX =  30;
        int offsetY =  15;

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        Blending.DEFAULT.apply();
        ClientRenderEventHandler.texHUDItemFrame.bind();

        GL11.glColor4f(1F, 1F, 1F, fadeAlpha * 0.9F);
        Tessellator tess = Tessellator.instance;
//        VertexBuffer vb = tes.getBuffer();

        tess.startDrawingQuads();
        tess.addVertexWithUV(offsetX, offsetY + height, 10, 0, 1);
        tess.addVertexWithUV(offsetX + width, offsetY + height, 10, 1, 1);
        tess.addVertexWithUV(offsetX + width, offsetY, 10, 1, 0);
        tess.addVertexWithUV(offsetX, offsetY, 10, 0, 0);
        tess.draw();
//        vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//        vb.pos(offsetX,         offsetY + height, 10).tex(0, 1).endVertex();
//        vb.pos(offsetX + width, offsetY + height, 10).tex(1, 1).endVertex();
//        vb.pos(offsetX + width, offsetY,          10).tex(1, 0).endVertex();
//        vb.pos(offsetX,         offsetY,          10).tex(0, 0).endVertex();
//        tes.draw();

        TextureHelper.refreshTextureBindState();
        TextureHelper.setActiveTextureToAtlasSprite();

        RenderHelper.enableGUIStandardItemLighting();
        RenderItem ri = new RenderItem();
        Minecraft mc = Minecraft.getMinecraft();
        ri.renderItemAndEffectIntoGUI(mc.fontRenderer,mc.renderEngine, blockStackStored, offsetX + 5, offsetY + 5);
        RenderHelper.disableStandardItemLighting();
        GL11.glEnable(GL11.GL_ALPHA_TEST); //Because Mc item rendering..

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslated(offsetX + 14, offsetY + 16, 0);
        String amtString = String.valueOf(amtFound);
        if(amtFound == -1) {
            amtString = "∞";
        }
        GL11.glTranslated(-Minecraft.getMinecraft().fontRenderer.getStringWidth(amtString) / 3, 0, 0);
        GL11.glScaled(0.7, 0.7, 0.7);
        if(amtString.length() > 3) {
            GL11.glScaled(0.9, 0.9, 0.9);
        }
        int c = 0x00DDDDDD;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(amtString, 0, 0, c);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        TextureHelper.refreshTextureBindState();

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onRenderWhileInHand(ItemStack stack, float pTicks) {
        Block stored = getStoredState(stack,stack.getItemDamage());
        if(stored == null || stored.equals(Blocks.air)) return;

        Deque<BlockPos> placeable = filterBlocksToPlace(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, architectRange);
        if(!placeable.isEmpty()) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix();
            boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_BLEND);
            Blending.ADDITIVEDARK.apply();
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            RenderingUtils.removeStandartTranslationFromTESRMatrix(pTicks);
            World w = Minecraft.getMinecraft().theWorld;

            Tessellator tess = Tessellator.instance;
//            VertexBuffer vb = tes.getBuffer();
//            vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            tess.startDrawingQuads();
            for (BlockPos pos : placeable) {
//                Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(stored, pos, w, vb); //todo fix this
            }
            //tess.getVertexState((float) TileEntityRendererDispatcher.staticPlayerX, (float) TileEntityRendererDispatcher.staticPlayerY, (float) TileEntityRendererDispatcher.staticPlayerZ);
            tess.draw();
            Blending.DEFAULT.apply();
            if(!blend) {
                GL11.glDisable(GL11.GL_BLEND);
            }
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn) {
        if (world.isRemote) return stack;

        Block stored = getStoredState(stack,stack.getItemDamage());
        ItemStack consumeStack = getStoredStateAsStack(stack);
        if(stored == null || stored.equals(Blocks.air) || consumeStack == null) return stack;

        Deque<BlockPos> placeable = filterBlocksToPlace(playerIn, world, architectRange);
        if(!placeable.isEmpty()) {
            for (BlockPos placePos : placeable) {
                if(drainTempCharge(playerIn, Config.architectWandUseCost, true)
                        && (playerIn.capabilities.isCreativeMode || ItemUtils.consumeFromPlayerInventory(playerIn, stack, ItemUtils.copyStackWithSize(consumeStack, 1), true))) {
                    drainTempCharge(playerIn, Config.architectWandUseCost, false);
                    gainPermCharge(playerIn, Config.architectWandUseCost / 4);
                    if(!playerIn.capabilities.isCreativeMode) {
                        ItemUtils.consumeFromPlayerInventory(playerIn, stack, ItemUtils.copyStackWithSize(consumeStack, 1), false);
                    }
                    world.setBlock(placePos.getX(), placePos.getY(), placePos.getZ(), stored);
                    PktParticleEvent ev = new PktParticleEvent(PktParticleEvent.ParticleEventType.ARCHITECT_PLACE, placePos);
                    ev.setAdditionalData(Block.getIdFromBlock(stored));
                    PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(world, placePos, 40));
                }
            }
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(playerIn.isSneaking()) {
            BlockPos pos = new BlockPos(x, y, z);
            tryStoreBlock(stack, world, pos);
            return true;
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    public static void playArchitectPlaceEvent(PktParticleEvent event) {
        AstralSorcery.proxy.scheduleClientside(() -> {
            Vector3 at = event.getVec();
            Block state = Block.getBlockById((int) Math.round(event.getAdditionalData()));
            RenderingUtils.playBlockBreakParticles(at.toBlockPos(), state);
            for (int i = 0; i < 9; i++) {
                EntityFXFacingParticle p = EffectHelper.genericFlareParticle(
                        at.getX() + (itemRand.nextBoolean() ? -(itemRand.nextFloat() * 0.1) : 1 + (itemRand.nextFloat() * 0.1)),
                        at.getY() + (itemRand.nextBoolean() ? -(itemRand.nextFloat() * 0.1) : 1 + (itemRand.nextFloat() * 0.1)),
                        at.getZ() + (itemRand.nextBoolean() ? -(itemRand.nextFloat() * 0.1) : 1 + (itemRand.nextFloat() * 0.1)));
                p.motion((itemRand.nextFloat() * 0.03F) * (itemRand.nextBoolean() ? 1 : -1),
                        (itemRand.nextFloat() * 0.03F) * (itemRand.nextBoolean() ? 1 : -1),
                        (itemRand.nextFloat() * 0.03F) * (itemRand.nextBoolean() ? 1 : -1));
                p.scale(0.35F).setColor(Color.WHITE.brighter());
            }
        }, 1);
    }

    private Deque<BlockPos> filterBlocksToPlace(Entity entity, World world, double range) {
        Deque<BlockPos> placeable = getBlocksToPlaceAt(entity, range);
        boolean discard = false;
        Iterator<BlockPos> iterator = placeable.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if(discard) {
                iterator.remove();
                continue;
            }
            if(!world.isAirBlock(pos.getX(), pos.getY(), pos.getZ()) && !world.getBlock(pos.getX(), pos.getY(), pos.getZ()).isReplaceable(world, pos.getX(), pos.getY(), pos.getZ())) {
                discard = true;
                iterator.remove();
            }
        }
        return placeable;
    }

    private Deque<BlockPos> getBlocksToPlaceAt(Entity entity, double range) {
        MovingObjectPosition rtr = getLookBlock(entity, false, true, range);
        if(rtr == null) {
            return Lists.newLinkedList();
        }
        LinkedList<BlockPos> blocks = Lists.newLinkedList();
        int sideHit = rtr.sideHit;
        BlockPos hitPos = new BlockPos(rtr.blockX, rtr.blockY, rtr.blockZ);
        int length;
        int cmpFrom;
        double cmpTo;
//        switch (sideHit) {
//            case X:
//                cmpFrom = hitPos.getX();
//                cmpTo = entity.posX;
//                break;
//            case Y:
//                cmpFrom = hitPos.getY();
//                cmpTo = entity.posY;
//                break;
//            case Z:
//                cmpFrom = hitPos.getZ();
//                cmpTo = entity.posZ;
//                break;
//            default:
//                return Lists.newLinkedList();
//        }
//        length = (int) Math.min(20, Math.abs(cmpFrom + 0.5 - cmpTo));
//        for (int i = 1; i < length; i++) {
//            blocks.add(hitPos.offset(ForgeDirection.getOrientation(sideHit), i));
//        }
        return blocks;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register)
    {
        this.itemIcon = register.registerIcon("astralsorcery:architectWand");
    }
}
