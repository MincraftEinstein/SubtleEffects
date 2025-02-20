package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class ClientEntityMixin {

    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Inject(method = "playEntityOnFireExtinguishedSound", at = @At("TAIL"))
    private void addExtinguishParticles(CallbackInfo ci) {
        Level level = subtleEffects$me.level();

        if (level.isClientSide() && ModConfigs.ENTITIES.burning.extinguishSteam) {
            // More particles than this will be displayed, since this method is called multiple times at once
            for (int i = 0; i < 5; i++) {
                boolean success = false;

                while (!success) {
                    double randomX = subtleEffects$me.getRandomX(1);
                    double randomY = subtleEffects$me.getRandomY();
                    double randomZ = subtleEffects$me.getRandomZ(1);

                    if (level.getFluidState(BlockPos.containing(randomX, randomY, randomZ)).isEmpty()) {
                        level.addParticle(ModParticles.STEAM.get(),
                                randomX,
                                randomY,
                                randomZ,
                                0, 0, 0
                        );
                        success = true;
                    }
                }
            }
        }
    }
}
