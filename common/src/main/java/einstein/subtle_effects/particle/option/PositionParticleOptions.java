package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

@SuppressWarnings("deprecation")
public record PositionParticleOptions(ParticleType<?> type, BlockPos pos) implements ParticleOptions {

    public static final Deserializer<PositionParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public PositionParticleOptions fromCommand(ParticleType<PositionParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int x = reader.readInt();
            reader.expect(' ');
            int y = reader.readInt();
            reader.expect(' ');
            int z = reader.readInt();
            return new PositionParticleOptions(type, new BlockPos(x, y, z));
        }

        @Override
        public PositionParticleOptions fromNetwork(ParticleType<PositionParticleOptions> type, FriendlyByteBuf buf) {
            return new PositionParticleOptions(type, buf.readBlockPos());
        }
    };

    public static Codec<PositionParticleOptions> codec(ParticleType<PositionParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(PositionParticleOptions::pos)
        ).apply(instance, pos -> new PositionParticleOptions(type, pos)));
    }

    @Override
    public ParticleType<?> getType() {
        return type();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), pos.getX(), pos.getY(), pos.getZ());
    }
}
