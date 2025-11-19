package einstein.subtle_effects.data.splash_types;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.FluidPair;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record SplashTypeData(ResourceLocation fluidPair, int lightEmission, SplashOptionsData splashOptions,
                             Either<Boolean, SplashOptionsData> splashOverlayOptions,
                             SplashOptionsData splashRippleOptions, Optional<SplashOptionsData> dropletOptions) {

    public record SplashType(FluidPair fluidPair, int lightEmission, SplashOptionsData.SplashOptions splashOptions,
                             SplashOptionsData.SplashOptions splashOverlayOptions,
                             SplashOptionsData.SplashOptions splashRippleOptions,
                             SplashOptionsData.SplashOptions dropletOptions) {

    }

    public static final Codec<SplashTypeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("fluid_pair").forGetter(SplashTypeData::fluidPair),
            Codec.intRange(0, 15).optionalFieldOf("light_emission", 0).forGetter(SplashTypeData::lightEmission),
            SplashOptionsData.CODEC.optionalFieldOf("splash", SplashOptionsData.EMPTY).forGetter(SplashTypeData::splashOptions),
            Codec.either(Codec.BOOL, SplashOptionsData.CODEC).optionalFieldOf("splash_overlay", Either.left(false)).forGetter(SplashTypeData::splashOverlayOptions),
            SplashOptionsData.CODEC.optionalFieldOf("splash_ripple", SplashOptionsData.EMPTY).forGetter(SplashTypeData::splashRippleOptions),
            SplashOptionsData.CODEC_NO_SPRITES.optionalFieldOf("droplets").forGetter(SplashTypeData::dropletOptions)
    ).apply(instance, SplashTypeData::new));
}
