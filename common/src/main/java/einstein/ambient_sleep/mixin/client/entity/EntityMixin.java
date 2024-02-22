package einstein.ambient_sleep.mixin.client.entity;

import einstein.ambient_sleep.util.ParticleManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract Level level();

    @Shadow
    @Final
    protected RandomSource random;

    @Unique
    private final Entity ambientSleep$me = (Entity) (Object) this;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        ParticleManager.entityTick(ambientSleep$me, level(), random);
    }
}
