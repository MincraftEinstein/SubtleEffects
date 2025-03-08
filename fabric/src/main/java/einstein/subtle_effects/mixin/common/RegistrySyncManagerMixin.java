package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.SubtleEffects;
import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashSet;
import java.util.Set;

@Mixin(RegistrySyncManager.class)
public class RegistrySyncManagerMixin {

    @WrapOperation(method = "createAndPopulateRegistryMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;keySet()Ljava/util/Set;"))
    private static Set<ResourceLocation> disableParticleRegistrySyncing(Registry<? extends Registry<?>> registry, Operation<Set<ResourceLocation>> original) {
        Set<ResourceLocation> registryIds = new HashSet<>(original.call(registry));
        if (SubtleEffects.SERVER_CONFIGS.disableRegistrySyncing) {
            subtleEffects$removeRegistry(registryIds, Registries.PARTICLE_TYPE);
            subtleEffects$removeRegistry(registryIds, Registries.SOUND_EVENT);
        }
        return Set.copyOf(registryIds);
    }

    @Unique
    private static void subtleEffects$removeRegistry(Set<ResourceLocation> registryIds, ResourceKey<?> key) {
        ResourceLocation registryId = key.location();
        if (!registryIds.remove(registryId)) {
            SubtleEffects.LOGGER.error("Failed to disable registry syncing for registry {} because it couldn't be found", registryId);
        }
    }
}
