package einstein.subtle_effects.mixin.client.particle;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.data.DynamicSpriteSetsManager;
import einstein.subtle_effects.data.MissingSpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@Mixin(ParticleEngine.class)
public abstract class NeoForgeParticleEngineMixin {

    @Shadow
    @Final
    private Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ClientLevel level, TextureManager textureManager, CallbackInfo ci) {
        spriteSets.put(MissingSpriteSet.ID, MissingSpriteSet.INSTANCE);
    }

    @Inject(method = "lambda$reload$7", at = @At("HEAD"))
    private void beginReloadingDynamicSpriteSets(Executor executor, Map<ResourceLocation, Resource> resources, CallbackInfoReturnable<CompletionStage<?>> cir) {
        DynamicSpriteSetsManager.beginReload(spriteSets);
    }

    @Inject(method = "lambda$reload$9", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"))
    private void finishReloadingDynamicSpriteSets(CallbackInfo ci, @Local TextureAtlasSprite missingSprite) {
        MissingSpriteSet.INSTANCE.rebind(missingSprite);
        DynamicSpriteSetsManager.finishReload(spriteSets);
    }
}
