package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

@SuppressWarnings("deprecation")
public class ColorParticleOptions extends DustParticleOptionsBase {

    public static Codec<ColorParticleOptions> codec(ParticleType<ColorParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(ColorParticleOptions::getColor)
        ).apply(instance, color -> new ColorParticleOptions(type, color)));
    }

    public static final Deserializer<ColorParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public ColorParticleOptions fromCommand(ParticleType<ColorParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            return new ColorParticleOptions(type, readVector3f(reader));
        }

        @Override
        public ColorParticleOptions fromNetwork(ParticleType<ColorParticleOptions> type, FriendlyByteBuf buf) {
            return new ColorParticleOptions(type, readVector3f(buf));
        }
    };

    private final ParticleType<ColorParticleOptions> type;

    public ColorParticleOptions(ParticleType<ColorParticleOptions> type, Vector3f color) {
        super(color, 1);
        this.type = type;
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}