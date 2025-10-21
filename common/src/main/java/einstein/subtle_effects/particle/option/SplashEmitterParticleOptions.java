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
public record SplashEmitterParticleOptions(ParticleType<SplashEmitterParticleOptions> type, float widthModifier,
                                           float heightModifier, float velocity,
                                           int entityId) implements ParticleOptions {

    public static final Deserializer<SplashEmitterParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public SplashEmitterParticleOptions fromCommand(ParticleType<SplashEmitterParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float widthModifier = reader.readFloat();
            reader.expect(' ');
            float heightModifier = reader.readFloat();
            reader.expect(' ');
            float velocity = reader.readFloat();
            reader.expect(' ');
            int entityId = reader.readInt();

            return new SplashEmitterParticleOptions(type, widthModifier, heightModifier, velocity, entityId);
        }

        @Override
        public SplashEmitterParticleOptions fromNetwork(ParticleType<SplashEmitterParticleOptions> type, FriendlyByteBuf buf) {
            return new SplashEmitterParticleOptions(type, buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt());
        }
    };

    public static Codec<SplashEmitterParticleOptions> codec(ParticleType<SplashEmitterParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                        Codec.FLOAT.fieldOf("widthModifier").forGetter(SplashEmitterParticleOptions::widthModifier),
                        Codec.FLOAT.fieldOf("heightModifier").forGetter(SplashEmitterParticleOptions::heightModifier),
                        Codec.FLOAT.fieldOf("velocity").forGetter(SplashEmitterParticleOptions::velocity),
                        Codec.INT.fieldOf("entityId").forGetter(SplashEmitterParticleOptions::entityId)
                ).apply(instance, (widthModifier, heightModifier, velocity, entityId) ->
                        new SplashEmitterParticleOptions(type, widthModifier, heightModifier, velocity, entityId))
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(widthModifier);
        buf.writeFloat(heightModifier);
        buf.writeFloat(velocity);
        buf.writeInt(entityId);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), widthModifier, heightModifier, velocity, entityId);
    }
}
