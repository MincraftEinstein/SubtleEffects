package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.conditional;

@Translation(prefix = ModConfigs.BASE_KEY + "general")
public class ModGeneralConfigs extends Config {

    private static final List<ParticleType<?>> DEFAULT_CULLING_BLOCKLIST = net.minecraft.Util.make(new ArrayList<>(), list -> {
        if (CompatHelper.IS_PARTICLE_RAIN_LOADED.get()) {
            ParticleType<?> mistType = BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.fromNamespaceAndPath(CompatHelper.PARTICLE_RAIN_MOD_ID, "mist"));

            if (mistType != null) {
                list.add(mistType);
            }
        }
    });

    public ConfigGroup particleRenderingGroup = new ConfigGroup("particle_rendering");
    public ValidatedBoolean enableParticleCulling = new ValidatedBoolean();
    public ValidatedCondition<Integer> particleRenderDistance = conditional(new ValidatedInt(5, 32, 1), enableParticleCulling);
    public ValidatedCondition<List<? extends ParticleType<?>>> particleCullingBlocklist =
            conditional(ValidatedRegistryType.of(ParticleTypes.FLAME, BuiltInRegistries.PARTICLE_TYPE).toList(DEFAULT_CULLING_BLOCKLIST), enableParticleCulling);
    public ValidatedCondition<Boolean> cullParticlesInUnloadedChunks = conditional(new ValidatedBoolean(), enableParticleCulling);
    public ValidatedBoolean allowUsingBlendedRenderType = new ValidatedBoolean();
    @ConfigGroup.Pop
    public ValidatedCondition<Boolean> spellParticlesUseBlendedRenderType = conditional(new ValidatedBoolean(), allowUsingBlendedRenderType);

    public ConfigGroup particlesGroup = new ConfigGroup("particles");

    public ConfigGroup dripParticlesGroup = new ConfigGroup("particles.drip_particles");
    public boolean glowingLavaDrops = true;
    public ValidatedBoolean fluidDropsEvaporate = new ValidatedBoolean();
    public ValidatedCondition<Float> fluidDropsEvaporationVolume = conditional(new ValidatedFloat(0.25F, 1, 0), fluidDropsEvaporate);
    public boolean dropLandInFluidRipples = true;
    @ConfigGroup.Pop
    public ValidatedFloat dropLandSoundVolume = new ValidatedFloat(1, 2, 0);

    public boolean lavaSparkSmoke = true;
    public ValidatedFloat sparksScale = new ValidatedFloat(1, 2, 1);
    public boolean poppingHearts = true;
    public boolean poppingBubbles = true;
    public ValidatedFloat poppingBubblesVolume = new ValidatedFloat(0.2F, 1, 0);
    public ConfigGroup enchantmentParticlesGroup = new ConfigGroup("enchant_particles");
    public boolean glowingEnchantmentParticles = true;
    public boolean translucentEnchantmentParticles = true;
    @ConfigGroup.Pop
    public boolean disableRandomizedShading = true;
    public ValidatedFloat potionParticleAlpha = new ValidatedFloat(0.7F, 1, 0.3F);
    public ValidatedFloat potionParticleAlphaNearPlayer = new ValidatedFloat(0.2F, 1, 0);
    public ConfigGroup leavesGroup = new ConfigGroup("leaves");
    public boolean leavesLandOnGround = true;
    public ValidatedBoolean leavesLandOnWater = new ValidatedBoolean();
    public ValidatedCondition<Boolean> leavesLandingOnWaterRipples = conditional(new ValidatedBoolean(), leavesLandOnWater);
    public ValidatedCondition<Boolean> leavesLandingOnWaterKeepMomentum = conditional(new ValidatedBoolean(), leavesLandOnWater);
    public ValidatedInt fallenLeavesLifeTime = new ValidatedInt(40, 100, 20);
    @ConfigGroup.Pop
    public boolean rainIncreasesLeavesMovementSpeed = true;
    @ConfigGroup.Pop
    public boolean staticFlameBrightness = true;

    public ConfigGroup fireOverlayGroup = new ConfigGroup("fireOverlay");
    public ValidatedFloat fireOverlayHeight = new ValidatedFloat(-0.15F, 0.4F, -0.5F);
    public ValidatedFloat fireOverlayAlpha = new ValidatedFloat(0.9F, 1, 0);
    @ConfigGroup.Pop
    public ValidatedFloat fireOverlayAlphaWithFireResistance = new ValidatedFloat(0.4F, 1, 0);
    public boolean mobSkullShaders = true;
    public ValidatedBoolean nightVisionFading = new ValidatedBoolean();
    public ValidatedCondition<Integer> nightVisionFadingTime =
            conditional(new ValidatedInt(100, 200, 10), nightVisionFading);
    public boolean enableEasterEggs = true;

    public ModGeneralConfigs() {
        super(SubtleEffects.loc("general"));
    }

    @Override
    public void onUpdateClient() {
        Minecraft minecraft = Minecraft.getInstance();
        SubtleEffectsClient.clear(minecraft.level);

        if (minecraft.level != null && minecraft.options.getCameraType().isFirstPerson()) {
            if (!mobSkullShaders) {
                minecraft.gameRenderer.shutdownEffect();
                return;
            }

            Player player = minecraft.player;
            if (player != null) {
                Util.applyHelmetShader(player.getInventory().getArmor(3), minecraft.options.getCameraType());
            }
        }
    }
}
