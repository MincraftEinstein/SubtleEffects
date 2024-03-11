package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.util.ParticleSpawnUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static einstein.ambient_sleep.init.ModConfigs.INSTANCE;
import static einstein.ambient_sleep.util.MathUtil.nextFloat;
import static einstein.ambient_sleep.util.MathUtil.nextSign;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Nullable
    private ClientLevel level;

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
                    int xSign = nextSign();
                    int zSign = nextSign();
                    level.addParticle(ModParticles.METAL_SPARK.get(),
                            pos.getX() + pointX,
                            pos.getY() + 1,
                            pos.getZ() + pointZ,
                            nextFloat(10, 20) * xSign,
                            nextFloat(10, 20),
                            nextFloat(10, 20) * zSign
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
                            nextFloat(10, 20) * (direction.getStepX() * 1.5),
                            face == AttachFace.CEILING ? 0 : nextFloat(10, 20),
                            nextFloat(10, 20) * (direction.getStepZ() * 1.5)
                    );
                }
            }
        }
    }
}
