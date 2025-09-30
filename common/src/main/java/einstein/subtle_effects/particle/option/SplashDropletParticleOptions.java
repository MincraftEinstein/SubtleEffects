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
public record SplashDropletParticleOptions(ParticleType<SplashDropletParticleOptions> type, float scale,
                                           float colorIntensity, boolean isSilent) implements ParticleOptions {

    public static final Deserializer<SplashDropletParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public SplashDropletParticleOptions fromCommand(ParticleType<SplashDropletParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float scale = reader.readFloat();
            reader.expect(' ');
            float colorIntensity = reader.readFloat();
            reader.expect(' ');
            boolean isSilent = reader.readBoolean();

            return new SplashDropletParticleOptions(type, scale, colorIntensity, isSilent);
        }

        @Override
        public SplashDropletParticleOptions fromNetwork(ParticleType<SplashDropletParticleOptions> type, FriendlyByteBuf buf) {
            return new SplashDropletParticleOptions(type, buf.readFloat(), buf.readFloat(), buf.readBoolean());
        }
    };

    public SplashDropletParticleOptions(ParticleType<SplashDropletParticleOptions> type, float scale, float colorIntensity) {
        this(type, scale, colorIntensity, false);
    }

    public SplashDropletParticleOptions(ParticleType<SplashDropletParticleOptions> type, float scale) {
        this(type, scale, 1);
    }

    public static Codec<SplashDropletParticleOptions> codec(ParticleType<SplashDropletParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("scale").forGetter(SplashDropletParticleOptions::scale),
                Codec.FLOAT.fieldOf("colorIntensity").forGetter(SplashDropletParticleOptions::colorIntensity),
                Codec.BOOL.fieldOf("isSilent").forGetter(SplashDropletParticleOptions::isSilent)
        ).apply(instance, (scale, colorIntensity, isSilent) -> new SplashDropletParticleOptions(type, scale, colorIntensity, isSilent)));
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(scale);
        buf.writeFloat(colorIntensity);
        buf.writeBoolean(isSilent);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), scale, colorIntensity, isSilent);
    }
}
