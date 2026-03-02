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
public record SplashParticleOptions(ResourceLocation fluidDefinitionId, float horizontalScale,
                                    float verticalScale) implements ParticleOptions {

    public static final Deserializer<SplashParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public SplashParticleOptions fromCommand(ParticleType<SplashParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            ResourceLocation fluidDefinitionId = ResourceLocation.read(reader);
            reader.expect(' ');
            float horizontalScale = reader.readFloat();
            reader.expect(' ');
            float verticalScale = reader.readFloat();

            return new SplashParticleOptions(fluidDefinitionId, horizontalScale, verticalScale);
        }

        @Override
        public SplashParticleOptions fromNetwork(ParticleType<SplashParticleOptions> type, FriendlyByteBuf buf) {
            return new SplashParticleOptions(buf.readResourceLocation(), buf.readFloat(), buf.readFloat());
        }
    };

    public static Codec<SplashParticleOptions> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("fluid_definition").forGetter(SplashParticleOptions::fluidDefinitionId),
                Codec.FLOAT.fieldOf("horizontalScale").forGetter(SplashParticleOptions::horizontalScale),
                Codec.FLOAT.fieldOf("verticalScale").forGetter(SplashParticleOptions::verticalScale)
        ).apply(instance, SplashParticleOptions::new));
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(fluidDefinitionId);
        buf.writeFloat(horizontalScale);
        buf.writeFloat(verticalScale);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), fluidDefinitionId, horizontalScale, verticalScale);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SPLASH.get();
    }
}
