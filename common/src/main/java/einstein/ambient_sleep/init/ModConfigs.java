package einstein.ambient_sleep.init;

import einstein.ambient_sleep.AmbientSleep;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigs {

    private static final Pair<ModConfigs, ForgeConfigSpec> SPEC_PAIR = new ForgeConfigSpec.Builder().configure(ModConfigs::new);
    public static final ModConfigs INSTANCE = SPEC_PAIR.getLeft();
    public static final ForgeConfigSpec SPEC = SPEC_PAIR.getRight();

    public final ForgeConfigSpec.BooleanValue disableDefaultCampfireSparks;
    public final ForgeConfigSpec.BooleanValue enableCandleSparks;
    public final ForgeConfigSpec.BooleanValue enableFurnaceSparks;
    public final ForgeConfigSpec.BooleanValue enableFireSparks;
    public final ForgeConfigSpec.BooleanValue enableCampfireSparks;
    public final ForgeConfigSpec.BooleanValue enableTorchSparks;

    public final ForgeConfigSpec.BooleanValue redstoneBlockDust;
    public final ForgeConfigSpec.BooleanValue glowstoneBlockDust;

    public final ForgeConfigSpec.DoubleValue playerSnoreChance;
    public final ForgeConfigSpec.DoubleValue villagerSnoreChance;
    public final ForgeConfigSpec.BooleanValue enableSleepingZs;
    public final ForgeConfigSpec.BooleanValue displaySleepingZsOnlyWhenSnoring;
    public final ForgeConfigSpec.BooleanValue foxesHaveSleepingZs;

    public ModConfigs(ForgeConfigSpec.Builder builder) {
        builder.translation(categoryKey("blocks")).push("blocks")
                .translation(categoryKey("flaming_blocks")).push("flamingBlocks");

        disableDefaultCampfireSparks = builder
                .comment("Disables the default lava spark particle from campfires")
                .translation(key("disable_default_campfire_sparks"))
                .define("disableDefaultCampfireSparks", true);

        enableCandleSparks = builder.translation(key("enable_candle_sparks"))
                .define("enableCandleSparks", true);
        enableFurnaceSparks = builder.translation(key("enable_furnace_sparks"))
                .define("enableFurnaceSparks", true);
        enableFireSparks = builder.translation(key("enable_fire_sparks"))
                .define("enableFireSparks", true);
        enableCampfireSparks = builder.translation(key("enable_campfire_sparks"))
                .define("enableCampfireSparks", true);
        enableTorchSparks = builder.translation(key("enable_torch_sparks"))
                .define("enableTorchSparks", true);

        builder.pop();

        redstoneBlockDust = builder
                .translation(key("redstone_block_dust"))
                .define("redstoneBlockDust", true);

        glowstoneBlockDust = builder
                .translation(key("glowstone_block_dust"))
                .define("glowstoneBlockDust", true);

        builder.pop().translation(categoryKey("entities")).push("entities")
                .translation(categoryKey("sleeping")).push("sleeping");

        playerSnoreChance = builder
                .comment("A percentage based change for a player to snore.", "0 to disable")
                .translation(key("player_snore_chance"))
                .defineInRange("playerSnoreChance", 1.0, 0, 1.0);

        villagerSnoreChance = builder
                .comment("A percentage based change for a villager to snore.", "0 to disable")
                .translation(key("villager_snore_chance"))
                .defineInRange("villagerSnoreChance", 1.0, 0, 1.0);

        enableSleepingZs = builder
                .comment("When an mob is sleeping display Z particles")
                .translation("enable_sleeping_zs")
                .define("enableSleepingZs", true);

        displaySleepingZsOnlyWhenSnoring = builder
                .comment("Only display Z particles when a mob can snore")
                .translation("display_sleeping_zs_only_when_snoring")
                .define("displaySleepingZsOnlyWhenSnoring", false);

        foxesHaveSleepingZs = builder
                .comment("Display Z particles for sleeping foxes")
                .translation("foxes_have_sleeping_zs")
                .define("foxesHaveSleepingZs", true);

        builder.pop();
    }

    private static String key(String path) {
        return "config." + AmbientSleep.MOD_ID + "." + path;
    }

    private static String categoryKey(String path) {
        return "config.category." + AmbientSleep.MOD_ID + "." + path;
    }
}
