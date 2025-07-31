package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;

@SuppressWarnings("deprecation")
@Mixin(DripParticle.class)
public abstract class DripParticleMixin extends TextureSheetParticle {

    @Shadow
    protected boolean isGlowing;

    @Shadow
    @Final
    private Fluid type;

    @Unique
    private boolean subtleEffects$dripIntoFluidEffectsPlayed;

    @Unique
    private boolean subtleEffects$isLava;

    protected DripParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ClientLevel level, double x, double y, double z, Fluid fluid, CallbackInfo ci) {
        subtleEffects$isLava = type.is(FluidTags.LAVA);

        if (GENERAL.glowingLavaDrops && subtleEffects$isLava) {
            isGlowing = true;
        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/material/Fluids;EMPTY:Lnet/minecraft/world/level/material/Fluid;"))
    private void tick(CallbackInfo ci) {
        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(pos);
        FluidState fluidState = level.getFluidState(pos);
        double fluidHeight = Math.max(Util.getCauldronFillHeight(state), fluidState.getHeight(level, pos));
        double fluidSurface = pos.getY() + fluidHeight;

        if (fluidHeight > 0 && y <= fluidSurface) {
            Fluid fluid = fluidState.isEmpty() ? Util.getCauldronFluid(state) : fluidState.getType();

            if (GENERAL.fluidDropsEvaporate) {
                if ((fluid.is(FluidTags.LAVA) && !subtleEffects$isLava) || (fluid.is(FluidTags.WATER) && subtleEffects$isLava)) {
                    level.addParticle(ModParticles.STEAM.get(), x, fluidSurface, z, 0, 0, 0);

                    if (GENERAL.fluidDropsEvaporationVolume.get() > 0) {
                        level.playLocalSound(x, y, z,
                                SoundEvents.LAVA_EXTINGUISH,
                                SoundSource.BLOCKS,
                                Mth.randomBetween(random, 0.3F, 1) * GENERAL.fluidDropsEvaporationVolume.get(),
                                (2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F),
                                false
                        );
                    }

                    remove();
                    return;
                }
            }

            if (!subtleEffects$dripIntoFluidEffectsPlayed) {
                if (GENERAL.dropLandSoundVolume.get() > 0) {
                    Supplier<SoundEvent> sound = subtleEffects$isLava ? ModSounds.DRIP_LAVA_INTO_FLUID : ModSounds.DRIP_WATER_INTO_FLUID;

                    level.playLocalSound(x, y, z,
                            sound.get(),
                            SoundSource.BLOCKS,
                            Mth.randomBetween(random, 0.3F, 1) * GENERAL.dropLandSoundVolume.get(),
                            1,
                            false
                    );
                }

                if (GENERAL.dropLandInFluidSplashes) {
                    ParticleOptions splashParticle = subtleEffects$isLava ? ModParticles.LAVA_SPLASH.get() : ParticleTypes.SPLASH;
                    level.addParticle(splashParticle, x, fluidSurface, z, 0, 0, 0);
                }
                subtleEffects$dripIntoFluidEffectsPlayed = true;
            }
        }
    }
}
