package einstein.subtle_effects.mixin.client.particle;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.FallenLeafParticleOptions;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CherryParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModConfigs.GENERAL;

@Mixin(CherryParticle.class)
public abstract class CherryParticleMixin extends TextureSheetParticle {

    @Unique
    private final BlockPos.MutableBlockPos subtleEffects$pos = new BlockPos.MutableBlockPos();

    protected CherryParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        if (!GENERAL.leavesLandOnGround && !GENERAL.leavesLandOnWater) {
            return;
        }

        subtleEffects$pos.set(x, y, z);

        BlockState state = level.getBlockState(subtleEffects$pos);
        FluidState fluidState = level.getFluidState(subtleEffects$pos);
        boolean isWaterCauldron = state.is(Blocks.WATER_CAULDRON);
        double fluidHeight = GENERAL.leavesLandOnWater ?
                (fluidState.is(Fluids.WATER) ? fluidState.getHeight(level, subtleEffects$pos) :
                        (isWaterCauldron ? Util.getCauldronFillHeight(state) : 0)) : 0;
        double surface = subtleEffects$pos.getY() + Math.max(fluidHeight, isWaterCauldron ? 0 : state.getCollisionShape(level, subtleEffects$pos).max(Direction.Axis.Y)) + 0.01;

        if ((fluidHeight > 0 && y <= surface && GENERAL.leavesLandOnWater) || (onGround && !state.is(Blocks.WATER) && GENERAL.leavesLandOnGround)) {
            level.addParticle(
                    new FallenLeafParticleOptions(sprite, quadSize, bbWidth, bbHeight, onGround, new Vector3f(rCol, gCol, bCol), alpha, roll),
                    x, surface + Mth.nextFloat(random, 0.001F, 0.005F), z,
                    xd, 0, zd
            );

            if (!onGround && GENERAL.leavesLandingOnWaterRipples) {
                double halfSize = quadSize / 2;

                level.addParticle(new FloatParticleOptions(ModParticles.WATER_RIPPLE.get(), Math.max(quadSize, Math.max(bbWidth, bbHeight)) + 0.3F * 3),
                        x + halfSize, surface, z + halfSize,
                        0, 0, 0
                );
            }

            remove();
        }
    }
}
