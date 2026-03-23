package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.data.DynamicSpriteSetsManager;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@Mixin(ParticleResources.class)
public abstract class ParticleResourcesMixin {

    @Shadow
    @Final
    private Map<Identifier, ParticleResources.MutableSpriteSet> spriteSets;

    @Inject(method = "lambda$reload$1", at = @At("HEAD"))
    private void allowDynamicSpriteSetLoading(Executor executor, Map<Identifier, Resource> resources, CallbackInfoReturnable<CompletionStage<?>> cir) {
        DynamicSpriteSetsManager.reload(spriteSets);
    }
}
