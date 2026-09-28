package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.ColorProviderParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

import static einstein.subtle_effects.init.ModConfigs.ITEMS;

public class ItemRarityParticle extends SingleQuadParticle {

    private final double maxY;

    protected ItemRarityParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite, ColorProviderParticleOptions options) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        Vector3f color = options.provider().provideColor(level, x, y, z, random);
        setColor(color.x(), color.y(), color.z());
        maxY = y + ITEMS.itemRarity.particleMaxHeight.get();
        gravity = -0.1F;
        lifetime = 1;
        xd = 0;
        yd *= ITEMS.itemRarity.particleMaxSpeed.get();
        zd = 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (y == yo || y >= maxY) {
            remove();
            return;
        }

        lifetime++;
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    @Override
    protected int getLightCoords(float partialTick) {
        return Util.PARTICLE_LIGHT_COLOR;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ColorProviderParticleOptions> {

        @Override
        public Particle createParticle(ColorProviderParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new ItemRarityParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(random), options);
        }
    }
}
