package einstein.ambient_sleep.platform.services;

import einstein.ambient_sleep.mixin.client.SpriteSetAccessor;
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
