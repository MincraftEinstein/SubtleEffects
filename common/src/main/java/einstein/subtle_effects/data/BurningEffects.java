package einstein.subtle_effects.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record BurningEffects(ColorProviderType.ColorProvider colorProvider,
                             Optional<SimpleParticleType> flameParticle) {

    public record Data(Identifier id, boolean isPrometheus,
                       ColorProviderType.ColorProvider colorProvider,
                       Optional<SimpleParticleType> flameParticle) {

        public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(Data::id),
                Codec.STRING.comapFlatMap(string -> {
                    if (string.equals("prometheus")) {
                        return DataResult.success(true);
                    }
                    else if (string.equals("dyed_flames")) {
                        return DataResult.success(false);
                    }
                    return DataResult.error(() -> "Invalid fire type provider '" + string + "' for burning effects, must be either 'prometheus' or 'dyed_flames'");
                }, isPrometheus -> isPrometheus ? "prometheus" : "dyed_flames").fieldOf("provider").forGetter(Data::isPrometheus),
                ColorProviderType.CODEC.fieldOf("color").forGetter(Data::colorProvider),
                Util.SIMPLE_PARTICLE_TYPE_CODEC.optionalFieldOf("flame_particle").forGetter(Data::flameParticle)
        ).apply(instance, Data::new));
    }
}
