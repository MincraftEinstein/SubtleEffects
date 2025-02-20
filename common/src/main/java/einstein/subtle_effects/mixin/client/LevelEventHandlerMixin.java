package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.SparkType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.util.MathUtil.nextSign;
import static net.minecraft.util.Mth.nextFloat;

@Mixin(LevelEventHandler.class)
public class LevelEventHandlerMixin {

    @Shadow
    @Final
    private Level level;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "levelEvent", at = @At("TAIL"))
    private void levelEvent(int type, BlockPos pos, int data, CallbackInfo ci) {
        if (level == null) {
            return;
        }

        RandomSource random = level.getRandom();
        BlockState state = level.getBlockState(pos);
        Player player = minecraft.player;

        switch (type) {
            case LevelEvent.SOUND_ANVIL_BROKEN: {
                if (BLOCKS.anvilBreakParticles) {
                    level.addDestroyBlockEffect(pos, Blocks.ANVIL.defaultBlockState());
                }
                break;
            }
            case LevelEvent.SOUND_ANVIL_USED: {
                if (BLOCKS.anvilUseParticles) {
                    float pointX = random.nextFloat();
                    float pointZ = random.nextFloat();

                    for (int i = 0; i < 20; i++) {
                        int xSign = nextSign(random);
                        int zSign = nextSign(random);
                        level.addParticle(SparkParticle.create(SparkType.METAL, random),
                                pos.getX() + pointX,
                                pos.getY() + 1,
                                pos.getZ() + pointZ,
                                nextFloat(random, 0.1F, 0.2F) * xSign,
                                nextFloat(random, 0.1F, 0.2F),
                                nextFloat(random, 0.1F, 0.2F) * zSign
                        );
                    }
                }
                break;
            }
            case LevelEvent.SOUND_GRINDSTONE_USED: {
                if (BLOCKS.grindstoneUseParticles) {
                    Direction direction = state.getValue(GrindstoneBlock.FACING);
                    AttachFace face = state.getValue(GrindstoneBlock.FACE);
                    Direction side = face == AttachFace.CEILING ? Direction.DOWN : Direction.UP;

                    for (int i = 0; i < 20; i++) {
                        ParticleSpawnUtil.spawnParticlesOnSide(SparkParticle.create(SparkType.METAL, random), 0, side, level, pos, random,
                                nextFloat(random, 0.1F, 0.2F) * (direction.getStepX() * 1.5),
                                face == AttachFace.CEILING ? 0 : nextFloat(random, 0.1F, 0.2F),
                                nextFloat(random, 0.1F, 0.2F) * (direction.getStepZ() * 1.5)
                        );
                    }
                }
                break;
            }
        }
    }

    @WrapOperation(method = "levelEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;LARGE_SMOKE:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceSmoke(Operation<SimpleParticleType> original) {
        if (BLOCKS.steam.lavaFizzSteam) {
            return ModParticles.STEAM.get();
        }
        return original.call();
    }

    @WrapOperation(method = "levelEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;CLOUD:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType replaceCloud(Operation<SimpleParticleType> original) {
        if (BLOCKS.steam.spongeDryingOutSteam) {
            return ModParticles.STEAM.get();
        }
        return original.call();
    }
}
