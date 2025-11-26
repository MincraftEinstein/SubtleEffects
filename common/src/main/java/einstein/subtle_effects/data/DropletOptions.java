package einstein.subtle_effects.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.data.color_providers.Colorable;
import einstein.subtle_effects.data.color_providers.NoneColorProvider;
import einstein.subtle_effects.data.splash_types.SplashOptionsData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Optional;

import static einstein.subtle_effects.data.splash_types.SplashOptionsData.configurableFloatCodec;

public record DropletOptions(ColorProviderType.ColorProvider colorProvider,
                             Optional<Either<Float, Boolean>> tinting,
                             Optional<RippleOptions> rippleOptions,
                             Optional<SimpleParticleType> landParticle) implements Colorable {

    public static final DropletOptions DEFAULT = new DropletOptions(NoneColorProvider.INSTANCE, Optional.empty(), Optional.empty(), Optional.empty());

    public static final Codec<DropletOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ColorProviderType.CODEC.fieldOf("color").forGetter(DropletOptions::colorProvider),
            configurableFloatCodec("intensity").optionalFieldOf("tinting").forGetter(DropletOptions::tinting),
            RippleOptions.CODEC.optionalFieldOf("ripple").forGetter(DropletOptions::rippleOptions),
            BuiltInRegistries.PARTICLE_TYPE.byNameCodec().comapFlatMap(options -> {
                if (options instanceof SimpleParticleType particle) {
                    return DataResult.success(particle);
                }
                return DataResult.error(() -> "Particle type is not a simple particle type: " + options);
            }, particle -> particle).optionalFieldOf("land_particle").forGetter(DropletOptions::landParticle)
    ).apply(instance, DropletOptions::new));

    public record RippleOptions(ColorProviderType.ColorProvider colorProvider, Optional<Either<Float, Boolean>> tinting,
                                Optional<Either<Float, Boolean>> transparency) implements Colorable {

        public static final RippleOptions DEFAULT = new RippleOptions(NoneColorProvider.INSTANCE, Optional.empty(), Optional.empty());

        public static final Codec<RippleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ColorProviderType.CODEC.fieldOf("color").forGetter(RippleOptions::colorProvider),
                configurableFloatCodec("intensity").optionalFieldOf("tinting").forGetter(RippleOptions::tinting),
                SplashOptionsData.configurableFloatCodec("alpha").optionalFieldOf("transparency").forGetter(RippleOptions::transparency)
        ).apply(instance, RippleOptions::new));
    }
}
