package einstein.ambient_sleep.mixin.client.entity;

import commonnetwork.api.Dispatcher;
import einstein.ambient_sleep.networking.clientbound.ClientBoundEntitySpawnSprintingDustCloudsPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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

    @Shadow
    public abstract Vec3 position();

    @Unique
    private boolean ambientSleep$canStart = true;

    @Unique
    private Vec3 ambientSleep$lastCheckedPos;

    @Unique
    private final Entity ambientSleep$me = (Entity) (Object) this;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!level().isClientSide) {
            if (ambientSleep$canStart || position().distanceToSqr(ambientSleep$lastCheckedPos) > 0.5) {
                if (ambientSleep$me instanceof Ravager ravager) {
                    if (ravager.getSpeed() > 0.34F && ravager.onGround()) {
                        Dispatcher.sendToAllClients(new ClientBoundEntitySpawnSprintingDustCloudsPacket(ravager.getId()), level().getServer());
                        ambientSleep$lastCheckedPos = position();
                        ambientSleep$canStart = false;
                        return;
                    }
                    ambientSleep$lastCheckedPos = Vec3.ZERO;
                    ambientSleep$canStart = true;
                }
                else if (ambientSleep$me instanceof Goat goat) {
                    if (goat.getSpeed() > 0.4F && goat.onGround()) {
                        Dispatcher.sendToAllClients(new ClientBoundEntitySpawnSprintingDustCloudsPacket(goat.getId()), level().getServer());
                        ambientSleep$lastCheckedPos = position();
                        ambientSleep$canStart = false;
                        return;
                    }
                    ambientSleep$lastCheckedPos = Vec3.ZERO;
                    ambientSleep$canStart = true;
                }
            }
        }
    }
}
