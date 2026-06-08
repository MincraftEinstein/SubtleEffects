package einstein.subtle_effects.particle;

import einstein.subtle_effects.particle.option.ColorProviderParticleOptions;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Vector3f;

import static einstein.subtle_effects.init.ModConfigs.ITEMS;

public class ItemRarityParticle extends TextureSheetParticle {

    private final double maxY;

    protected ItemRarityParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ColorProviderParticleOptions options) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        Vector3f color = options.provider().provideColor(level, x, y, z, random);
        setColor(color.x(), color.y(), color.z());
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Util.PARTICLE_LIGHT_COLOR;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ColorProviderParticleOptions> {

        @Override
        public Particle createParticle(ColorProviderParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ItemRarityParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }
}
