package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.particle.*;
import einstein.subtle_effects.particle.emitter.FireFlyEmitter;
import einstein.subtle_effects.particle.emitter.PotionEmitter;
import einstein.subtle_effects.particle.provider.*;
import einstein.subtle_effects.platform.Services;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;
import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModParticles.*;

public class ModParticleProviders {

    public static void init() {
        register(SNORING, SnoringParticle.Provider::new);
        register(FALLING_SNORING, SnoringParticle.FallingProvider::new);
        register(LONG_SPARK, SparkParticle.LongLifeProvider::new);
        register(SHORT_SPARK, SparkParticle.ShortLifeProvider::new);
        register(FLOATING_SPARK, SparkParticle.FloatingProvider::new);
        register(METAL_SPARK, SparkParticle.MetalProvider::new);
        register(SNOW, SnowParticle.Provider::new);
        register(SNOWBALL_TRAIL, SnowParticle.SnowballTrailProvider::new);
        register(FREEZING, SnowParticle.FreezingProvider::new);
        register(CHICKEN_FEATHER, FeatherParticle.Provider::new);
        register(BLUE_PARROT_FEATHER, FeatherParticle.Provider::new);
        register(GRAY_PARROT_FEATHER, FeatherParticle.Provider::new);
        register(GREEN_PARROT_FEATHER, FeatherParticle.Provider::new);
        register(RED_BLUE_PARROT_FEATHER, FeatherParticle.Provider::new);
        register(YELLOW_BLUE_PARROT_FEATHER, FeatherParticle.Provider::new);
        register(ALLAY_MAGIC, AllayMagicParticle.Provider::new);
        register(VEX_MAGIC, AllayMagicParticle.VexProvider::new);
        register(SMALL_DUST_CLOUD, DustCloudParticle.SmallProvider::new);
        register(LARGE_DUST_CLOUD, DustCloudParticle.LargeProvider::new);
        register(SHEEP_FLUFF, FeatherParticle.SheepFluffProvider::new);
        register(MUSHROOM_SPORE, MushroomSporeProvider::new);
        register(FIREFLY, FireflyParticle.Provider::new);
        register(FIREFLY_EMITTER, sprites -> new FireFlyEmitter.Provider());
        register(SMOKE, SmokeParticleProvider::new);
        register(POLLEN, PollenProvider::new);
        register(COMMAND_BLOCK, CommandBlockParticle.Provider::new);
        register(ITEM_RARITY, ItemRarityParticle.Provider::new);
        register(BEACON, BeaconParticle.Provider::new);
        register(COMPOST, CustomTerrainParticle.CompostProvider::new);
        register(STEAM, SteamParticle.Provider::new);
        register(END_PORTAL, EndPortalParticle.Provider::new);
        register(END_CRYSTAL, EndCrystalParticle.Provider::new);
        register(SCULK_DUST, SculkDustParticle.Provider::new);
        register(SLIME_TRAIL, SlimeTrailParticle.Provider::new);
        register(MAGMA_CUBE_TRAIL, SlimeTrailParticle.Provider::new);
        register(SPELL_CASTER_MAGIC, SpellCasterMagicProvider::new);
        register(AMETHYST_SPARKLE, SuspendedTownParticle.HappyVillagerProvider::new);
        register(AZALEA_PETAL, AzaleaParticleProvider::new);
        register(FROSTY_BREATH, SteamParticle.FrostyBreathProvider::new);
        register(EXPERIENCE, ExperienceParticle.Provider::new);
        register(HEART_POP, HeartPopParticle.Provider::new);
        register(POTION_RING, PotionRingParticle.Provider::new);
        register(POTION_DOT, PotionDotParticle.PotionDotProvider::new);
        register(POTION_EMITTER, sprites -> new PotionEmitter.Provider());
        register(IRON_GOLEM, CustomTerrainParticle.IronGolemProvider::new);
        register(DROWNING_BUBBLE, DrowningBubbleParticle.Provider::new);
        register(DROWNING_BUBBLE_POP, DrowningBubblePopParticle.Provider::new);
        register(EGG_SPLAT, EggSplatParticle.Provider::new);
        register(ENDER_EYE_PLACED_RING, EnderEyePlacedRingParticle.Provider::new);
        register(BLOCK_NO_MOMENTUM, sprites -> new TerrainNoMomentumParticleProvider());
        register(LAVA_SPLASH, LavaSplashParticle.Provider::new);
        register(GEYSER_HOLE, GeyserSpoutParticle.Provider::new);
        register(SNEEZE, SneezeParticle.Provider::new);
        SubtleEffects.LOGGER.info("Forge please do the world a favor and STOP EXISTING!!");
    }

    private static <T extends ParticleType<V>, V extends ParticleOptions> void register(Supplier<T> type, Function<SpriteSet, ParticleProvider<V>> provider) {
        Services.REGISTRY.registerParticleProvider(type, provider);
    }
}
