package einstein.subtle_effects.particle;

import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class ElectricityParticle extends TextureSheetParticle {

    private final List<TextureAtlasSprite> sprites;
    private int i;

    protected ElectricityParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z);
        xd = xSpeed * 0.03;
        yd = ySpeed * 0.03;
        zd = zSpeed * 0.03;
        List<TextureAtlasSprite> spritesFromSet = Services.PARTICLE_HELPER.getSpritesFromSet(sprites);
        this.sprites = new ArrayList<>(spritesFromSet == null ? List.of() : spritesFromSet);
        lifetime = Mth.nextInt(random, 10, 16);
        gravity = 0;
        setRandomSprite();
    }

    private void setRandomSprite() {
        int i2 = age * (sprites.size() - 1) / lifetime;
        if (age == 0 || i != i2) {
            sprite = sprites.get(random.nextInt(sprites.size()));
            sprites.remove(sprite);
            i = i2;
        }
    }

    @Override
    public void tick() {
        super.tick();
        setRandomSprite();
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Util.PARTICLE_LIGHT_COLOR;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ElectricityParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
