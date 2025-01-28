package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeartParticle.class)
public abstract class HeartParticleMixin extends TextureSheetParticle {

    protected HeartParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ClientLevel level, double x, double y, double z, CallbackInfo ci) {
        lifetime = random.nextIntBetweenInclusive(16, 25);
    }

    @Override
    public void tick() {
        super.tick();

        if (ModConfigs.GENERAL.poppingHearts && !isAlive()) {
            level.addParticle(ModParticles.HEART_POP.get(), x, y, z, 0, yd, 0);
        }
    }
}
