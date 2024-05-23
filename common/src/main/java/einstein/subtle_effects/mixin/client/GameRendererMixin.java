package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.util.ShaderManager;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements ShaderManager {

    @Shadow
    @Nullable
    PostChain postEffect;

    @Shadow
    abstract void loadEffect(ResourceLocation location);

    @Override
    public void subtleEffects$loadShader(ResourceLocation location) {
        subtleEffects$clearShader();
        loadEffect(location);
    }

    @Override
    public void subtleEffects$clearShader() {
        if (postEffect != null) {
            postEffect.close();
        }

        postEffect = null;
    }
}
