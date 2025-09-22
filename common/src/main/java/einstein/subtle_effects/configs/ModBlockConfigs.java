package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.compat.EndRemasteredCompat;
import einstein.subtle_effects.configs.blocks.FallingBlocksConfigs;
import einstein.subtle_effects.configs.blocks.SparksConfigs;
import einstein.subtle_effects.configs.blocks.SteamConfigs;
import einstein.subtle_effects.configs.blocks.UpdatedSmokeConfigs;
import einstein.subtle_effects.init.ModBlockTickers;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.EnderEyePlacedRingParticle;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.Util;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.util.AllowableIdentifiers;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static einstein.subtle_effects.init.ModConfigs.BASE_KEY;
import static einstein.subtle_effects.util.Util.VANILLA_EYE;

@Translation(prefix = ModConfigs.BASE_KEY + "blocks")
public class ModBlockConfigs extends Config {

    private static final Map<ResourceLocation, ValidatedColor.ColorHolder> DEFAULT_EYE_COLORS = net.minecraft.Util.make(new HashMap<>(), map -> {
        map.put(VANILLA_EYE, Util.toColorHolder(EnderEyePlacedRingParticle.DEFAULT_COLOR));

        if (CompatHelper.IS_END_REMASTERED_LOADED.get()) {
            map.putAll(CompatHelper.getDefaultEyes());
        }
    });

    public SparksConfigs sparks = new SparksConfigs();
    public UpdatedSmokeConfigs updatedSmoke = new UpdatedSmokeConfigs();
    public SteamConfigs steam = new SteamConfigs();
    public FallingBlocksConfigs fallingBlocks = new FallingBlocksConfigs();

    public ConfigGroup dustyBlocksGroup = new ConfigGroup("dusty_blocks");
    public boolean redstoneBlockDust = true;
    public BlockDustDensity redstoneBlockDustDensity = BlockDustDensity.DEFAULT;
    public ValidatedList<Block> redstoneDustEmittingBlocks = new ValidatedList<>(List.of(Blocks.REDSTONE_BLOCK), ValidatedRegistryType.of(BuiltInRegistries.BLOCK));
    public boolean glowstoneBlockDust = true;
    public BlockDustDensity glowstoneBlockDustDensity = BlockDustDensity.DEFAULT;
    public ValidatedList<Block> glowstoneDustEmittingBlocks = new ValidatedList<>(List.of(Blocks.GLOWSTONE), ValidatedRegistryType.of(BuiltInRegistries.BLOCK));
    @ConfigGroup.Pop
    public boolean netherOnlyGlowstoneBlockDust = false;
    public boolean beehivesHaveSleepingZs = true;
    public SmokeType torchflowerSmoke = SmokeType.DEFAULT;
    public boolean torchflowerFlames = true;
    public boolean dragonEggParticles = true;
    public boolean replaceEndPortalSmoke = true;
    public boolean pumpkinCarvedParticles = true;

    public ConfigGroup workstationsGroup = new ConfigGroup("work_stations");
    public boolean anvilBreakParticles = true;
    public boolean anvilUseParticles = true;
    public boolean grindstoneUseParticles = true;
    public boolean smithingTableUseParticles = true;
    public boolean stonecutterUseParticles = true;
    public boolean cauldronUseParticles = true;
    public boolean cauldronCleanItemSounds = true;
    public boolean compostingCompostParticles = true;
    @ConfigGroup.Pop
    public boolean compostingItemParticles = true;

    public CommandBlockSpawnType commandBlockParticles = CommandBlockSpawnType.ON;
    public ValidatedDouble commandBlockParticlesDensity = new ValidatedDouble(1, 1, 0.1);
    public boolean slimeBlockBounceSounds = true;
    public ConfigGroup beaconParticlesGroup = new ConfigGroup("beacon_particles");
    public BeaconParticlesDisplayType beaconParticlesDisplayType = BeaconParticlesDisplayType.ON;
    public ValidatedInt beaconParticlesDensity = new ValidatedInt(10, 20, 1);
    @ConfigGroup.Pop
    public ValidatedFloat beaconParticlesSpeed = new ValidatedFloat(1, 2, 0.5F);
    public boolean respawnAnchorParticles = true;
    public boolean beehiveShearParticles = true;
    public boolean endPortalParticles = true;
    public boolean leavesDecayEffects = true;
    public boolean farmlandDestroyEffects = true;

    public ConfigGroup amethystGroup = new ConfigGroup("amethyst");
    public AmethystSparkleDisplayType amethystSparkleDisplayType = AmethystSparkleDisplayType.ON;
    public ValidatedList<Block> amethystSparkleEmittingBlocks = new ValidatedList<>(List.of(Blocks.AMETHYST_BLOCK, Blocks.BUDDING_AMETHYST), ValidatedRegistryType.of(BuiltInRegistries.BLOCK));
    @ConfigGroup.Pop
    public boolean amethystSparkleSounds = true;

