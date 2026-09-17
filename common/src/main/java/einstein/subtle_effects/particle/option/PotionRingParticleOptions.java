package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

import java.util.Locale;

@SuppressWarnings("deprecation")
public record PotionRingParticleOptions(ParticleType<PotionRingParticleOptions> type,
                                        ColorProviderType.ColorProvider provider,
                                        boolean isHarmful, int entityId) implements ParticleOptions {

    public static final Deserializer<PotionRingParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public PotionRingParticleOptions fromCommand(ParticleType<PotionRingParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            ColorProviderType.ColorProvider provider = ColorProviderType.CODEC.parse(JsonOps.INSTANCE, GsonHelper.parse(reader.getString())).result().orElseThrow(() ->
                    new SimpleCommandExceptionType(Component.translatable("argument.enum.invalid", reader.getCursor())).createWithContext(reader));
            reader.expect(' ');
            boolean isHarmful = reader.readBoolean();
            reader.expect(' ');
            int entityId = reader.readInt();
            reader.expect(' ');
            return new PotionRingParticleOptions(type, provider, isHarmful, entityId);
        }

        @Override
        public PotionRingParticleOptions fromNetwork(ParticleType<PotionRingParticleOptions> type, FriendlyByteBuf buf) {
            return new PotionRingParticleOptions(type, ColorProviderType.read(buf), buf.readBoolean(), buf.readInt());
        }
    };

    public static Codec<PotionRingParticleOptions> codec(ParticleType<PotionRingParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                ColorProviderType.CODEC.fieldOf("color").forGetter(PotionRingParticleOptions::provider),
                Codec.BOOL.fieldOf("is_harmful").forGetter(PotionRingParticleOptions::isHarmful),
                Codec.INT.fieldOf("entity_id").forGetter(PotionRingParticleOptions::entityId)
        ).apply(instance, (color, isHarmful, entityId) -> new PotionRingParticleOptions(type, color, isHarmful, entityId)));
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        ColorProviderType.write(buf, provider);
        buf.writeBoolean(isHarmful);
        buf.writeInt(entityId);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(type),
                ColorProviderType.CODEC.encodeStart(JsonOps.INSTANCE, provider).result().orElse(null),
                isHarmful, entityId
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
