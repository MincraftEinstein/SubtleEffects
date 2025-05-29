package einstein.subtle_effects.configs.environment;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.AllowableIdentifiers;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Translation(prefix = ModConfigs.BASE_KEY + "environment.biomes")
public class BiomeConfigs extends ConfigSection {

    public ValidatedInt biomeParticlesRadius = new ValidatedInt(32, 32, 0);
    public ValidatedList<ResourceLocation> mushroomSporeBiomes = biomeList("mushroom_fields");
    public ValidatedInt mushroomSporeDensity = new ValidatedInt(10, 100, 0);
    public ValidatedList<ResourceLocation> fireflyBiomes = biomeList("swamp", "mangrove_swamp");
    public ValidatedInt fireflyDensity = new ValidatedInt(6, 100, 0);
    public ValidatedList<ResourceLocation> pollenBiomes = biomeList("flower_forest", "sunflower_plains");
    public ValidatedInt pollenDensity = new ValidatedInt(50, 100, 0);
    public ValidatedList<ResourceLocation> sculkDustBiomes = biomeList("deep_dark");
    public ValidatedInt sculkDustDensity = new ValidatedInt(5, 100, 0);

    public static ValidatedList<ResourceLocation> biomeList(String... biomeIds) {
        return new ValidatedList<>(Arrays.stream(biomeIds).map(ResourceLocation::withDefaultNamespace).toList(),
                new ValidatedIdentifier(ResourceLocation.withDefaultNamespace("air"),
                        new AllowableIdentifiers(
                                location -> getBiomeRegistry().map(biomes -> biomes.containsKey(location)).orElse(true),
                                () -> getBiomeRegistry().map(biomes -> biomes.keySet().stream().toList()).orElseGet(List::of)
                        )
                )
        );
    }

    private static Optional<Registry<Biome>> getBiomeRegistry() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            return level.registryAccess().lookup(Registries.BIOME);
        }
        return Optional.empty();
    }
}
