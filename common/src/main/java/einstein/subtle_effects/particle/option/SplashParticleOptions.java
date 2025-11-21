package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

@SuppressWarnings("deprecation")
public record SplashParticleOptions(ParticleType<SplashParticleOptions> type, float xScale,
                                    float yScale) implements ParticleOptions {

    public static final Deserializer<SplashParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public SplashParticleOptions fromCommand(ParticleType<SplashParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float xScale = reader.readFloat();
            reader.expect(' ');
            float yScale = reader.readFloat();

            return new SplashParticleOptions(type, xScale, yScale);
        }

        @Override
        public SplashParticleOptions fromNetwork(ParticleType<SplashParticleOptions> type, FriendlyByteBuf buf) {
            return new SplashParticleOptions(type, buf.readFloat(), buf.readFloat());
        }
    };

    public static Codec<SplashParticleOptions> codec(ParticleType<SplashParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("xScale").forGetter(SplashParticleOptions::xScale),
                Codec.FLOAT.fieldOf("yScale").forGetter(SplashParticleOptions::yScale)
        ).apply(instance, (xScale, yScale) -> new SplashParticleOptions(type, xScale, yScale)));
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(xScale);
        buf.writeFloat(yScale);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), xScale, yScale);
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
