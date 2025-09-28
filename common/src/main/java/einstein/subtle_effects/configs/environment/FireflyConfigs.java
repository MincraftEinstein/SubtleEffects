package einstein.subtle_effects.configs.environment;

import einstein.subtle_effects.configs.ColdSeasonsType;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.biomeList;

@Translation(prefix = ModConfigs.BASE_KEY + "environment.fireflies")
public class FireflyConfigs extends ConfigSection {

    private static final List<Block> DEFAULT_SPAWNABLE_BLOCKS = Util.make(new ArrayList<>(), blocks -> {
        blocks.add(Blocks.SHORT_GRASS);
        blocks.add(Blocks.TALL_GRASS);
        blocks.add(Blocks.POPPY);
        blocks.add(Blocks.DANDELION);
        blocks.add(Blocks.PEONY);
        blocks.add(Blocks.BLUE_ORCHID);
        blocks.add(Blocks.ALLIUM);
        blocks.add(Blocks.AZURE_BLUET);
        blocks.add(Blocks.ORANGE_TULIP);
        blocks.add(Blocks.PINK_TULIP);
        blocks.add(Blocks.RED_TULIP);
        blocks.add(Blocks.WHITE_TULIP);
        blocks.add(Blocks.OXEYE_DAISY);
        blocks.add(Blocks.CORNFLOWER);
        blocks.add(Blocks.LILY_OF_THE_VALLEY);
        blocks.add(Blocks.SUNFLOWER);
        blocks.add(Blocks.LILAC);
        blocks.add(Blocks.ROSE_BUSH);
        blocks.add(Blocks.PINK_PETALS);
        blocks.add(Blocks.FLOWERING_AZALEA);
    });
    private static final List<ResourceLocation> DEFAULT_BLOCKED_DIMENSIONS = Util.make(new ArrayList<>(), dimensions -> {
        dimensions.add(ResourceLocation.fromNamespaceAndPath("twilightforest", "twilight_forest_type"));
    });

    public boolean firefliesEnabled = true;
    public ValidatedList<ResourceLocation> dimensionBlocklist = ModConfigs.registryList(Registries.DIMENSION_TYPE, DEFAULT_BLOCKED_DIMENSIONS);
    public ValidatedList<ResourceLocation> biomesBlocklist = biomeList();
    public ValidatedList<ResourceLocation> biomesAllowlist = biomeList("lush_caves");
    public ValidatedList<Block> spawnableBlocks = new ValidatedList<>(DEFAULT_SPAWNABLE_BLOCKS, ValidatedRegistryType.of(BuiltInRegistries.BLOCK));
    public ValidatedInt defaultDensity = new ValidatedInt(3, 10, 1);
    public ColdSeasonsType ignoredSeasons = ColdSeasonsType.DEFAULT;
    public FireflyType fireflyType = FireflyType.ORIGINAL;
    public ValidatedFloat fireflySoundVolume = new ValidatedFloat(1, 2, 0);

    public ConfigGroup habitatBiomesGroup = new ConfigGroup("habitat_biomes");
    public boolean onlyAllowInHabitatBiomes = false;
    public ValidatedList<ResourceLocation> habitatBiomes = biomeList("swamp", "mangrove_swamp");
    @ConfigGroup.Pop
    public ValidatedInt habitatBiomeDensity = new ValidatedInt(3, 10, 1);

    public enum FireflyType implements EnumTranslatable {
        ORIGINAL(ModParticles.FIREFLY),
        VANILLA(() -> ParticleTypes.FIREFLY);

        private final Supplier<SimpleParticleType> particle;

        FireflyType(Supplier<SimpleParticleType> particle) {
            this.particle = particle;
        }

        public Supplier<SimpleParticleType> getParticle() {
            return particle;
        }

        @Override
        public @NotNull String prefix() {
            return ModConfigs.BASE_KEY + "environment.fireflies.fireflyType";
        }
    }
}
