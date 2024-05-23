package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Locale;

@SuppressWarnings("deprecation")
public record CommandBlockParticleOptions(Direction direction) implements ParticleOptions {

    public static final Codec<CommandBlockParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Direction.CODEC.fieldOf("direction").forGetter(CommandBlockParticleOptions::direction)
    ).apply(instance, CommandBlockParticleOptions::new));

    public static final Deserializer<CommandBlockParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public CommandBlockParticleOptions fromCommand(ParticleType<CommandBlockParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String string = reader.readString();
            Direction direction = Direction.byName(string);
            if (direction != null) {
                return new CommandBlockParticleOptions(direction);
            }
            throw new SimpleCommandExceptionType(Component.translatable("argument.enum.invalid", string)).createWithContext(reader);
        }

        @Override
        public CommandBlockParticleOptions fromNetwork(ParticleType<CommandBlockParticleOptions> type, FriendlyByteBuf buf) {
            return new CommandBlockParticleOptions(buf.readEnum(Direction.class));
        }
    };

    @Override
    public ParticleType<?> getType() {
        return ModParticles.COMMAND_BLOCK.get();
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
