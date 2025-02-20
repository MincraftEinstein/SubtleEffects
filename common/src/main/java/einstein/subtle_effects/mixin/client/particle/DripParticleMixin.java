package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.TextureSheetParticle;
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

@SuppressWarnings("deprecation")
@Mixin(DripParticle.class)
public abstract class DripParticleMixin extends TextureSheetParticle {

    @Shadow
    protected boolean isGlowing;

    @Shadow
    @Final
    private Fluid type;

    @Unique
    private boolean subtleEffects$dripSoundPlayed;

    protected DripParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ClientLevel level, double x, double y, double z, Fluid fluid, CallbackInfo ci) {
        if (ModConfigs.GENERAL.glowingLavaDrops && fluid.is(FluidTags.LAVA)) {
            isGlowing = true;
        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/material/Fluids;EMPTY:Lnet/minecraft/world/level/material/Fluid;"))
    private void tick(CallbackInfo ci) {
        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(pos);
        FluidState fluidState = level.getFluidState(pos);
        double fluidHeight = Math.max(Util.getCauldronFillHeight(state), fluidState.getHeight(level, pos));

        if (fluidHeight > 0 && y <= pos.getY() + fluidHeight) {
            Fluid fluid = fluidState.isEmpty() ? Util.getCauldronFluid(state) : fluidState.getType();
            boolean isLava = type.is(FluidTags.LAVA);

            if (ModConfigs.GENERAL.fluidDropsEvaporate) {
                if ((fluid.is(FluidTags.LAVA) && !isLava) || (fluid.is(FluidTags.WATER) && isLava)) {
                    level.addParticle(ModParticles.STEAM.get(), x, y + 0.1, z, 0, 0, 0);

                    if (ModConfigs.GENERAL.fluidDropsEvaporationVolume.get() > 0) {
                        level.playLocalSound(x, y, z,
                                SoundEvents.LAVA_EXTINGUISH,
                                SoundSource.BLOCKS,
                                ModConfigs.GENERAL.fluidDropsEvaporationVolume.get(),
                                (2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F),
                                false
                        );
                    }

                    remove();
                    return;
                }
            }

            if (ModConfigs.GENERAL.dropLandSounds && !subtleEffects$dripSoundPlayed) {
                Supplier<SoundEvent> sound = isLava ? ModSounds.DRIP_LAVA_INTO_FLUID : ModSounds.DRIP_WATER_INTO_FLUID;
                level.playLocalSound(x, y, z,
                        sound.get(),
                        SoundSource.BLOCKS,
                        Mth.randomBetween(random, 0.3F, 1),
                        1,
                        false
                );
                subtleEffects$dripSoundPlayed = true;
            }
        }
    }
}
