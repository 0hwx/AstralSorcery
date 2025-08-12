package hellfirepvp.astralsorcery.client.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.data.config.Config;

public class GuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class ConfigGui extends GuiConfig {

        private static IConfigElement ceGeneral = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.general));
        private static IConfigElement ceEntities = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.entities));
        private static IConfigElement ceRecipes = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.recipes));
        private static IConfigElement ceTools = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.tools));
        private static IConfigElement ceCrafting = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.crafting));
        private static IConfigElement ceLightnetwork = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.lightnetwork));
        private static IConfigElement ceRendering = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.rendering));
        private static IConfigElement ceWorldgen = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.worldgen));
        private static IConfigElement ceRetrogen = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.retrogen));
        private static IConfigElement ceDebug = new ConfigElement(
            Config.getRawConfig()
                .getCategory(Config.Categories.debug));

        public ConfigGui(GuiScreen parentScreen) {
            super(
                parentScreen,
                ImmutableList.of(
                    ceGeneral,
                    ceEntities,
                    ceRecipes,
                    ceTools,
                    ceCrafting,
                    ceLightnetwork,
                    ceRendering,
                    ceWorldgen,
                    ceRetrogen,
                    ceDebug),
                AstralSorcery.MODID,
                AstralSorcery.MODID,
                false,
                false,
                I18n.format("astralsorcery.configgui.title"));
        }

        @Override
        public void initGui() {
            // You can add buttons and initialize fields here
            super.initGui();
            AstralSorcery.debug("Initializing config gui");
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            // You can do things like create animations, draw additional elements, etc. here
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void actionPerformed(GuiButton b) {
            AstralSorcery.debug("Config button id " + b.id + " pressed");
            super.actionPerformed(b);
            /* "Done" button */
            if (b.id == 2000) {
                /* Syncing config */
                AstralSorcery.debug("Saving config");
                Config.getRawConfig()
                    .save();
                Config.load(AstralSorcery.confFile);
            }
        }
    }
}
