package einstein.subtle_effects.mixin.client.block;

import com.google.common.base.Predicates;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {

    @Inject(method = "animateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos;"), cancellable = true)
    private void endCrystalCancelEffects(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (ModConfigs.ENTITIES.endCrystalsDisableFireEffects) {
            List<EndCrystal> endCrystals = new ArrayList<>();
            level.getEntities(EntityType.END_CRYSTAL, new AABB(pos), Predicates.alwaysTrue(), endCrystals);

            if (!endCrystals.isEmpty()) {
                ci.cancel();
            }
        }
    }

    @WrapOperation(method = "animateTick", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;LARGE_SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceSmoke(Operation<SimpleParticleType> original) {
        if (ModConfigs.BLOCKS.updatedSmoke.fireSmoke) {
            return ModParticles.SMOKE.get();
        }
        return original.call();
    }
}
