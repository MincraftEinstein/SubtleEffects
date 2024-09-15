package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.FrustumGetter;
import einstein.subtle_effects.util.ParticleAccessor;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModConfigs.INSTANCE;
import static einstein.subtle_effects.util.MathUtil.nextSign;
import static net.minecraft.util.Mth.nextFloat;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin implements FrustumGetter {

    @Shadow
    @Nullable
    private ClientLevel level;

    @Shadow
    private Frustum cullingFrustum;

    @Inject(method = "levelEvent", at = @At("TAIL"))
    private void levelEvent(int type, BlockPos pos, int data, CallbackInfo ci) {
        if (level == null) {
            return;
        }
        RandomSource random = level.getRandom();
        BlockState state = level.getBlockState(pos);

        if (type == 1029) {
            if (INSTANCE.anvilBreakParticles.get()) {
                level.addDestroyBlockEffect(pos, Blocks.ANVIL.defaultBlockState());
            }
        }
        else if (type == 1030) {
            if (INSTANCE.anvilUseParticles.get()) {
                float pointX = random.nextFloat();
                float pointZ = random.nextFloat();

                for (int i = 0; i < 20; i++) {
                    int xSign = nextSign(random);
                    int zSign = nextSign(random);
                    level.addParticle(ModParticles.METAL_SPARK.get(),
                            pos.getX() + pointX,
                            pos.getY() + 1,
                            pos.getZ() + pointZ,
                            nextFloat(random, 0.1F, 0.2F) * xSign,
                            nextFloat(random, 0.1F, 0.2F),
                            nextFloat(random, 0.1F, 0.2F) * zSign
                    );
                }
            }
        }
        else if (type == 1042) {
            if (INSTANCE.grindstoneUseParticles.get()) {
                Direction direction = state.getValue(GrindstoneBlock.FACING);
                AttachFace face = state.getValue(GrindstoneBlock.FACE);
                Direction side = face == AttachFace.CEILING ? Direction.DOWN : Direction.UP;

                for (int i = 0; i < 20; i++) {
                    ParticleSpawnUtil.spawnParticlesOnSide(ModParticles.METAL_SPARK.get(), 0, side, level, pos, random,
                            nextFloat(random, 0.1F, 0.2F) * (direction.getStepX() * 1.5),
                            face == AttachFace.CEILING ? 0 : nextFloat(random, 0.1F, 0.2F),
                            nextFloat(random, 0.1F, 0.2F) * (direction.getStepZ() * 1.5)
                    );
                }
            }
        }
    }

    @Redirect(method = "levelEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;LARGE_SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceSmoke() {
        if (INSTANCE.lavaFizzSteam.get()) {
            return ModParticles.STEAM.get();
        }
        return ParticleTypes.LARGE_SMOKE;
    }

    @ModifyReturnValue(method = "addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value = "RETURN", ordinal = 0))
    private Particle spawnForcedParticle(Particle particle) {
        ((ParticleAccessor) particle).subtleEffects$force();
        return particle;
    }

    @Override
    public Frustum subtleEffects$getCullingFrustum() {
        return cullingFrustum;
    }
}
