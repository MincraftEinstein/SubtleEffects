package einstein.subtle_effects.util;

import com.mojang.serialization.Codec;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public enum SparkType implements StringRepresentable {
    SHORT_LIFE("short_life", ModParticles.SHORT_SPARK),
    LONG_LIFE("long_life", ModParticles.LONG_SPARK),
    FLOATING("floating", ModParticles.FLOATING_SPARK),
    METAL("metal", ModParticles.METAL_SPARK);

    public static final Codec<SparkType> CODEC = StringRepresentable.fromEnum(SparkType::values);

    private final String name;
    private final Supplier<ParticleType<ColorParticleOption>> type;

    SparkType(String name, Supplier<ParticleType<ColorParticleOption>> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public Supplier<ParticleType<ColorParticleOption>> getType() {
        return type;
    }
}
