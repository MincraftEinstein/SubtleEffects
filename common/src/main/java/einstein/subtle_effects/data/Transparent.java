package einstein.subtle_effects.data;

import com.mojang.datafixers.util.Either;

import java.util.Optional;
import java.util.function.Function;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public interface Transparent {

    Optional<Either<Float, Boolean>> transparency();

    default float getAlpha() {
        return transparency().map(transparency ->
                transparency.mapRight(useConfig ->
                        useConfig ? ENTITIES.splashes.splashOverlayAlpha.get() : 1F
                ).map(Function.identity(), Function.identity())
        ).orElse(1F);
    }
}
