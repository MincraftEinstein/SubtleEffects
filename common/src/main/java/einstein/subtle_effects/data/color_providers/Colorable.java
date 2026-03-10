package einstein.subtle_effects.data.color_providers;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.Optional;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public interface Colorable {

    ColorProviderType.ColorProvider colorProvider();

    Optional<Either<Float, Boolean>> tinting();

    default Vector3f getColorAndApplyTint(Level level, BlockPos pos, RandomSource random) {
        Vector3f color = colorProvider().provideColor(level, pos, random);
        float[] tintedColor = new float[] {color.x(), color.y(), color.z()};

        tinting().ifPresent(tinting -> {
            tintedColor[0] = 1;
            tintedColor[1] = 1;
            tintedColor[2] = 1;

            tinting.ifLeft(colorIntensity -> tint(colorIntensity, tintedColor, color))
                    .ifRight(useConfig -> {
                        if (useConfig) {
                            tint(ENTITIES.splashes.splashOverlayTint.get(), tintedColor, color);
                        }
                    });
        });

        return new Vector3f(tintedColor[0], tintedColor[1], tintedColor[2]);
    }

    static void tint(float colorIntensity, float[] tintedColor, Vector3f color) {
        if (colorIntensity <= 0) {
            return;
        }

        float whiteIntensity = 1 - colorIntensity;
        tintedColor[0] = whiteIntensity + (colorIntensity * color.x());
        tintedColor[1] = whiteIntensity + (colorIntensity * color.y());
        tintedColor[2] = whiteIntensity + (colorIntensity * color.z());
    }
}
