package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.FluidLogicAccessor;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class FabricClientEntityMixin implements FluidLogicAccessor {

    @Unique
    private final Object2DoubleMap<FluidDefinition> subtleEffects$fluidPairHeight = new Object2DoubleArrayMap<>();

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @ModifyExpressionValue(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType()Lnet/minecraft/world/level/block/SoundType;"))
    private SoundType stepSound(SoundType soundType) {
        if (subtleEffects$me instanceof SnowGolem && ModConfigs.ENTITIES.snowGolemStepSounds) {
            return SoundType.SNOW;
        }
        return soundType;
    }

    @Override
    public Object2DoubleMap<FluidDefinition> subtleEffects$getFluidDefinitionHeight() {
        return subtleEffects$fluidPairHeight;
    }
}
