package einstein.subtle_effects.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface WeatherColumnInstance {

    Level subtleEffects$getLevel();

    BlockPos subtleEffects$getPos();

    void subtleEffects$set(Level level, BlockPos pos);
}
