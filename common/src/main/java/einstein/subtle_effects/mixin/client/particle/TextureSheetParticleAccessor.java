package einstein.subtle_effects.mixin.client.particle;

import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextureSheetParticle.class)
public interface TextureSheetParticleAccessor {

    @Invoker("setSprite")
    void setSpriteAccessor(TextureAtlasSprite sprite);
}
