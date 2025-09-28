package einstein.subtle_effects.mixin.client.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static einstein.subtle_effects.compat.CompatHelper.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.BITES;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateBaseMixin {

    @WrapOperation(method = "useWithoutItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;useWithoutItem(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"))
    private InteractionResult use(Block block, BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, Operation<InteractionResult> original) {
        if (level.isClientSide && (ModConfigs.BLOCKS.cakeEatParticles || ModConfigs.BLOCKS.cakeEatSounds)) {
            Pair<IntegerProperty, Integer> bitesData = subtleEffects$getBitesData(state);
            boolean hadBites = bitesData != null;
            int oldBites = hadBites ? state.getValue(bitesData.getFirst()) : 0;
            InteractionResult result = original.call(block, state, level, pos, player, hitResult);

            if (result == InteractionResult.SUCCESS) {
                BlockState newState = level.getBlockState(pos);
                int maxBites = 0;
                int bites = 0;

                if (hadBites) {
                    maxBites = bitesData.getSecond();
                    if (newState.hasProperty(bitesData.getFirst())) {
                        bites = newState.getValue(bitesData.getFirst());
                    }
                    else {
                        bites = oldBites + 1;
                    }
                }
                else {
                    bitesData = subtleEffects$getBitesData(newState);
                    if (bitesData != null) {
                        bites = newState.getValue(bitesData.getFirst());
                        maxBites = bitesData.getSecond();
                    }
                }

                if (bites > oldBites) {
                    ItemStack stack = block.getCloneItemStack(level, pos, state);
                    RandomSource random = player.getRandom();

                    if (!stack.isEmpty() && ModConfigs.BLOCKS.cakeEatParticles) {
                        float partialTick = Util.getPartialTicks();
                        ItemParticleOption options = new ItemParticleOption(ParticleTypes.ITEM, stack);

                        for (int i = 0; i < 16; i++) {
                            ParticleSpawnUtil.spawnEntityFaceParticle(options, player, level, random, Vec3.ZERO, partialTick);
                        }
                    }

                    if (ModConfigs.BLOCKS.cakeEatSounds) {
                        SoundSource soundSource = player.getSoundSource();
                        if (bites == maxBites) {
                            Util.playClientSound(player,
                                    SoundEvents.PLAYER_BURP,
                                    soundSource,
                                    0.5F,
                                    level.random.nextFloat() * 0.1F + 0.9F
                            );
                        }

                        if (!bitesData.getFirst().equals(FD_PIE_BITES)) { // FD already plays its own sound
                            Util.playClientSound(player,
                                    player.getEatingSound(stack),
                                    soundSource,
                                    0.5F + 0.5F * random.nextInt(2),
                                    (random.nextFloat() - random.nextFloat()) * 0.2F + 1
                            );
                        }
                    }
                }
            }
            return result;
        }
        return original.call(block, state, level, pos, player, hitResult);
    }

    @Unique
    @Nullable
    private static Pair<IntegerProperty, Integer> subtleEffects$getBitesData(BlockState state) {
        if (state.hasProperty(BITES)) {
            return Pair.of(BITES, 7);
        }

        IntegerProperty pieProperty = subtleEffects$findProperty(state, FD_PIE_BITES);
        if (pieProperty != null) {
            return Pair.of(pieProperty, 4);
        }

        IntegerProperty twoTieredCakeProperty = subtleEffects$findProperty(state, JMC_TWO_TIERED_CAKE_BITES);
        if (twoTieredCakeProperty != null) {
            return Pair.of(twoTieredCakeProperty, 11);
        }

        IntegerProperty threeTieredCakeProperty = subtleEffects$findProperty(state, JMC_THREE_TIERED_CAKE_BITES);
        if (threeTieredCakeProperty != null) {
            return Pair.of(threeTieredCakeProperty, 16);
        }
        return null;
    }

    // Hacky way to get access to properties without the actual property constant
    // .getValue()/.hasValue() doesn't work because it checks == instead of .equals()
    @Unique
    private static IntegerProperty subtleEffects$findProperty(BlockState state, IntegerProperty property1) {
        return (IntegerProperty) state.getProperties().stream().filter(property2 ->
                property2.equals(property1)).findFirst().orElse(null);
    }
}