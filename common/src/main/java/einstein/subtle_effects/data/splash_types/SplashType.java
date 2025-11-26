package einstein.subtle_effects.data.splash_types;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.DropletOptions;

import java.util.Optional;

public record SplashType(SplashOptions splashOptions,
                         SplashOptions splashOverlayOptions,
                         SplashOptions splashRippleOptions,
                         Optional<DropletOptions> dropletOptions) {

    public record Data(SplashOptions.Data splashOptions,
                       Either<Boolean, SplashOptions.Data> splashOverlayOptions,
                       SplashOptions.Data splashRippleOptions, Optional<DropletOptions> dropletOptions) {

        public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SplashOptions.Data.CODEC.optionalFieldOf("splash", SplashOptions.Data.EMPTY).forGetter(SplashType.Data::splashOptions),
                Codec.either(Codec.BOOL, SplashOptions.Data.CODEC).optionalFieldOf("splash_overlay", Either.left(false)).forGetter(SplashType.Data::splashOverlayOptions),
                SplashOptions.Data.CODEC.optionalFieldOf("splash_ripple", SplashOptions.Data.EMPTY).forGetter(SplashType.Data::splashRippleOptions),
                DropletOptions.CODEC.optionalFieldOf("droplets").forGetter(SplashType.Data::dropletOptions)
        ).apply(instance, SplashType.Data::new));
    }
}
