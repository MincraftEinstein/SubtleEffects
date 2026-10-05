package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.FluidLogicAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class NeoForgeClientEntityMixin implements FluidLogicAccessor {

    @Shadow
    @Final
    private EntityFluidInteraction fluidInteraction;
    @Shadow
    protected boolean firstTick;
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

    @Inject(method = "updateFluidInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityFluidInteraction;isInFluidMatching(Lnet/minecraft/world/entity/Entity;Lnet/neoforged/neoforge/fluids/InFluidPredicate;)Z", ordinal = 0))
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
        return subtleEffects$fluidDefHeight;
    }
}
