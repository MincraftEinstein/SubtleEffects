package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.ticking.tickers.geyser.GeyserType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

public record GeyserSpoutParticleOptions(GeyserType type, int lifeTime) implements ParticleOptions {

    @SuppressWarnings("deprecation")
    public static final Deserializer<GeyserSpoutParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public GeyserSpoutParticleOptions fromCommand(ParticleType<GeyserSpoutParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int typeId = reader.readInt();
            reader.expect(' ');
            int lifeTime = reader.readInt();
            return new GeyserSpoutParticleOptions(GeyserType.values()[typeId], lifeTime);
        }

        @Override
        public GeyserSpoutParticleOptions fromNetwork(ParticleType<GeyserSpoutParticleOptions> type, FriendlyByteBuf buf) {
            return new GeyserSpoutParticleOptions(GeyserType.values()[buf.readByte()], buf.readInt());
        }
    };

    public static final Codec<GeyserSpoutParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GeyserType.CODEC.fieldOf("type").forGetter(GeyserSpoutParticleOptions::type),
            Codec.INT.fieldOf("lifeTime").forGetter(GeyserSpoutParticleOptions::lifeTime)
    ).apply(instance, GeyserSpoutParticleOptions::new));

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeByte(type.ordinal());
        buf.writeInt(lifeTime);
    }

    @Override
    public String writeToString() {
        return String.format("%s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), type, lifeTime);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.GEYSER_SPOUT.get();
    }
}
