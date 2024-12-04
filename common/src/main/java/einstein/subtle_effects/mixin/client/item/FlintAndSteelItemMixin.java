package einstein.subtle_effects.mixin.client.item;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelItemMixin {

    @Inject(method = "useOn", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/gameevent/GameEvent;BLOCK_CHANGE:Lnet/minecraft/world/level/gameevent/GameEvent;"))
    private void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            if (!ModConfigs.ITEMS.flintAndSteelParticles) {
                return;
            }

            RandomSource random = level.getRandom();
            BlockPos pos = context.getClickedPos();
            Vec3 location = context.getClickLocation();

            for (int i = 0; i < 3; i++) {
                level.addParticle(ParticleTypes.FLAME,
                        location.x() + MathUtil.nextNonAbsDouble(random, 0.07),
                        location.y() + MathUtil.nextDouble(random, 0.07),
                        location.z() + MathUtil.nextNonAbsDouble(random, 0.07),
                        0, 0, 0
                );
            }

            for (int i = 0; i < 10; i++) {
                level.addParticle(ModParticles.SHORT_SPARK.get(),
                        location.x() + MathUtil.nextNonAbsDouble(random, 0.07),
                        location.y(),
                        location.z() + MathUtil.nextNonAbsDouble(random, 0.07),
                        0, 0, 0
                );
            }
        }
    }
}
