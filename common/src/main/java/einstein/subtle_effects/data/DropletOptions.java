package einstein.subtle_effects.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.color_providers.ColorProviderType;
import einstein.subtle_effects.data.color_providers.Colorable;
import einstein.subtle_effects.data.color_providers.NoneColorProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

import static einstein.subtle_effects.data.splash_types.SplashOptionsData.configurableFloatCodec;

public record DropletOptions(ColorProviderType.ColorProvider colorProvider,
                             Optional<Either<Float, Boolean>> tinting) implements Colorable {

    public static final DropletOptions DEFAULT = new DropletOptions(NoneColorProvider.INSTANCE, Optional.empty());

    public static final Codec<DropletOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ColorProviderType.CODEC.fieldOf("color").forGetter(DropletOptions::colorProvider),
            configurableFloatCodec("intensity").optionalFieldOf("tinting").forGetter(DropletOptions::tinting)
    ).apply(instance, DropletOptions::new));

    public static final StreamCodec<ByteBuf, DropletOptions> STREAM_CODEC = StreamCodec.composite(
            ColorProviderType.STREAM_CODEC, DropletOptions::colorProvider,
            ByteBufCodecs.optional(ByteBufCodecs.fromCodec(configurableFloatCodec("intensity"))), DropletOptions::tinting,
            DropletOptions::new
    );
}
