package einstein.subtle_effects.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import einstein.subtle_effects.particle.option.SplashParticleOptions;
import einstein.subtle_effects.platform.Services;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

import static net.minecraft.util.Mth.DEG_TO_RAD;

public class SplashParticle extends FlatPlaneParticle {

    private final SpriteSet sprites;
    private final boolean translucent;
    private float xScale;
    private final float yScale;
    private final TextureAtlasSprite overlaySprite;
    private final TextureAtlasSprite bottomSprite;

    // TODO
    //  - animate textures
    //    - need to figure out how to have multiple animated textures for the particle
    //    - lava variant textures
    //  - apply biome coloring
    //    - should overlay and bottom textures be tinted?
    protected SplashParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, boolean translucent, float xScale, float yScale) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.translucent = translucent;
        lifetime = 18;
        xScale /= 2; // Divided by 2 because it is used as the distance from the center
        yScale /= 2;
        this.xScale = xScale;
        this.yScale = yScale;
        quadSize = xScale;
        setSize(xScale, yScale);
        List<TextureAtlasSprite> spriteList = Services.PARTICLE_HELPER.getSpritesFromSet(sprites);

        if (spriteList != null && spriteList.size() >= 3) {
            setSprite(spriteList.getFirst());
            overlaySprite = spriteList.get(1);
            bottomSprite = spriteList.get(2);
            return;
        }
        overlaySprite = bottomSprite = null;
    }

    @Override
    public ParticleRenderType getRenderType() {
        if (translucent) {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprites);

        xScale += 0.005F;
    }

    @Override
    protected void renderQuad(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTicks, yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTicks, zo, this.z) - cameraPos.z());

        renderBox(consumer, partialTicks, x, y, z, getU0(), getV0(), getU1(), getV1(), rCol, gCol, bCol);
//        renderBox(consumer, partialTicks, x, y, z, overlaySprite.getU0(), overlaySprite.getV0(), overlaySprite.getU1(), overlaySprite.getV1(), 1, 1, 1);

//        renderQuad(consumer, new Quaternionf().rotateX(90 * DEG_TO_RAD), partialTicks, x, y, z, false, bottomSprite.getU0(), bottomSprite.getV0(), bottomSprite.getU1(), bottomSprite.getV1(), xScale, xScale, 1, 1, 1);
    }

    private void renderBox(VertexConsumer consumer, float partialTicks, float x, float y, float z, float u0, float v0, float u1, float v1, float red, float green, float blue) {
        renderDoubleQuads(consumer, new Quaternionf(), partialTicks, x, y + yScale, z + xScale, u0, v0, u1, v1, xScale, yScale, red, green, blue);
        renderDoubleQuads(consumer, new Quaternionf().rotateY(90 * DEG_TO_RAD), partialTicks, x - xScale, y + yScale, z, u0, v0, u1, v1, xScale, yScale, red, green, blue);
        renderDoubleQuads(consumer, new Quaternionf().rotateY(180 * DEG_TO_RAD), partialTicks, x, y + yScale, z - xScale, u0, v0, u1, v1, xScale, yScale, red, green, blue);
        renderDoubleQuads(consumer, new Quaternionf().rotateY(-90 * DEG_TO_RAD), partialTicks, x + xScale, y + yScale, z, u0, v0, u1, v1, xScale, yScale, red, green, blue);
    }

    protected void renderDoubleQuads(VertexConsumer consumer, Quaternionf rotation, float partialTicks, float x, float y, float z, float u0, float v0, float u1, float v1, float quadWidthSize, float quadHeightSize, float red, float green, float blue) {
        renderQuad(consumer, rotation, partialTicks, x, y, z, false, u0, v0, u1, v1, quadWidthSize, quadHeightSize, red, green, blue);
        renderQuad(consumer, rotation, partialTicks, x, y, z, true, u0, v0, u1, v1, quadWidthSize, quadHeightSize, red, green, blue);
    }

    protected void renderQuad(VertexConsumer consumer, Quaternionf rotation, float partialTicks, float x, float y, float z, boolean renderInverted, float u0, float v0, float u1, float v1, float quadWidthSize, float quadHeightSize, float red, float green, float blue) {
        int packedLight = getLightColor(partialTicks);
        int i = renderInverted ? 1 : -1;
        int i2 = renderInverted ? -1 : 1;

        renderVertex(consumer, rotation, x, y, z, i, -1, quadWidthSize, quadHeightSize, u1, v1, packedLight, red, green, blue);
        renderVertex(consumer, rotation, x, y, z, i, 1, quadWidthSize, quadHeightSize, u1, v0, packedLight, red, green, blue);
        renderVertex(consumer, rotation, x, y, z, i2, 1, quadWidthSize, quadHeightSize, u0, v0, packedLight, red, green, blue);
        renderVertex(consumer, rotation, x, y, z, i2, -1, quadWidthSize, quadHeightSize, u0, v1, packedLight, red, green, blue);
    }

    protected void renderVertex(VertexConsumer buffer, Quaternionf rotation, float x, float y, float z, float xOffset, float yOffset, float quadWidthSize, float quadHeightSize, float u, float v, int packedLight, float red, float green, float blue) {
        Vector3f vector3f = new Vector3f(xOffset, yOffset, 0).rotate(rotation).mul(quadWidthSize, quadHeightSize, quadWidthSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(red, green, blue, alpha).setLight(packedLight);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SplashParticleOptions> {

        @Override
        public Particle createParticle(SplashParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SplashParticle(level, x, y, z, sprites, true, options.xScale(), options.yScale());
        }
    }

    public record LavaProvider(SpriteSet sprites) implements ParticleProvider<SplashParticleOptions> {

        @Override
        public Particle createParticle(SplashParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SplashParticle(level, x, y, z, sprites, false, options.xScale(), options.yScale());
        }
    }
}
