package einstein.subtle_effects.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.subtle_effects.data.DynamicSpriteSetsManager;
import einstein.subtle_effects.data.MissingSpriteSet;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
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

@Mixin(ParticleResources.class)
public abstract class ParticleResourcesMixin {

    @Shadow
    @Final
    private Map<Identifier, ParticleResources.MutableSpriteSet> spriteSets;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        spriteSets.put(MissingSpriteSet.ID, MissingSpriteSet.INSTANCE);
    }

    @Inject(method = "lambda$reload$1", at = @At("HEAD"))
    private void beginReloadingDynamicSpriteSets(Executor taskExecutor, Map<Identifier, Resource> definitionsToScan, CallbackInfoReturnable<CompletionStage<?>> cir) {
        DynamicSpriteSetsManager.beginReload(spriteSets);
    }

    @Inject(method = "lambda$reload$4", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"))
    private void finishReloadingDynamicSpriteSets(CallbackInfo ci, @Local TextureAtlasSprite missingSprite) {
        MissingSpriteSet.INSTANCE.rebind(missingSprite);
        DynamicSpriteSetsManager.finishReload(spriteSets);
    }
}
