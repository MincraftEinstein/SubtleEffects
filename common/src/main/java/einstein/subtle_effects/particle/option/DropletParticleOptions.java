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

@SuppressWarnings("deprecation")
public record DropletParticleOptions(ResourceLocation fluidDefinitionId, boolean fromSplash,
                                     float scale, boolean isSilent) implements ParticleOptions {

    public static final Deserializer<DropletParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public DropletParticleOptions fromCommand(ParticleType<DropletParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            ResourceLocation fluidDefinitionId = ResourceLocation.read(reader);
            reader.expect(' ');
            boolean fromSplash = reader.readBoolean();
            reader.expect(' ');
            float scale = reader.readFloat();
            reader.expect(' ');
            boolean isSilent = reader.readBoolean();
            return new DropletParticleOptions(fluidDefinitionId, fromSplash, scale, isSilent);
        }

        @Override
        public DropletParticleOptions fromNetwork(ParticleType<DropletParticleOptions> type, FriendlyByteBuf buf) {
            return new DropletParticleOptions(buf.readResourceLocation(), buf.readBoolean(), buf.readFloat(), buf.readBoolean());
        }
    };

    public static final Codec<DropletParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("fluid_definition").forGetter(DropletParticleOptions::fluidDefinitionId),
            Codec.BOOL.fieldOf("from_splash").forGetter(DropletParticleOptions::fromSplash),
            Codec.FLOAT.fieldOf("scale").forGetter(DropletParticleOptions::scale),
            Codec.BOOL.fieldOf("is_silent").forGetter(DropletParticleOptions::isSilent)
    ).apply(instance, DropletParticleOptions::new));

    @Override
    public ParticleType<?> getType() {
        return ModParticles.DROPLET.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(fluidDefinitionId);
        buf.writeBoolean(fromSplash);
        buf.writeFloat(scale);
        buf.writeBoolean(isSilent);
    }

    @Override
    public String writeToString() {
        return String.format("%s %s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), fluidDefinitionId, fromSplash, scale);
    }
}
