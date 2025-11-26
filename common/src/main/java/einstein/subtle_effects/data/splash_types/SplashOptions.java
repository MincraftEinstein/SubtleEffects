package einstein.subtle_effects.data.splash_types;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.data.color_providers.Colorable;
import einstein.subtle_effects.data.color_providers.NoneColorProvider;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record SplashOptions(SpriteSet sprites, ColorProviderType.ColorProvider colorProvider,
                            Optional<Either<Float, Boolean>> tinting,
                            Optional<Either<Float, Boolean>> transparency) implements Colorable {

    public record Data(Optional<ResourceLocation> spriteSetId,
                       Optional<ColorProviderType.ColorProvider> colorProvider,
                       Optional<Either<Float, Boolean>> tinting,
                       Optional<Either<Float, Boolean>> transparency) {

        public static final Data EMPTY = new Data(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        public static final Data DEFAULT = new Data(Optional.empty(), Optional.of(NoneColorProvider.INSTANCE), Optional.empty(), Optional.empty());

        public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("sprite_set").forGetter(Data::spriteSetId),
                ColorProviderType.CODEC.optionalFieldOf("color").forGetter(Data::colorProvider),
                Util.configurableFloatCodec("intensity").optionalFieldOf("tinting").forGetter(Data::tinting),
                Util.configurableFloatCodec("alpha").optionalFieldOf("transparency").forGetter(Data::transparency)
        ).apply(instance, (location, colorProvider, tinting, alpha) -> {
            if (location.isEmpty() && colorProvider.isEmpty() && alpha.isEmpty()) {
                return EMPTY;
            }
            return new Data(location, colorProvider, tinting, alpha);
        }));
    }
}
