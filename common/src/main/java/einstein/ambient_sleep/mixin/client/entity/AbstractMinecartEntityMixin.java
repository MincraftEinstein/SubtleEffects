package einstein.ambient_sleep.mixin.client.entity;

import einstein.ambient_sleep.util.ParticleManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartEntityMixin extends Entity {

    @Unique
    private final AbstractMinecart ambientSleep$me = (AbstractMinecart) (Object) this;

    public AbstractMinecartEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        ParticleManager.entityTick(ambientSleep$me, level(), random);
    }
}
