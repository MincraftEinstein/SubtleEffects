package einstein.subtle_effects.platform.services;

import einstein.subtle_effects.mixin.client.particle.SpriteSetAccessor;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ParticleHelper {

    @Nullable
    default List<TextureAtlasSprite> getSpritesFromSet(SpriteSet spriteSet) {
        return ((SpriteSetAccessor) spriteSet).getSprites();
    }

    default TextColor getRarityColor(Rarity rarity) {
        return TextColor.fromLegacyFormat(rarity.color());
    }
}
