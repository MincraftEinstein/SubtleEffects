package einstein.subtle_effects.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;

public class CustomTerrainParticle extends TerrainParticle {

    public static final ResourceLocation COMPOST_TEXTURE = ResourceLocation.tryParse("minecraft:block/composter_compost");
    private final ParticleRenderType renderType;

    public CustomTerrainParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleRenderType renderType) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, Blocks.AIR.defaultBlockState());
        this.renderType = renderType;
        hasPhysics = true;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return renderType;
    }

    public record CompostProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CustomTerrainParticle particle = new CustomTerrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, ParticleRenderType.TERRAIN_SHEET);
            particle.setSprite(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(COMPOST_TEXTURE));
            return particle;
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CustomTerrainParticle particle = new CustomTerrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, ParticleRenderType.PARTICLE_SHEET_OPAQUE);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
