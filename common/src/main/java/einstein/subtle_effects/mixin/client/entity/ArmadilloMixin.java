package einstein.subtle_effects.mixin.client.entity;

import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static einstein.subtle_effects.util.MathUtil.nextDouble;
import static einstein.subtle_effects.util.MathUtil.nextSign;

@IfModAbsent(value = CompatHelper.HARDENED_ARMADILLOS_MOD_ID)
@Mixin(Armadillo.class)
public class ArmadilloMixin {

    @Unique
    private final Armadillo subtleEffects$me = (Armadillo) (Object) this;

    @Inject(method = "brushOffScute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/armadillo/Armadillo;gameEvent(Lnet/minecraft/core/Holder;)V"))
    private void addBrushEffects(CallbackInfoReturnable<Boolean> cir) {
        Level level = subtleEffects$me.level();
        if (level.isClientSide() && ModConfigs.ITEMS.armadilloBrushParticles.get()) {
            RandomSource random = subtleEffects$me.getRandom();

            for (int i = 0; i < 10; i++) {
                int xSign = nextSign(random);
                int zSign = nextSign(random);

                level.addParticle(ModParticles.ARMADILLO.get(),
                        subtleEffects$me.getX() + (nextDouble(random, 0.3) * xSign),
                        subtleEffects$me.getRandomY() + 0.2,
                        subtleEffects$me.getZ() + (nextDouble(random, 0.3) * zSign),
                        nextDouble(random, 0.1) * xSign,
                        nextDouble(random, 0.2),
                        nextDouble(random, 0.1) * zSign
                );
            }
        }
    }
}
