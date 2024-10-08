package einstein.subtle_effects.platform.services;

import einstein.subtle_effects.mixin.client.particle.SpriteSetAccessor;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ParticleHelper {

    @Nullable
    default List<TextureAtlasSprite> getSpritesFromSet(SpriteSet spriteSet) {
        return ((SpriteSetAccessor) spriteSet).getSprites();
    }
}
