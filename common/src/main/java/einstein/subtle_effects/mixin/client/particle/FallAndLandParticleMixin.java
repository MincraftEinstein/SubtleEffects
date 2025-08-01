package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModSounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(targets = "net.minecraft.client.particle.DripParticle.FallAndLandParticle")
public abstract class FallAndLandParticleMixin extends Particle {

    @Unique
    private final Fluid subtleEffects$fluid = ((DripParticle) (Object) this).getType();

    protected FallAndLandParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "postMoveUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/DripParticle$FallAndLandParticle;remove()V"))
    private void onRemove(CallbackInfo ci) {
        if (ModConfigs.GENERAL.dropLandSoundVolume.get() > 0) {
            Supplier<SoundEvent> sound = subtleEffects$fluid.is(FluidTags.LAVA) ? ModSounds.DRIP_LAVA : ModSounds.DRIP_WATER;
            level.playLocalSound(x, y, z,
                    sound.get(),
                    SoundSource.BLOCKS,
                    ModConfigs.GENERAL.dropLandSoundVolume.get() * Mth.randomBetween(random, 0.3F, 1),
                    1,
                    false
            );
        }
    }
}
