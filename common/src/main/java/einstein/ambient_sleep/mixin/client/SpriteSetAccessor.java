package einstein.ambient_sleep.mixin.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(targets = "net/minecraft/client/particle/ParticleEngine$MutableSpriteSet")
public interface SpriteSetAccessor {

    @Accessor("sprites")
    List<TextureAtlasSprite> getSprites();
}
