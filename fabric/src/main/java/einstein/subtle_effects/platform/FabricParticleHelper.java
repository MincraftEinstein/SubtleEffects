package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.ParticleHelper;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.List;

public class FabricParticleHelper implements ParticleHelper {

    @Override
    public List<TextureAtlasSprite> getSpritesFromSet(SpriteSet spriteSet) {
        if (spriteSet instanceof FabricSpriteProvider spriteProvider) {
            return spriteProvider.getSprites();
        }
        return ParticleHelper.super.getSpritesFromSet(spriteSet);
    }
}