    public boolean floweringAzaleaPetals = true;
    public ConfigGroup sculkGroup = new ConfigGroup("sculk");
    public boolean sculkBlockSculkDust = true;
    public boolean sculkVeinSculkDust = true;
    public boolean sculkShriekerDestroySouls = true;
    public boolean sculkCatalystDestroySouls = true;
    @ConfigGroup.Pop
    public boolean calibratedSculkSensorAmethystSparkle = true;
    public ValidatedFloat campfireSizzlingSoundVolume = new ValidatedFloat(0.5F, 1, 0);
    public boolean replacePowderSnowFlakes = true;
    public boolean lavaCauldronEffects = true;

    public ConfigGroup endPortalFrameGroup = new ConfigGroup("end_portal_frame");
    public boolean enderEyePlacedRings = true;
    public ValidatedInt enderEyePlacedRingsDuration = new ValidatedInt(10, 60, 5);
    public EnderEyePlacedParticlesDisplayType enderEyePlacedParticlesDisplayType = EnderEyePlacedParticlesDisplayType.BOTH;
    public ValidatedMap<ResourceLocation, ValidatedColor.ColorHolder> eyeColors = new ValidatedMap<>(DEFAULT_EYE_COLORS,
            getEyeHandler(), new ValidatedColor(new Color(EnderEyePlacedRingParticle.DEFAULT_COLOR), false));
    public EndPortalFrameParticlesDisplayType endPortalFrameParticlesDisplayType = EndPortalFrameParticlesDisplayType.SMOKE;
    @ConfigGroup.Pop
    public ValidatedFloat endPortalFrameParticlesDensity = new ValidatedFloat(1, 1, 0);

    public boolean replaceOminousVaultConnection = true;
    public boolean cobwebMovementSounds = true;
    public boolean cakeEatParticles = true;
    public boolean cakeEatSounds = true;
    public boolean rainWaterRipples = true;
    public ValidatedFloat rainWaterRipplesDensity = new ValidatedFloat(0.35F, 1, 0);
    public boolean underwaterBlockBreakBubbles = true;
    public ConfigGroup underwaterContainerBubblingGroup = new ConfigGroup("underwater_container_bubbling");
    public boolean decoratedPotsSpawnBubbles = true;
    public boolean openingBarrelsSpawnsBubbles = true;
    public boolean openingChestsSpawnsBubbles = true;
    public boolean chestsOpenRandomlyUnderwater = true;
    @ConfigGroup.Pop
    public boolean randomChestOpeningNeedsSoulSand = false;

    private static ValidatedIdentifier getEyeHandler() {
        List<ResourceLocation> eyes = CompatHelper.IS_END_REMASTERED_LOADED.get()
                ? EndRemasteredCompat.getAllEyes()
                : CompatHelper.getDefaultEyes().keySet().stream().toList();

        return new ValidatedIdentifier(VANILLA_EYE, new AllowableIdentifiers(id -> {
            if (eyes.contains(id)) {
                return true;
            }
            return id.equals(VANILLA_EYE);
        }, () -> eyes, true));
    }

    public ModBlockConfigs() {
        super(SubtleEffects.loc("blocks"));
    }

    @Override
    public void onUpdateClient() {
        TickerManager.clear(Minecraft.getInstance().level);
        ModBlockTickers.init();
    }

    public enum BlockDustDensity implements EnumTranslatable {
        DEFAULT(0),
        MINIMAL(2);

        private final int perSideChance;

        BlockDustDensity(int perSideChance) {
            this.perSideChance = perSideChance;
        }

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "blocks.blockDustDensity";
        }

        public int getPerSideChance() {
            return perSideChance;
        }
    }

    public enum AmethystSparkleDisplayType implements EnumTranslatable {
        OFF,
        ON,
        CRYSTALS_ONLY;

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "blocks.amethystSparkleDisplayType";
        }
    }

    public enum BeaconParticlesDisplayType implements EnumTranslatable {
        OFF,
        ON,
        NOT_COLORED;

        @Override
        public @NotNull String prefix() {
            return BASE_KEY + "blocks.beaconParticlesDisplayType";
        }
    }

    public enum EnderEyePlacedParticlesDisplayType implements EnumTranslatable {
        DOTS,
        VANILLA,
        BOTH;

        @Override
        public @NotNull String prefix() {
            return BASE_KEY + "blocks.enderEyePlacedParticlesDisplayType";
        }
    }

    public enum EndPortalFrameParticlesDisplayType implements EnumTranslatable {
        OFF(0, null),
        DOTS(8, (level, pos) -> ColorParticleOption.create(ModParticles.SHORT_SPARK.get(), Util.getEyeColorHolder(level, pos).toInt())),
        SMOKE(1, (level, pos) -> ParticleTypes.SMOKE);

        public final int count;
        public final BiFunction<Level, BlockPos, ParticleOptions> particle;

        EndPortalFrameParticlesDisplayType(int count, BiFunction<Level, BlockPos, ParticleOptions> particle) {
            this.count = count;
            this.particle = particle;
        }

        @Override
        public @NotNull String prefix() {
            return BASE_KEY + "blocks.endPortalFrameParticlesDisplayType";
        }
    }
}
