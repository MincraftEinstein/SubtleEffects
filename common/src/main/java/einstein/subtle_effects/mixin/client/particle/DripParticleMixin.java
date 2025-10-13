package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.util.DripParticleAccessor;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
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
public abstract class DripParticleMixin extends SingleQuadParticle implements DripParticleAccessor {

    @Shadow
    protected boolean isGlowing;

    @Shadow
    @Final
    private Fluid type;

    @Unique
    private boolean subtleEffects$dripIntoFluidEffectsPlayed;

    @Unique
    private boolean subtleEffects$isLava;

    @Unique
    private boolean subtleEffects$isSilent = false;

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
        double fluidSurface = pos.getY() + fluidHeight + 0.01;

        if (fluidHeight > 0 && y <= fluidSurface) {
            Fluid fluid = fluidState.isEmpty() ? Util.getCauldronFluid(state) : fluidState.getType();

            if (GENERAL.fluidDropsEvaporate) {
                if ((fluid.is(FluidTags.LAVA) && !subtleEffects$isLava) || (fluid.is(FluidTags.WATER) && subtleEffects$isLava)) {
                    level.addParticle(ModParticles.STEAM.get(), x, fluidSurface, z, 0, 0, 0);

                    if (GENERAL.fluidDropsEvaporationVolume.get() > 0 && !subtleEffects$isSilent) {
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
                if (GENERAL.dropLandSoundVolume.get() > 0 && !subtleEffects$isSilent) {
                    Supplier<SoundEvent> sound = subtleEffects$isLava ? ModSounds.DRIP_LAVA_INTO_FLUID : ModSounds.DRIP_WATER_INTO_FLUID;

                    level.playLocalSound(x, y, z,
                            sound.get(),
                            SoundSource.BLOCKS,
                            Mth.randomBetween(random, 0.3F, 1) * GENERAL.dropLandSoundVolume.get(),
                            1,
                            false
                    );
                }

                if (GENERAL.dropLandInFluidRipples) {
                    level.addParticle(new FloatParticleOptions(subtleEffects$isLava ? ModParticles.LAVA_RIPPLE.get() : ModParticles.WATER_RIPPLE.get(), Math.max(quadSize, Math.max(bbWidth, bbHeight)) + 0.3F * 3),
                            x + MathUtil.nextNonAbsDouble(random, 0.07),
                            fluidSurface,
                            z + MathUtil.nextNonAbsDouble(random, 0.07),
                            0, 0, 0
                    );
                }
                subtleEffects$dripIntoFluidEffectsPlayed = true;
            }
        }
    }

    @Override
    public void subtleEffects$setSilent() {
        subtleEffects$isSilent = true;
    }

    @Override
    public boolean subtleEffects$isSilent() {
        return subtleEffects$isSilent;
    }
}
