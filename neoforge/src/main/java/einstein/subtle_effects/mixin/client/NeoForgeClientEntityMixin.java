package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.util.CommonUtil;
import einstein.subtle_effects.util.EntityAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class NeoForgeClientEntityMixin {

    @Shadow
    protected boolean firstTick;

    @Unique
    private final Entity subtleEffectsNeoForge$me = (Entity) (Object) this;

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("TAIL"))
    private void updateInWaterStateAndDoFluidPushing(CallbackInfoReturnable<Boolean> cir) {
        boolean isInLava = CommonUtil.isEntityInFluid(subtleEffectsNeoForge$me, FluidTags.LAVA);
        EntityAccessor accessor = (EntityAccessor) subtleEffectsNeoForge$me;
        ParticleSpawnUtil.spawnLavaSplash(subtleEffectsNeoForge$me, isInLava, firstTick, accessor.subtleEffects$wasTouchingLava(), subtleEffectsNeoForge$me.getDeltaMovement());
        accessor.subtleEffects$setTouchingLava(isInLava);
    }
}
