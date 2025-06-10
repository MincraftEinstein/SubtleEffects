package einstein.subtle_effects.mixin.common;

import einstein.subtle_effects.networking.clientbound.ClientBoundVillagerWorkPacket;
import einstein.subtle_effects.platform.Services;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.WorkAtPoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(WorkAtPoi.class)
public class WorkAtPoiMixin {

    @Inject(method = "start(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/Villager;J)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;playWorkSound()V"))
    private void spawnWorkEffects(ServerLevel level, Villager villager, long gameTime, CallbackInfo ci) {
        Optional<GlobalPos> memory = villager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        memory.ifPresent(globalPos -> Services.NETWORK.sendToClientsTracking(level, villager.blockPosition(),
                new ClientBoundVillagerWorkPacket(villager.getId(), globalPos.pos())
        ));
    }
}
