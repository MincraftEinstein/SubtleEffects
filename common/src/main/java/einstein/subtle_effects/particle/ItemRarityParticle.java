package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.IntegerParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.RandomSource;

import static einstein.subtle_effects.init.ModConfigs.ITEMS;

public class ItemRarityParticle extends SingleQuadParticle {

    private final double maxY;

    protected ItemRarityParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int color) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        color = (((color >> 24) & 255) != 0 ? color : 0xFF000000 | color);
        rCol = ((color >> 16) & 255) / 255F;
        gCol = ((color >> 8) & 255) / 255F;
        bCol = (color & 255) / 255F;
        pickSprite(sprites);
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
    protected int getLightColor(float partialTick) {
        return Util.PARTICLE_LIGHT_COLOR;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<IntegerParticleOptions> {

        @Override
        public Particle createParticle(IntegerParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new ItemRarityParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.integer(), sprites.get(random));
        }
    }
}
