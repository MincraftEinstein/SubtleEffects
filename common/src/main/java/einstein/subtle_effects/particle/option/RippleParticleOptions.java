package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("deprecation")
public record RippleParticleOptions(ParticleType<RippleParticleOptions> type, ResourceLocation fluidDefinitionId,
                                    float scale, boolean fromSplash) implements ParticleOptions {

    public static final Deserializer<RippleParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public RippleParticleOptions fromCommand(ParticleType<RippleParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            ResourceLocation fluidDefinitionId = ResourceLocation.read(reader);
            reader.expect(' ');
            float scale = reader.readFloat();
            reader.expect(' ');
            boolean fromSplash = reader.readBoolean();
            return new RippleParticleOptions(type, fluidDefinitionId, scale, fromSplash);
        }

        @Override
        public RippleParticleOptions fromNetwork(ParticleType<RippleParticleOptions> type, FriendlyByteBuf buf) {
            return new RippleParticleOptions(type, buf.readResourceLocation(), buf.readFloat(), buf.readBoolean());
        }
    };

    public static Codec<RippleParticleOptions> codec(ParticleType<RippleParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                net.minecraft.resources.ResourceLocation.CODEC.fieldOf("fluid_definition").forGetter(RippleParticleOptions::fluidDefinitionId),
                Codec.FLOAT.fieldOf("scale").forGetter(RippleParticleOptions::scale),
                Codec.BOOL.fieldOf("from_splash").forGetter(RippleParticleOptions::fromSplash)
        ).apply(instance, (fluidDefinitionId, scale, fromSplash) -> new RippleParticleOptions(type, fluidDefinitionId, scale, fromSplash)));
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(fluidDefinitionId);
        buf.writeFloat(scale);
        buf.writeBoolean(fromSplash);
    }

    @Override
    public String writeToString() {
        return String.format("%s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), fluidDefinitionId, scale, fromSplash);
    }
}
