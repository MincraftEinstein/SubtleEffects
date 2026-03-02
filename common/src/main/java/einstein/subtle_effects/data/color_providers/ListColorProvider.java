package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.List;

public record ListColorProvider(List<? extends ColorProviderType.ColorProvider> providers)
        implements ColorProviderType.ColorProvider {

    @SuppressWarnings("unchecked")
    public static final MapCodec<ListColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ColorProviderType.CODEC.listOf().fieldOf("providers").forGetter(listColorProvider -> (List<ColorProviderType.ColorProvider>) listColorProvider.providers)
    ).apply(instance, ListColorProvider::new));

    public static ListColorProvider fromIntList(List<Integer> color) {
        return new ListColorProvider(color.stream().map(ConstantColorProvider::new).toList());
    }

    @Override
    public ColorProviderType<?> getType() {
        return ColorProviderType.LIST;
    }

    @Override
    public Vector3f provideColor(Level level, BlockPos pos, RandomSource random) {
        return providers.get(random.nextInt(providers.size())).provideColor(level, pos, random);
    }
}
