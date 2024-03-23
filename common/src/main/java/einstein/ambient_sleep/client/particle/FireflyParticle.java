package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class FireflyParticle extends SuspendedParticle {

    private static final int FIRST_FRAME_TICKS = 16;
    private static final int TOTAL_FRAMES = 19; // Technically there are 20 but 0 is the first frame
    private static final int TOTAL_ANIMATION_TICKS = TOTAL_FRAMES + FIRST_FRAME_TICKS;

    private final SpriteSet sprites;
    private int animationTick;
    private int currentFrame;

    public FireflyParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        hasPhysics = true;
        lifetime = 105; // will allow the animation to play 3
        setColor(1, 1, 1);
        setSpriteFrame();
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240 | super.getLightColor(partialTick) >> 16 & 0xFF << 16;
    }

    @Override
    public void tick() {
        super.tick();
        if (!removed) {
            animationTick++;
            if (animationTick < FIRST_FRAME_TICKS) {
                currentFrame = 0;
            }
            else if (animationTick < TOTAL_ANIMATION_TICKS) {
                currentFrame++;
            }
            else {
                currentFrame = 0;
                animationTick = 0;
            }
            setSpriteFrame();
        }
    }

    private void setSpriteFrame() {
        if (!removed) {
            TextureAtlasSprite sprite = sprites.get(currentFrame, TOTAL_FRAMES);
            setSprite(sprite);
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RandomSource random = level.getRandom();
            xSpeed = (random.nextGaussian() * 1.0E-6F) / 8;
            ySpeed = (random.nextGaussian() * 1.0E-6F) / 8;
            zSpeed = (random.nextGaussian() * 1.0E-6F) / 8;
            return new FireflyParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
