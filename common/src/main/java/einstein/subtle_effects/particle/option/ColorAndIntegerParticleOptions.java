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
public record ColorAndIntegerParticleOptions(ParticleType<?> type, int color, int integer) implements ParticleOptions {

    public static final Deserializer<ColorAndIntegerParticleOptions> DESERIALIZER = new Deserializer<ColorAndIntegerParticleOptions>() {

        @Override
        public ColorAndIntegerParticleOptions fromCommand(ParticleType<ColorAndIntegerParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            return new ColorAndIntegerParticleOptions(type, reader.readInt(), reader.readInt());
        }

        @Override
        public ColorAndIntegerParticleOptions fromNetwork(ParticleType<ColorAndIntegerParticleOptions> type, FriendlyByteBuf buf) {
            return new ColorAndIntegerParticleOptions(type, buf.readInt(), buf.readInt());
        }
    };

    public static Codec<ColorAndIntegerParticleOptions> codec(ParticleType<ColorAndIntegerParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("color").forGetter(ColorAndIntegerParticleOptions::color),
                Codec.INT.fieldOf("integer").forGetter(ColorAndIntegerParticleOptions::integer)
        ).apply(instance, (color, integer) -> new ColorAndIntegerParticleOptions(type, color, integer)));
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(color);
        buf.writeInt(integer);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), color, integer);
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
