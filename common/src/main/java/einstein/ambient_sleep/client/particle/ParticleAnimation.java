package einstein.ambient_sleep.client.particle;

import einstein.ambient_sleep.platform.Services;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ParticleAnimation {

    private final SpriteSet sprites;
    private final int firstFrameDelay;
    private final int loopCount;
    private final int totalFrames;
    private final int totalTicks;
    private int tick;
    private int frame;

    public ParticleAnimation(SpriteSet sprites, int loopCount) {
        this(sprites, 0, loopCount);
    }

    public ParticleAnimation(SpriteSet sprites, int firstFrameDelay, int loopCount) {
        this.sprites = sprites;
        this.firstFrameDelay = firstFrameDelay;
        this.loopCount = loopCount;
        List<TextureAtlasSprite> textures = Services.PARTICLE_HELPER.getSpritesFromSet(sprites);
        totalFrames = textures != null ? textures.size() : 0;
        totalTicks = firstFrameDelay + totalFrames;
    }

    public void tick() {
        tick++;
        if (firstFrameDelay > 0 && tick < firstFrameDelay) {
            frame = 0;
        }
        else if (tick <= totalTicks) {
            frame++;
        }
        else {
            frame = 0;
            tick = 0;
        }
    }

    public int getAnimationLifetime() {
        return totalTicks * loopCount;
    }

    public TextureAtlasSprite getSpriteForFrame() {
        return sprites.get(frame, totalFrames);
    }

    public SpriteSet getSprites() {
        return sprites;
    }
}
