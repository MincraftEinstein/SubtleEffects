package einstein.ambient_sleep.mixin.client.block;

import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @Shadow
    @Final
    private boolean spawnParticles;

    @Inject(method = "animateTick", at = @At("TAIL"))
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (ModConfigs.INSTANCE.campfireSparks.get() && state.getValue(CampfireBlock.LIT)) {
            ParticleSpawnUtil.spawnSparks(level, random, pos, new Vec3(0.5, 0.4, 0.5), new Vec3i(3, 5, 3), 10, 6, state.is(Blocks.SOUL_CAMPFIRE), true);
        }
    }

    @Redirect(method = "animateTick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/CampfireBlock;spawnParticles:Z"))
    private boolean shouldDisableLavaSparks(CampfireBlock block) {
        return spawnParticles && !ModConfigs.INSTANCE.removeVanillaCampfireSparks.get();
    }
}
