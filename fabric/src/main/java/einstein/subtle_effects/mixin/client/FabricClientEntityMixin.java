package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.util.EntityMixinAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class FabricClientEntityMixin {

    @Shadow
    protected boolean firstTick;

    @WrapOperation(method = "updateInWaterStateAndDoFluidPushing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateFluidHeightAndDoFluidPushing(Lnet/minecraft/tags/TagKey;D)Z"))
    private boolean doLavaSplash(Entity entity, TagKey<Fluid> fluidTag, double motionScale, Operation<Boolean> original) {
        boolean result = original.call(entity, fluidTag, motionScale);
        boolean isInLava = result && fluidTag == FluidTags.LAVA; // Just in case someone decides to do their own fluid pushing here
        EntityMixinAccessor accessor = (EntityMixinAccessor) entity;
        ParticleSpawnUtil.spawnLavaSplash(entity, isInLava, firstTick && !(entity instanceof PrimedTnt), accessor.subtleEffects$wasTouchingLava());
        accessor.subtleEffects$setTouchingLava(isInLava);
        return result;
    }
}
