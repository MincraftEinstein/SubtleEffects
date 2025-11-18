package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.SparkProviderData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ConstantColorProvider extends ColorProviderType.ColorProvider {

    public static final MapCodec<ConstantColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SparkProviderData.Options.RGB_COLOR_CODEC.fieldOf("color").forGetter(ConstantColorProvider::getColor)
    ).apply(instance, ConstantColorProvider::new));

    private final int color;

    public ConstantColorProvider(Integer color) {
        this.color = color;
    }

    @Override
    public ColorProviderType<?> getType() {
        return ColorProviderType.CONSTANT;
    }

    @Override
    public Vector3f provideColor(Level level, BlockPos pos, RandomSource random) {
        return Vec3.fromRGB24(color).toVector3f();
    }

    public int getColor() {
        return color;
    }
}
