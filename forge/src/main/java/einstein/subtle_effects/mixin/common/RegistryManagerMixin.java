package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(RegistryManager.class)
public class RegistryManagerMixin {

    @ModifyExpressionValue(method = "takeSnapshot", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;doesSync()Z"))
    private static boolean cancelSnapshotForRegistries(boolean doesSync, @Local Registry<?> registry) {
        return subtleEffects$isNotDisabledRegistry(registry.key()) && doesSync;
    }

    @ModifyExpressionValue(method = "lambda$getRegistryNamesForSyncToClient$4", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;doesSync()Z"))
    private static <T> boolean cancelRegistryNamesForRegistries(boolean doesSync, @Local(argsOnly = true) Map.Entry<ResourceKey<T>, T> entry) {
        return subtleEffects$isNotDisabledRegistry(entry.getKey()) && doesSync;
    }

    @Unique
    private static <T> boolean subtleEffects$isNotDisabledRegistry(ResourceKey<T> key) {
        return !SubtleEffects.SERVER_CONFIGS.disableRegistrySyncing ||
                (!key.equals(Registries.PARTICLE_TYPE) && !key.equals(Registries.SOUND_EVENT));
    }
}
