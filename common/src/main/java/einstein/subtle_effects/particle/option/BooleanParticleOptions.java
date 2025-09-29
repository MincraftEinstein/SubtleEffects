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
public record BooleanParticleOptions(ParticleType<BooleanParticleOptions> type, boolean bool) implements ParticleOptions {

    public static final Deserializer<BooleanParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public BooleanParticleOptions fromCommand(ParticleType<BooleanParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new BooleanParticleOptions(type, reader.readBoolean());
        }

        @Override
        public BooleanParticleOptions fromNetwork(ParticleType<BooleanParticleOptions> type, FriendlyByteBuf buf) {
            return new BooleanParticleOptions(type, buf.readBoolean());
        }
    };

    public static Codec<BooleanParticleOptions> codec(ParticleType<BooleanParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("boolean").forGetter(BooleanParticleOptions::bool)
        ).apply(instance, bool -> new BooleanParticleOptions(type, bool)));
    }

    @Override
    public ParticleType<?> getType() {
        return type();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeBoolean(bool);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), bool);
    }
}
