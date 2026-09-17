package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

@SuppressWarnings("deprecation")
public record ProjectileSplatParticleOptions(ParticleType<ProjectileSplatParticleOptions> type, Direction direction,
                                             BlockPos pos) implements ParticleOptions {

    public static final Deserializer<ProjectileSplatParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public ProjectileSplatParticleOptions fromCommand(ParticleType<ProjectileSplatParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String string = reader.readString();
            Direction direction = Direction.byName(string);
            reader.expect(' ');
            int x = reader.readInt();
            reader.expect(' ');
            int y = reader.readInt();
            reader.expect(' ');
            int z = reader.readInt();
            if (direction != null) {
                return new ProjectileSplatParticleOptions(type, direction, new BlockPos(x, y, z));
            }
            throw new SimpleCommandExceptionType(Component.translatable("argument.enum.invalid", string)).createWithContext(reader);
        }

        @Override
        public ProjectileSplatParticleOptions fromNetwork(ParticleType<ProjectileSplatParticleOptions> type, FriendlyByteBuf buf) {
            return new ProjectileSplatParticleOptions(type, buf.readEnum(Direction.class), buf.readBlockPos());
        }
    };

    public static Codec<ProjectileSplatParticleOptions> codec(ParticleType<ProjectileSplatParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Direction.CODEC.fieldOf("direction").forGetter(ProjectileSplatParticleOptions::direction),
                BlockPos.CODEC.fieldOf("pos").forGetter(ProjectileSplatParticleOptions::pos)
        ).apply(instance, (direction, pos) -> new ProjectileSplatParticleOptions(type, direction, pos)));
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeEnum(direction);
        buf.writeBlockPos(pos);
    }

    @Override
    public String writeToString() {
        return String.format("%s %s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(type), direction, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
