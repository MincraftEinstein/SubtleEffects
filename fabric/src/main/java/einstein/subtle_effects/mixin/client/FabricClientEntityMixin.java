package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.FluidLogicAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class FabricClientEntityMixin implements FluidLogicAccessor {

    @Shadow
    protected boolean firstTick;
    @Shadow
    @Final
    private EntityFluidInteraction fluidInteraction;
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

    @Inject(method = "updateFluidInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityFluidInteraction;isInFluid(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
    private void preformWaterSplash(CallbackInfoReturnable<Boolean> cir) {
        fluidInteraction.currentAccumulators.forEach((tag, _) -> {
            if (fluidInteraction.isInFluid(tag)) {
                subtleEffects$setLastTouchedFluid(ParticleSpawnUtil.preformSplash(true, false, subtleEffects$me, firstTick, isWater -> {
                    if (isWater) {
                        subtleEffects$cancelNextWaterSplash();
                    }
                }));
            }
        });
    }


    @Override
    public Object2DoubleMap<FluidDefinition> subtleEffects$getFluidDefinitionHeight() {
        return subtleEffects$fluidPairHeight;
    }
}
