package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class NoneColorProvider implements ColorProviderType.ColorProvider {

    public static final NoneColorProvider INSTANCE = new NoneColorProvider();
    public static final MapCodec<NoneColorProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public ColorProviderType<?> getType() {
        return ColorProviderType.NONE;
    }

    @Override
    public Vector3f provideColor(Level level, BlockPos pos, RandomSource random) {
        return new Vector3f(1);
    }
}
