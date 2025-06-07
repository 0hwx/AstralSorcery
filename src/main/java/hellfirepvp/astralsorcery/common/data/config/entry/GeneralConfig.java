package hellfirepvp.astralsorcery.common.data.config.entry;


import com.google.common.base.Predicates;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.List;


public class GeneralConfig extends ConfigEntry {

    public static final GeneralConfig CONFIG = new GeneralConfig();

    public static int dayLength;

    public boolean giveJournalOnJoin;
    public boolean mobSpawningDenyAllTypes;
    public List<? extends String> modidOreBlacklist;

    public boolean doColoredLensesAffectPlayers;

    private GeneralConfig() {
        super(Section.GENERAL,"general");
    }

    @Override
    public void loadFromConfig(Configuration cfg) {
        dayLength = cfg.getInt("dayLength",getConfigurationSection(),24000, 1000, 400_000,
                "Defines the length of a day (both daytime & nighttime obviously) for the mod's internal logic. NOTE: This does NOT CHANGE HOW LONG A DAY IN MC IS! It is only to provide potential compatibility for mods that do provide such functionality.");

        giveJournalOnJoin = cfg.getBoolean("giveJournalOnJoin" ,getConfigurationSection(),true,
                "If set to 'true', the player will receive an AstralSorcery Journal when they join the server for the first time.");

//        mobSpawningDenyAllTypes = cfgBuilder
//            .comment("If set to 'true' anything that prevents mobspawning !by this mod!, will also prevent EVERY natural mobspawning of any mobtype. When set to 'false' it'll only stop monsters of type 'MONSTER' from spawning.")
//            .translation(translationKey("mobSpawningDenyAllTypes"))
//            .define("mobSpawningDenyAllTypes", false);
//
//        modidOreBlacklist = cfgBuilder
//            .comment("Features generating random ores in AstralSorcery will not spawn ores from mods listed here.")
//            .translation(translationKey("modidOreBlacklist"))
//            .defineList("modidOreBlacklist", Arrays.asList("techreborn", "gregtech"), Predicates.alwaysTrue());
//
//        doColoredLensesAffectPlayers = cfgBuilder
//            .comment("Set this to false to prevent players from being affected by entity-related colored lens effects.")
//            .translation(translationKey("doColoredLensesAffectPlayers"))
//            .define("doColoredLensesAffectPlayers", true);
    }
}
