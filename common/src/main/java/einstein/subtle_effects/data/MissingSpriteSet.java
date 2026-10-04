package einstein.subtle_effects.data;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MissingSpriteSet extends ParticleResources.MutableSpriteSet {

    public static final Identifier ID = SubtleEffects.loc("missing");
    public static final MissingSpriteSet INSTANCE = new MissingSpriteSet();

    @Nullable
    private TextureAtlasSprite sprite;

    public TextureAtlasSprite get() {
        if (sprite == null) {
            throw new IllegalStateException("Missing sprite set texture was missing");
        }
        return sprite;
    }

    @Override
    public TextureAtlasSprite get(RandomSource random) {
        return get();
    }

    @Override
    public TextureAtlasSprite get(int particleAge, int particleMaxAge) {
        return get();
    }

    @Override
    public void rebind(List<TextureAtlasSprite> sprites) {
        rebind(sprites.getFirst());
    }

    public void rebind(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }
}
