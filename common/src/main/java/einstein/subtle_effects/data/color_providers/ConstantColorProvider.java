package einstein.subtle_effects.data.color_providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.util.CodecUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public record ConstantColorProvider(int color) implements ColorProviderType.ColorProvider {

    public static final MapCodec<ConstantColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CodecUtil.RGB_COLOR_CODEC.fieldOf("color").forGetter(ConstantColorProvider::color)
    ).apply(instance, ConstantColorProvider::new));

    @Override
    public ColorProviderType<?> getType() {
        return ColorProviderType.CONSTANT;
    }

    @Override
    public Vector3f provideColor(Level level, BlockPos pos, RandomSource random) {
        return Vec3.fromRGB24(color).toVector3f();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(color);
    }

    public static ColorProviderType.ColorProvider read(FriendlyByteBuf buf) {
        return new ConstantColorProvider(buf.readInt());
    }
}
