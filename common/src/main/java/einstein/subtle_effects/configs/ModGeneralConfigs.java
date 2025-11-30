package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

@Translation(prefix = ModConfigs.BASE_KEY + "general")
public class ModGeneralConfigs extends Config {

    private static final List<ParticleType<?>> DEFAULT_CULLING_BLOCKLIST = net.minecraft.util.Util.make(new ArrayList<>(), list -> {
        if (CompatHelper.IS_PARTICLE_RAIN_LOADED.get()) {
            BuiltInRegistries.PARTICLE_TYPE.get(Identifier.fromNamespaceAndPath(CompatHelper.PARTICLE_RAIN_MOD_ID, "mist"))
                    .ifPresent(value -> list.add(value.value()));
        }
    });

    public ConfigGroup particleRenderingGroup = new ConfigGroup("particle_rendering");
    public boolean enableParticleCulling = true;
    public ValidatedInt particleRenderDistance = new ValidatedInt(5, 32, 1);
    public ValidatedList<ParticleType<?>> particleCullingBlocklist = ValidatedRegistryType.of(ParticleTypes.FLAME, BuiltInRegistries.PARTICLE_TYPE).toList(DEFAULT_CULLING_BLOCKLIST);
    public boolean cullParticlesInUnloadedChunks = true;
    public boolean allowUsingBlendedRenderType = true;
    @ConfigGroup.Pop
    public boolean spellParticlesUseBlendedRenderType = true;

    public ConfigGroup particlesGroup = new ConfigGroup("particles");

    public ConfigGroup dripParticlesGroup = new ConfigGroup("particles.drip_particles");
    public boolean glowingLavaDrops = true;
    public boolean fluidDropsEvaporate = true;
    public ValidatedFloat fluidDropsEvaporationVolume = new ValidatedFloat(0.25F, 1, 0);
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
    public boolean leavesLandOnWater = true;
    public boolean leavesLandingOnWaterRipples = true;
    public boolean leavesLandingOnWaterKeepMomentum = true;
    public ValidatedInt fallenLeavesLifeTime = new ValidatedInt(40, 100, 20);
    @ConfigGroup.Pop
    @ConfigGroup.Pop
    public boolean rainIncreasesLeavesMovementSpeed = true;

    public ConfigGroup fireOverlayGroup = new ConfigGroup("fireOverlay");
    public ValidatedFloat fireOverlayHeight = new ValidatedFloat(-0.15F, 0.4F, -0.5F);
    public ValidatedFloat fireOverlayAlpha = new ValidatedFloat(0.9F, 1, 0);
    @ConfigGroup.Pop
    public ValidatedFloat fireOverlayAlphaWithFireResistance = new ValidatedFloat(0.4F, 1, 0);
    public boolean mobSkullShaders = true;
    public boolean nightVisionFading = true;
    public ValidatedInt nightVisionFadingTime = new ValidatedInt(100, 200, 10);
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
                minecraft.gameRenderer.clearPostEffect();
                return;
            }

            Player player = minecraft.player;
            if (player != null) {
                Util.applyHelmetShader(player.getItemBySlot(EquipmentSlot.HEAD), minecraft.options.getCameraType());
            }
        }
    }
}
