package einstein.ambient_sleep.mixin.client;

import einstein.ambient_sleep.util.LoopingSpriteSet;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ParticleEngine.MutableSpriteSet.class)
public class MutableSpriteSetMixin implements LoopingSpriteSet {

    @Shadow
    private List<TextureAtlasSprite> sprites;
    @Unique
    private int ambientSleep$index = 0;

    @Inject(method = "rebind", at = @At("TAIL"))
    private void rebind(List<TextureAtlasSprite> sprites, CallbackInfo ci) {
        ambientSleep$index = 0;
    }

    @Override
    public TextureAtlasSprite ambientSleep$nextSpite() {
        if (ambientSleep$index < sprites.size()) {
            TextureAtlasSprite sprite = sprites.get(ambientSleep$index);
            ambientSleep$index++;

            if (ambientSleep$index >= sprites.size()) {
                ambientSleep$index = 0;
            }

            return sprite;
        }
        return sprites.get(0);
    }
}
