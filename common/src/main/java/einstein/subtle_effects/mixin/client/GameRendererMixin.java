package einstein.subtle_effects.mixin.client;

import einstein.subtle_effects.util.ShaderManager;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements ShaderManager {

    @Shadow
    protected abstract void setPostEffect(ResourceLocation resourceLocation);

    @Shadow
    public abstract void clearPostEffect();

    @Override
    public void subtleEffects$loadShader(ResourceLocation location) {
        subtleEffects$clearShader();
        setPostEffect(location);
    }

    @Override
    public void subtleEffects$clearShader() {
        clearPostEffect();
    }
}
