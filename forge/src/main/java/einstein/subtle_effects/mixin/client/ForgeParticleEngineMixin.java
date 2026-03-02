package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.data.DynamicSpriteSetsManager;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.resources.ResourceLocation;
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

@Mixin(ParticleEngine.class)
public abstract class ForgeParticleEngineMixin {

    @Shadow
    @Final
    private Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets;

    @Inject(method = "lambda$reload$7", at = @At("HEAD"))
    private void allowDynamicSpriteSetLoading(Executor executor, Map<ResourceLocation, Resource> resources, CallbackInfoReturnable<CompletionStage<?>> cir) {
        DynamicSpriteSetsManager.reload(spriteSets);
    }
}
