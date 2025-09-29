package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Locale;

@SuppressWarnings("deprecation")
public record DirectionParticleOptions(ParticleType<DirectionParticleOptions> type,
                                       Direction direction) implements ParticleOptions {

    public static Codec<DirectionParticleOptions> codec(ParticleType<DirectionParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                net.minecraft.core.Direction.CODEC.fieldOf("direction").forGetter(DirectionParticleOptions::direction)
        ).apply(instance, direction -> new DirectionParticleOptions(type, direction)));
    }

    public static final Deserializer<DirectionParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public DirectionParticleOptions fromCommand(ParticleType<DirectionParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String string = reader.readString();
            Direction direction = net.minecraft.core.Direction.byName(string);
            if (direction != null) {
                return new DirectionParticleOptions(type, direction);
            }
            throw new SimpleCommandExceptionType(Component.translatable("argument.enum.invalid", string)).createWithContext(reader);
        }

        @Override
        public DirectionParticleOptions fromNetwork(ParticleType<DirectionParticleOptions> type, FriendlyByteBuf buf) {
            return new DirectionParticleOptions(type, buf.readEnum(Direction.class));
        }
    };

    @Override
    public ParticleType<?> getType() {
        return type();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeEnum(direction);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), direction);
    }
}
