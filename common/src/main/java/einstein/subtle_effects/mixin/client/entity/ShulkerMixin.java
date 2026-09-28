package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Shulker.class)
public class ShulkerMixin extends AbstractGolem {

    @Shadow
    @Nullable
    private BlockPos clientOldAttachPosition;

    protected ShulkerMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "setPos", at = @At("TAIL"))
    private void setPos(double x, double y, double z, CallbackInfo ci) {
        if (clientOldAttachPosition != null && ModConfigs.ENTITIES.shulkerTeleportParticles) {
            for (int j = 0; j < 32; ++j) {
                BlockPos pos = blockPosition();
                float xSpeed = (random.nextFloat() - 0.5F) * 0.2F;
                float ySpeed = (random.nextFloat() - 0.5F) * 0.2F;
                float zSpeed = (random.nextFloat() - 0.5F) * 0.2F;
                double d = random.nextDouble();
                double particleX = Mth.lerp(d, clientOldAttachPosition.getX(), pos.getX()) + (random.nextDouble() - 0.5) + 0.5;
                double particleY = Mth.lerp(d, clientOldAttachPosition.getY(), pos.getY()) + random.nextDouble() - 0.5;
                double particleZ = Mth.lerp(d, clientOldAttachPosition.getZ(), pos.getZ()) + (random.nextDouble() - 0.5) + 0.5;

                level().addParticle(ParticleTypes.PORTAL, particleX, particleY, particleZ, xSpeed, ySpeed, zSpeed);
            }
        }
    }
}
