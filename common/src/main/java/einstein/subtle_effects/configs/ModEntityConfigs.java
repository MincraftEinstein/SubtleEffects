package einstein.subtle_effects.configs;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.SubtleEffectsClient;
import einstein.subtle_effects.configs.entities.*;
import einstein.subtle_effects.init.ModAnimalFedEffectSettings;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedCondition;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Translation(prefix = ModConfigs.BASE_KEY + "entities")
public class ModEntityConfigs extends Config {

    private static final List<ResourceLocation> LIST = Util.make(new ArrayList<>(), list -> {
        add(list, DamageTypes.GENERIC);
        add(list, DamageTypes.STALAGMITE);
        add(list, DamageTypes.TRIDENT);
        add(list, DamageTypes.FALL);
        add(list, DamageTypes.FALLING_STALACTITE);
        add(list, DamageTypes.MOB_PROJECTILE);
        add(list, DamageTypes.WIND_CHARGE);
        add(list, DamageTypes.ARROW);
        add(list, DamageTypes.BAD_RESPAWN_POINT);
        add(list, DamageTypes.EXPLOSION);
        add(list, DamageTypes.FALLING_ANVIL);
        add(list, DamageTypes.FALLING_BLOCK);
        add(list, DamageTypes.FIREBALL);
        add(list, DamageTypes.FLY_INTO_WALL);
        add(list, DamageTypes.LIGHTNING_BOLT);
        add(list, DamageTypes.MOB_ATTACK);
        add(list, DamageTypes.MOB_ATTACK_NO_AGGRO);
        add(list, DamageTypes.PLAYER_ATTACK);
        add(list, DamageTypes.PLAYER_EXPLOSION);
        add(list, DamageTypes.SONIC_BOOM);
        add(list, DamageTypes.STING);
        add(list, DamageTypes.THROWN);
        add(list, DamageTypes.UNATTRIBUTED_FIREBALL);
        add(list, DamageTypes.WITHER_SKULL);
    });

    public SleepingConfigs sleeping = new SleepingConfigs();
    public HumanoidConfigs humanoids = new HumanoidConfigs();
    public DustCloudsConfig dustClouds = new DustCloudsConfig();
    public BurningEntityConfigs burning = new BurningEntityConfigs();
    public ExplosivesConfigs explosives = new ExplosivesConfigs();
    public SplashConfigs splashes = new SplashConfigs();

    public ConfigGroup attackedGroup = new ConfigGroup("attacked");
    public ValidatedList<ResourceLocation> damageTypes = ModConfigs.registryList(Registries.DAMAGE_TYPE, LIST);
    public boolean attackedChickenFeathers = true;
    public boolean attackedParrotFeathers = true;
    public boolean attackedSnowGolemSnowflakes = true;
    public boolean attackedSheepFluff = true;
    public boolean attackedSlimeSlime = true;
    @ConfigGroup.Pop
    public boolean attackedSkeletonBones = true;

    public ValidatedDouble allayMagicDensity = new ValidatedDouble(0.2, 1, 0);
    public ValidatedDouble vexMagicDensity = new ValidatedDouble(0.2, 1, 0);
    public boolean sheepShearFluff = true;
    public boolean improvedDragonFireballTrail = true;
    public boolean dragonsBreathClouds = true;
    public CommandBlockSpawnType commandBlockMinecartParticles = CommandBlockSpawnType.ON;
    public boolean endCrystalParticles = true;
    public ValidatedEnum<MinecartSparksDisplayType> minecartSparksDisplayType = new ValidatedEnum<>(MinecartSparksDisplayType.DEFAULT);
    public ValidatedCondition<Float> minecartSparksDensity = ModConfigs.conditional(new ValidatedFloat(0.5F, 1, 0), minecartSparksDisplayType, MinecartSparksDisplayType.OFF);
    public boolean slimeTrails = true;
    public boolean magmaCubeTrails = true;
    public boolean replaceSlimeSquishParticles = true;
    public boolean replaceOozingEffectParticles = true;
    public boolean replaceSpellCasterParticles = true;
    public boolean ironGolemCrackParticles = true;
    public boolean spectralArrowParticles = true;
    public boolean wardenDeathSoulParticles = true;
    public boolean freezingSnowFlakes = true;
    public boolean featherTicklingPandas = true;
    public boolean improvedPandaSneezes = true;
    public boolean villagerWorkAtWorkstationParticles = true;
    public boolean improvedBrownMooshroomFeedingEffects = true;
    public boolean improvedMooshroomShearingEffects = true;
    public boolean replaceBlazeSmoke = true;
    public boolean animalFeedingParticles = true;
    public ValidatedFloat animalFeedingSoundVolume = new ValidatedFloat(1, 1, 0);
    public boolean underwaterEntityPoofBubbles = true;
    public boolean improvedWetWolfShakeEffects = true;
    public boolean improvedVillagerSweatingEffects = true;
    public boolean magmaCubeLandSparks = true;
    public boolean snowGolemStepSounds = true;

    public ModEntityConfigs() {
        super(SubtleEffects.loc("entities"));
    }

    private static void add(List<ResourceLocation> list, ResourceKey<DamageType> key) {
        list.add(key.location());
    }

    @Override
    public void onUpdateClient() {
        SubtleEffectsClient.clear(Minecraft.getInstance().level);
        ModAnimalFedEffectSettings.init();
    }

    public enum MinecartSparksDisplayType implements EnumTranslatable {
        OFF,
        LAND_ON_RAIL,
        DEFAULT;

        @Override
        public @NotNull String prefix() {
            return ModConfigs.BASE_KEY + "entities.minecartSparksDisplayType";
        }
    }
}
