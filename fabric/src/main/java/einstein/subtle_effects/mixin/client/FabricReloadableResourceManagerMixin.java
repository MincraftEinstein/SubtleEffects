package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.data.splash_types.SplashTypeReloadListener;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ReloadableResourceManager.class)
public abstract class FabricReloadableResourceManagerMixin {

    @Shadow
    @Final
    private List<PreparableReloadListener> listeners;

    // Despite Fabric api having dependency ordering. Fabric's ordering only takes place during the
    // apply phase, which won't do because custom sprite sets need to be loaded before the particle definitions in ParticleResources.
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addAdditionalListeners(PackType type, CallbackInfo ci) {
        if (type == PackType.CLIENT_RESOURCES) {
            listeners.addFirst(new SplashTypeReloadListener());
        }
    }
}
