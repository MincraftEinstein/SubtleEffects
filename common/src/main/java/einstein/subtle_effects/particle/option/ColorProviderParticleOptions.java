package einstein.subtle_effects.particle.option;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.data.color_providers.ConstantColorProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;

public record ColorProviderParticleOptions(ParticleType<? extends ColorProviderParticleOptions> type,
                                           ColorProviderType.ColorProvider provider) implements ParticleOptions {

    public ColorProviderParticleOptions(ParticleType<? extends ColorProviderParticleOptions> type, int color) {
        this(type, new ConstantColorProvider(color));
    }

    public ColorProviderParticleOptions(ParticleType<? extends ColorProviderParticleOptions> type, float red, float green, float blue) {
        this(type, FastColor.as8BitChannel(red) << 16 | FastColor.as8BitChannel(green) << 8 | FastColor.as8BitChannel(blue));
    }

    public static MapCodec<ColorProviderParticleOptions> codec(ParticleType<? extends ColorProviderParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ColorProviderType.CODEC.fieldOf("color").forGetter(ColorProviderParticleOptions::provider)
        ).apply(instance, provider -> new ColorProviderParticleOptions(type, provider)));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, ColorProviderParticleOptions> streamCodec(ParticleType<? extends ColorProviderParticleOptions> type) {
        return StreamCodec.composite(
                ColorProviderType.STREAM_CODEC, ColorProviderParticleOptions::provider,
                provider -> new ColorProviderParticleOptions(type, provider)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
