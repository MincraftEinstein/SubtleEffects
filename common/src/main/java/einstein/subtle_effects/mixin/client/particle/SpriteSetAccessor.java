package einstein.subtle_effects.mixin.client.particle;

import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ParticleResources.MutableSpriteSet.class)
public interface SpriteSetAccessor {

    @Accessor("sprites")
    List<TextureAtlasSprite> getSprites();
}
