package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

@SuppressWarnings("deprecation")
public record SplashEmitterParticleOptions(ResourceLocation fluidDefinitionId, float widthModifier,
                                           float heightModifier, float velocity,
                                           int entityId) implements ParticleOptions {

    public static final Deserializer<SplashEmitterParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public SplashEmitterParticleOptions fromCommand(ParticleType<SplashEmitterParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            ResourceLocation fluidDefinitionId = ResourceLocation.read(reader);
            reader.expect(' ');
            float widthModifier = reader.readFloat();
            reader.expect(' ');
            float heightModifier = reader.readFloat();
            reader.expect(' ');
            float velocity = reader.readFloat();
            reader.expect(' ');
            int entityId = reader.readInt();

            return new SplashEmitterParticleOptions(fluidDefinitionId, widthModifier, heightModifier, velocity, entityId);
        }

        @Override
        public SplashEmitterParticleOptions fromNetwork(ParticleType<SplashEmitterParticleOptions> type, FriendlyByteBuf buf) {
            return new SplashEmitterParticleOptions(buf.readResourceLocation(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt());
        }
    };

    public static Codec<SplashEmitterParticleOptions> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                        ResourceLocation.CODEC.fieldOf("fluid_definition").forGetter(SplashEmitterParticleOptions::fluidDefinitionId),
                        Codec.FLOAT.fieldOf("widthModifier").forGetter(SplashEmitterParticleOptions::widthModifier),
                        Codec.FLOAT.fieldOf("heightModifier").forGetter(SplashEmitterParticleOptions::heightModifier),
                        Codec.FLOAT.fieldOf("velocity").forGetter(SplashEmitterParticleOptions::velocity),
                        Codec.INT.fieldOf("entityId").forGetter(SplashEmitterParticleOptions::entityId)
                ).apply(instance, SplashEmitterParticleOptions::new)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SPLASH_EMITTER.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(fluidDefinitionId);
        buf.writeFloat(widthModifier);
        buf.writeFloat(heightModifier);
        buf.writeFloat(velocity);
        buf.writeInt(entityId);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), fluidDefinitionId, widthModifier, heightModifier, velocity, entityId);
    }
}
