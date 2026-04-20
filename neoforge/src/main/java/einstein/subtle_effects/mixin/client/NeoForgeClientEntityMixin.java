package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.FluidLogicAccessor;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class NeoForgeClientEntityMixin implements FluidLogicAccessor {

    @Unique
    private final Object2DoubleMap<FluidDefinition> subtleEffects$fluidDefHeight = new Object2DoubleArrayMap<>();

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Inject(method = "playStepSound", at = @At(value = "HEAD"), cancellable = true)
    private void stepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (subtleEffects$me instanceof SnowGolem && ModConfigs.ENTITIES.snowGolemStepSounds) {
            SoundType soundType = SoundType.SNOW;
            subtleEffects$me.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15F, soundType.getPitch());
            ci.cancel();
        }
    }

    @Override
    public Object2DoubleMap<FluidDefinition> subtleEffects$getFluidDefinitionHeight() {
        return subtleEffects$fluidDefHeight;
    }
}
