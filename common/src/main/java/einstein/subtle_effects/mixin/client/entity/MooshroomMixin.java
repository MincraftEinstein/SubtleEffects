package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.networking.PayloadSender;
import einstein.subtle_effects.networking.clientbound.ClientBoundFeedMooshroomPayload;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.util.Util.template;

@Mixin(MushroomCow.class)
public class MooshroomMixin {

    @Unique
    private final MushroomCow subtleEffects$me = (MushroomCow) (Object) this;

    @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDDDDLnet/minecraft/network/protocol/game/ClientboundLevelParticlesPacket$RandomizationType;)I", ordinal = 0))
    private int spawnFeedingFailedParticles(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double xSpeed, double ySpeed, double zSpeed, ClientboundLevelParticlesPacket.RandomizationType randomizationType, Operation<Integer> original) {
        PayloadSender.sendToClientsTracking(null, level, subtleEffects$me.blockPosition(), new ClientBoundFeedMooshroomPayload(subtleEffects$me.getId(), List.of()), skippedPlayer ->
                original.call(level, particle, x, y, z, count, xDist, yDist, zDist, xSpeed, ySpeed, zSpeed, randomizationType));
        return -1;
    }

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;"))
    private void spawnFeedingParticles(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "itemStack") ItemStack heldStack) {
        Level level = subtleEffects$me.level();
        RandomSource random = subtleEffects$me.getRandom();

        if (ENTITIES.animalFeedingParticles && !heldStack.isEmpty()) {
            ItemParticleOption options = new ItemParticleOption(ParticleTypes.ITEM, template(heldStack));

            for (int i = 0; i < 16; i++) {
                ParticleSpawnUtil.spawnEntityFaceParticle(options, subtleEffects$me, level,
                        random, new Vec3(0, 0.3, -0.2), Util.getPartialTicks()
                );
            }
        }
    }

    @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDDDDLnet/minecraft/network/protocol/game/ClientboundLevelParticlesPacket$RandomizationType;)I", ordinal = 1))
    private int spawnEffectParticles(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double xSpeed, double ySpeed, double zSpeed, ClientboundLevelParticlesPacket.RandomizationType randomizationType, Operation<Integer> original, @Local(name = "effectsFromItemStack") Optional<SuspiciousStewEffects> effects) {
        PayloadSender.sendToClientsTracking(null, level, subtleEffects$me.blockPosition(), new ClientBoundFeedMooshroomPayload(subtleEffects$me.getId(), effects.orElseThrow().effects()), skippedPlayer ->
                original.call(level, particle, x, y, z, count, xDist, yDist, zDist, xSpeed, ySpeed, zSpeed, randomizationType));
        return -1;
    }
}
