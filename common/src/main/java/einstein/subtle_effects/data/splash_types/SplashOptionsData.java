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
                                Optional<Either<Float, Boolean>> tinting,
                                Optional<Either<Float, Boolean>> transparency) {

    public record SplashOptions(SpriteSet sprites, ColorProviderType.ColorProvider colorProvider,
                                Optional<Either<Float, Boolean>> tinting, Optional<Either<Float, Boolean>> transparency) {

    }

    public static final SplashOptionsData EMPTY = new SplashOptionsData(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    public static final SplashOptionsData DEFAULT = new SplashOptionsData(Optional.empty(), Optional.of(NoneColorProvider.INSTANCE), Optional.empty(), Optional.empty());

    public static Codec<Either<Float, Boolean>> configurableFloatCodec(String floatName) {
        return Codec.either(
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.floatRange(0, 1).fieldOf(floatName).forGetter(Float::floatValue)
                ).apply(instance, Float::floatValue)),
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.BOOL.fieldOf("use_config").forGetter(Boolean::booleanValue)
                ).apply(instance, Boolean::booleanValue))
        );
    }

    public static final Codec<SplashOptionsData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("sprite_set").forGetter(SplashOptionsData::spriteSetId),
            ColorProviderType.CODEC.optionalFieldOf("color").forGetter(SplashOptionsData::colorProvider),
            configurableFloatCodec("intensity").optionalFieldOf("tinting").forGetter(SplashOptionsData::tinting),
            configurableFloatCodec("alpha").optionalFieldOf("transparency").forGetter(SplashOptionsData::transparency)
    ).apply(instance, (location, colorProvider, tinting, alpha) -> {
        if (location.isEmpty() && colorProvider.isEmpty() && alpha.isEmpty()) {
            return EMPTY;
        }
        return new SplashOptionsData(location, colorProvider, tinting, alpha);
    }));

    public static final Codec<SplashOptionsData> DROPLETS_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ColorProviderType.CODEC.optionalFieldOf("color").forGetter(SplashOptionsData::colorProvider),
            configurableFloatCodec("intensity").optionalFieldOf("tinting").forGetter(SplashOptionsData::tinting)
    ).apply(instance, (colorProvider, tinting) -> new SplashOptionsData(Optional.empty(), colorProvider, tinting, Optional.empty())));
}
