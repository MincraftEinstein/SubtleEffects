package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.util.ShaderManager;
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
    public void ambientSleep$loadShader(ResourceLocation location) {
        ambientSleep$clearShader();
        loadEffect(location);
    }

    @Override
    public void ambientSleep$clearShader() {
        if (postEffect != null) {
            postEffect.close();
        }

        postEffect = null;
    }
}
