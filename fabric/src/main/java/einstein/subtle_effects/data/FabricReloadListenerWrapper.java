package einstein.subtle_effects.data;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricReloadListenerWrapper<T extends PreparableReloadListener & NamedReloadListener> implements IdentifiableResourceReloadListener {

    private final T listener;

    public FabricReloadListenerWrapper(T listener) {
        this.listener = listener;
    }

    @Override
    public ResourceLocation getFabricId() {
        return listener.getId();
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, Executor backgroundExecutor, Executor gameExecutor) {
        return listener.reload(preparationBarrier, resourceManager, backgroundExecutor, gameExecutor);
    }

    @Override
    public String getName() {
        return listener.getName();
    }
}
