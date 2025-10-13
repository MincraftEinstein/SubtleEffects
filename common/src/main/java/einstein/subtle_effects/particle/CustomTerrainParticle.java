package einstein.subtle_effects.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;

public class CustomTerrainParticle extends TerrainParticle {

    public static final ResourceLocation COMPOST_TEXTURE = ResourceLocation.tryParse("minecraft:block/composter_compost");
    private final Layer renderType;

    public CustomTerrainParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleRenderType renderType) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, Blocks.AIR.defaultBlockState());
        this.renderType = renderType;
        hasPhysics = true;
    }

    @Override
    public Layer getLayer() {
        return renderType;
    }

    public record CompostProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @SuppressWarnings("deprecation")
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            CustomTerrainParticle particle = new CustomTerrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, Layer.TERRAIN);
            particle.setSprite(Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(COMPOST_TEXTURE));
            return particle;
        }
    }

    public record IronGolemProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            CustomTerrainParticle particle = new CustomTerrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, Layer.OPAQUE);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
