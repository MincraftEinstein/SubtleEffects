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
public record IntegerParticleOptions(ParticleType<?> type, int integer) implements ParticleOptions {

    public static final Deserializer<IntegerParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public IntegerParticleOptions fromCommand(ParticleType<IntegerParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new IntegerParticleOptions(type, reader.readInt());
        }

        @Override
        public IntegerParticleOptions fromNetwork(ParticleType<IntegerParticleOptions> type, FriendlyByteBuf buf) {
            return new IntegerParticleOptions(type, buf.readInt());
        }
    };

    public static Codec<IntegerParticleOptions> codec(ParticleType<IntegerParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("int").forGetter(IntegerParticleOptions::integer)
        ).apply(instance, i -> new IntegerParticleOptions(type, i)));
    }

    @Override
    public ParticleType<?> getType() {
        return type();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(integer);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), integer);
    }
}
