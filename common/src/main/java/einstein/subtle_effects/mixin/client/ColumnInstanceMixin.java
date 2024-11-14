package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.util.WeatherColumnInstance;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WeatherEffectRenderer.ColumnInstance.class)
public class ColumnInstanceMixin implements WeatherColumnInstance {

    @Unique
    private Level subtleEffects$level;
    @Unique
    private BlockPos subtleEffects$pos;

    @Override
    public Level subtleEffects$getLevel() {
        return subtleEffects$level;
    }

    @Override
    public BlockPos subtleEffects$getPos() {
        return subtleEffects$pos;
    }

    @Override
    public void subtleEffects$set(Level level, BlockPos pos) {
        subtleEffects$level = level;
        subtleEffects$pos = pos;
    }
}
