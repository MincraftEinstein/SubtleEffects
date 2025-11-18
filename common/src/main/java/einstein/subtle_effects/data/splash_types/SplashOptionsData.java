package einstein.subtle_effects.data.splash_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.data.color_providers.NoneColorProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record SplashOptionsData(Optional<ResourceLocation> spriteSetId,
                                Optional<ColorProviderType.ColorProvider> colorProvider) {

    public record SplashOptions(SpriteSet sprites, ColorProviderType.ColorProvider color) {

    }

    public static final SplashOptionsData EMPTY = new SplashOptionsData(Optional.empty(), Optional.empty());
    public static final SplashOptionsData DEFAULT = new SplashOptionsData(Optional.empty(), Optional.of(NoneColorProvider.INSTANCE));

    public static final Codec<SplashOptionsData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("sprite_set").forGetter(SplashOptionsData::spriteSetId),
            ColorProviderType.CODEC.optionalFieldOf("color").forGetter(SplashOptionsData::colorProvider)
    ).apply(instance, (location, provider) -> {
        if (location.isEmpty() && provider.isEmpty()) {
            return EMPTY;
        }
        return new SplashOptionsData(location, provider);
    }));
}
