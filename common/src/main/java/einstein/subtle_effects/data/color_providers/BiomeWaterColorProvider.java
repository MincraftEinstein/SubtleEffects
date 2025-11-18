package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class BiomeWaterColorProvider extends ColorProviderType.ColorProvider {

    public static final BiomeWaterColorProvider INSTANCE = new BiomeWaterColorProvider();
    public static final MapCodec<BiomeWaterColorProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public ColorProviderType<?> getType() {
        return ColorProviderType.BIOME_WATER;
    }

    @Override
    public Vector3f provideColor(Level level, BlockPos pos, RandomSource random) {
        int waterColor = level.getBiome(pos).value().getWaterColor();
        return Vec3.fromRGB24(waterColor).toVector3f();
    }
}
