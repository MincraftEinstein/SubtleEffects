package einstein.subtle_effects.data.splash_types;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.data.color_providers.NoneColorProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record SplashOptionsData(Optional<ResourceLocation> spriteSetId,
                                Optional<ColorProviderType.ColorProvider> colorProvider,
                                Optional<Either<Float, Boolean>> tinting) {

    public record SplashOptions(SpriteSet sprites, ColorProviderType.ColorProvider colorProvider,
                                Optional<Either<Float, Boolean>> tinting) {

    }

    public static final SplashOptionsData EMPTY = new SplashOptionsData(Optional.empty(), Optional.empty(), Optional.empty());
    public static final SplashOptionsData DEFAULT = new SplashOptionsData(Optional.empty(), Optional.of(NoneColorProvider.INSTANCE), Optional.empty());

    private static final Codec<Float> TINT_INTENSITY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.floatRange(0, 1).fieldOf("intensity").forGetter(Float::floatValue)
    ).apply(instance, Float::floatValue));

    private static final Codec<Boolean> CONFIG_TINT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("use_config").forGetter(Boolean::booleanValue)
    ).apply(instance, Boolean::booleanValue));

    public static final Codec<Either<Float, Boolean>> TINT_OPTIONS_CODEC = Codec.either(TINT_INTENSITY_CODEC, CONFIG_TINT_CODEC);

    public static final Codec<SplashOptionsData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("sprite_set").forGetter(SplashOptionsData::spriteSetId),
            ColorProviderType.CODEC.optionalFieldOf("color").forGetter(SplashOptionsData::colorProvider),
            TINT_OPTIONS_CODEC.optionalFieldOf("tinting").forGetter(SplashOptionsData::tinting)
    ).apply(instance, (location, colorProvider, tinting) -> {
        if (location.isEmpty() && colorProvider.isEmpty()) {
            return EMPTY;
        }
        return new SplashOptionsData(location, colorProvider, tinting);
    }));

    public static final Codec<SplashOptionsData> CODEC_NO_SPRITES = RecordCodecBuilder.create(instance -> instance.group(
            ColorProviderType.CODEC.optionalFieldOf("color").forGetter(SplashOptionsData::colorProvider),
            TINT_OPTIONS_CODEC.optionalFieldOf("tinting").forGetter(SplashOptionsData::tinting)
    ).apply(instance, (colorProvider, tinting) -> new SplashOptionsData(Optional.empty(), colorProvider, tinting)));
}
