package einstein.subtle_effects.mixin.client.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.configs.ReplacedParticlesDisplayType;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.init.ModConfigs.ITEMS;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @WrapOperation(method = "evaluateNewBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void evaluateNewBlockState(Level level, Entity entity, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        if (level.isClientSide) {
            if (sound.equals(SoundEvents.AXE_STRIP)) {
                if (ITEMS.axeStripParticles) {
                    level.addDestroyBlockEffect(pos, state);
                }
            }
            else if (sound.equals(SoundEvents.AXE_SCRAPE)) {
                if (ITEMS.axeScrapeParticlesDisplayType != ReplacedParticlesDisplayType.VANILLA) {
                    subtleEffects$spawnCopperParticles(level, pos, state, state);
                }
            }
            else if (sound.equals(SoundEvents.AXE_WAX_OFF)) {
                if (ITEMS.axeWaxOffParticlesDisplayType != ReplacedParticlesDisplayType.VANILLA) {
                    subtleEffects$spawnCopperParticles(level, pos, state, Blocks.HONEY_BLOCK.defaultBlockState());
                }
            }
        }

        original.call(level, entity, pos, sound, source, volume, pitch);
    }

    @Unique
    private static void subtleEffects$spawnCopperParticles(Level level, BlockPos pos, BlockState state, BlockState particleState) {
        ParticleSpawnUtil.spawnParticlesAroundShape(
                new BlockParticleOption(ModParticles.BLOCK_NO_MOMENTUM.get(), particleState),
                level, pos, state, 10,
                () -> Vec3.ZERO, 0.0625F
        );
    }
}
