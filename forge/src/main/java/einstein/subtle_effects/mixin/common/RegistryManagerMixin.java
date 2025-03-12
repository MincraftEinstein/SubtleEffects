package einstein.subtle_effects.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RegistryManager.class)
public class RegistryManagerMixin {

    @Inject(method = "lambda$takeSnapshot$2", at = @At("HEAD"), cancellable = true)
    private void cancelSnapshotForRegistries(Map<ResourceLocation, ForgeRegistry.Snapshot> snapshots, ResourceLocation registryId, CallbackInfo ci) {
        if (!subtleEffects$isNotDisabledRegistry(registryId)) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "lambda$getRegistryNamesForSyncToClient$4", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"))
    private static <T> boolean cancelRegistryNamesForRegistries(boolean doesSync, @Local(argsOnly = true) ResourceLocation registryId) {
        return subtleEffects$isNotDisabledRegistry(registryId) && doesSync;
    }

    @Unique
    private static <T> boolean subtleEffects$isNotDisabledRegistry(ResourceLocation registryId) {
        return !SubtleEffects.SERVER_CONFIGS.disableRegistrySyncing ||
                (!registryId.equals(Registries.PARTICLE_TYPE.location()) && !registryId.equals(Registries.SOUND_EVENT.location()));
    }
}
