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
import net.minecraft.world.item.DyeColor;

import java.util.Locale;

public record SheepFluffParticleOptions(DyeColor color, int sheepId, boolean isJeb) implements ParticleOptions {

    @SuppressWarnings("deprecation")
    public static final Deserializer<SheepFluffParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public SheepFluffParticleOptions fromCommand(ParticleType<SheepFluffParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int color = reader.readInt();
            reader.expect(' ');
            int sheepId = reader.readInt();
            reader.expect(' ');
            boolean isJeb = reader.readBoolean();
            return new SheepFluffParticleOptions(DyeColor.byId(color), sheepId, isJeb);
        }

        @Override
        public SheepFluffParticleOptions fromNetwork(ParticleType<SheepFluffParticleOptions> type, FriendlyByteBuf buf) {
            return new SheepFluffParticleOptions(DyeColor.byId(buf.readByte()), buf.readInt(), buf.readBoolean());
        }
    };

    public static Codec<SheepFluffParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DyeColor.CODEC.fieldOf("color").forGetter(SheepFluffParticleOptions::color),
            Codec.INT.fieldOf("sheepId").forGetter(SheepFluffParticleOptions::sheepId),
            Codec.BOOL.fieldOf("isJeb").forGetter(SheepFluffParticleOptions::isJeb)
    ).apply(instance, SheepFluffParticleOptions::new));

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeByte(color.getId());
        buf.writeInt(sheepId);
        buf.writeBoolean(isJeb);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), color, sheepId);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SHEEP_FLUFF.get();
    }
}
