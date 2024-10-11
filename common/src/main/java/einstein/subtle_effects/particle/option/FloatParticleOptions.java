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
public record FloatParticleOptions(ParticleType<?> type, float f) implements ParticleOptions {

    public static final Deserializer<FloatParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public FloatParticleOptions fromCommand(ParticleType<FloatParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new FloatParticleOptions(type, reader.readFloat());
        }

        @Override
        public FloatParticleOptions fromNetwork(ParticleType<FloatParticleOptions> type, FriendlyByteBuf buf) {
            return new FloatParticleOptions(type, buf.readFloat());
        }
    };

    public static Codec<FloatParticleOptions> codec(ParticleType<FloatParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("float").forGetter(FloatParticleOptions::f)
        ).apply(instance, f -> new FloatParticleOptions(type, f)));
    }

    @Override
    public ParticleType<?> getType() {
        return type();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(f);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), f);
    }
}
