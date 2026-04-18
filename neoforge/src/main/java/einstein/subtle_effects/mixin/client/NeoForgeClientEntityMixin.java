package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import einstein.subtle_effects.data.FluidDefinition;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.FluidDefinitionAccessor;
import einstein.subtle_effects.util.FluidLogicAccessor;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(Entity.class)
public abstract class NeoForgeClientEntityMixin implements FluidLogicAccessor {

    @Shadow
    protected Object2DoubleMap<FluidType> forgeFluidTypeHeight;

    @Unique
    private final Object2DoubleMap<FluidDefinition> subtleEffects$fluidDefHeight = new Object2DoubleArrayMap<>();

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @ModifyExpressionValue(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/block/SoundType;"))
    private SoundType stepSound(SoundType soundType) {
        if (subtleEffects$me instanceof SnowGolem && ModConfigs.ENTITIES.snowGolemStepSounds) {
            return SoundType.SNOW;
        }
        return soundType;
    }

    @Inject(method = "updateFluidHeightAndDoFluidPushing()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getHeight(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
    private void storeFluidPairHeight(CallbackInfo ci, @Local FluidState fluidState, @Share("fluidDefs") LocalRef<Map<FluidType, FluidDefinition>> fluidDefsRef) {
        FluidDefinition fluidDefinition = ((FluidDefinitionAccessor) fluidState.getType()).subtleEffects$getFluidDefinition();
        FluidType type = fluidState.getFluidType();
        Map<FluidType, FluidDefinition> fluidDefs = fluidDefsRef.get();
        if (fluidDefs == null) {
            fluidDefs = new HashMap<>();
        }

        if (!fluidDefs.containsKey(type) && fluidDefinition != null) {
            fluidDefs.put(type, fluidDefinition);
        }

        fluidDefsRef.set(fluidDefs);
    }

    @Inject(method = "updateFluidHeightAndDoFluidPushing()V", at = @At("TAIL"))
    private void updateFluidPairHeight(CallbackInfo ci, @Share("fluidDefs") LocalRef<Map<FluidType, FluidDefinition>> fluidDefsRef) {
        for (Object2DoubleMap.Entry<FluidType> entry : forgeFluidTypeHeight.object2DoubleEntrySet()) {
            Map<FluidType, FluidDefinition> fluidDefs = fluidDefsRef.get();
            if (fluidDefs != null) {
                FluidDefinition fluidDefinition = fluidDefs.get(entry.getKey());
                if (fluidDefinition != null) {
                    subtleEffects$fluidDefHeight.put(fluidDefinition, entry.getDoubleValue());
                }
            }
        }
    }

    @Override
    public Object2DoubleMap<FluidDefinition> subtleEffects$getFluidDefinitionHeight() {
        return subtleEffects$fluidDefHeight;
    }
}
