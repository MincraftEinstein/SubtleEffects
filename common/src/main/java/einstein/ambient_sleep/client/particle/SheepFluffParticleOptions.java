package einstein.ambient_sleep.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.ambient_sleep.init.ModParticles;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

@SuppressWarnings("deprecation")
public class SheepFluffParticleOptions extends DustParticleOptionsBase {

    public static final Codec<SheepFluffParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(SheepFluffParticleOptions::getColor)
    ).apply(instance, SheepFluffParticleOptions::new));

    public static final Deserializer<SheepFluffParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public SheepFluffParticleOptions fromCommand(ParticleType<SheepFluffParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            return new SheepFluffParticleOptions(readVector3f(reader));
        }

        @Override
        public SheepFluffParticleOptions fromNetwork(ParticleType<SheepFluffParticleOptions> type, FriendlyByteBuf buf) {
            return new SheepFluffParticleOptions(readVector3f(buf));
        }
    };

    public SheepFluffParticleOptions(Vector3f color) {
        super(color, 1);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SHEEP_FLUFF.get();
    }
}
