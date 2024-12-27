package einstein.subtle_effects.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;

public record Box(Vec3 min, Vec3 max) {

    public static final Codec<Box> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("min").forGetter(Box::min),
            Vec3.CODEC.fieldOf("max").forGetter(Box::max)
    ).apply(instance, Box::new));

    public Box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this(new Vec3(minX, minY, minZ), new Vec3(maxX, maxY, maxZ));
    }

    public Box() {
        this(Vec3.ZERO, new Vec3(1, 1, 1));
    }

    public Box add(double x, double y, double z) {
        return new Box(min.add(x, y, z), max.add(x, y, z));
    }
}
