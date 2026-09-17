package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.data.color_providers.ConstantColorProvider;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.GsonHelper;

@SuppressWarnings("deprecation")
public record ColorProviderParticleOptions(ParticleType<? extends ColorProviderParticleOptions> type,
                                           ColorProviderType.ColorProvider provider) implements ParticleOptions {

    public static final Deserializer<ColorProviderParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public ColorProviderParticleOptions fromCommand(ParticleType<ColorProviderParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            ColorProviderType.ColorProvider provider = ColorProviderType.CODEC.parse(JsonOps.INSTANCE, GsonHelper.parse(reader.getString())).result().orElseThrow(() ->
                    new SimpleCommandExceptionType(Component.translatable("argument.enum.invalid", reader.getCursor())).createWithContext(reader));
            return new ColorProviderParticleOptions(type, provider);
        }

        @Override
        public ColorProviderParticleOptions fromNetwork(ParticleType<ColorProviderParticleOptions> type, FriendlyByteBuf buf) {
            return new ColorProviderParticleOptions(type, ColorProviderType.read(buf));
        }
    };

    public ColorProviderParticleOptions(ParticleType<? extends ColorProviderParticleOptions> type, int color) {
        this(type, new ConstantColorProvider(color));
    }

    public ColorProviderParticleOptions(ParticleType<? extends ColorProviderParticleOptions> type, float red, float green, float blue) {
        this(type, MathUtil.as8BitChannel(red) << 16 | MathUtil.as8BitChannel(green) << 8 | MathUtil.as8BitChannel(blue));
    }

    public static Codec<ColorProviderParticleOptions> codec(ParticleType<? extends ColorProviderParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                ColorProviderType.CODEC.fieldOf("color").forGetter(ColorProviderParticleOptions::provider)
        ).apply(instance, provider -> new ColorProviderParticleOptions(type, provider)));
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        ColorProviderType.write(buf, provider);
    }

    @Override
    public String writeToString() {
        return String.format("%s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(type), ColorProviderType.CODEC.encodeStart(JsonOps.INSTANCE, provider).result().orElse(null));
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
