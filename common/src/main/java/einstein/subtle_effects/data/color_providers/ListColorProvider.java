package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.List;

public record ListColorProvider(List<ColorProviderType.ColorProvider> providers)
        implements ColorProviderType.ColorProvider {

    public static final MapCodec<ListColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ColorProviderType.CODEC.listOf().fieldOf("providers").forGetter(ListColorProvider::providers)
    ).apply(instance, ListColorProvider::new));

    @Override
    public ColorProviderType<?> getType() {
        return ColorProviderType.LIST;
    }

    @Override
    public Vector3f provideColor(Level level, BlockPos pos, RandomSource random) {
        return providers.get(random.nextInt(providers.size())).provideColor(level, pos, random);
    }
}
