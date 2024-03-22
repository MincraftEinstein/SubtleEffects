package einstein.ambient_sleep.client.particle;

import einstein.ambient_sleep.client.particle.option.CommandBlockParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class CommandBlockParticle extends TextureSheetParticle {

    private final double xStart;
    private final double yStart;
    private final double zStart;
    private final Direction direction;

    public CommandBlockParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Direction direction) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;
        xStart = x;
        yStart = y;
        zStart = z;
        xo = x + xSpeed;
        yo = y + ySpeed;
        zo = z + zSpeed;
        this.x = xo;
        this.y = yo;
        this.z = zo;
        this.direction = direction;
        quadSize = 0.1F;
        hasPhysics = false;
        lifetime = 50;
        float shade = Mth.clamp(level.random.nextFloat(), 0.6F, 1);
        setColor(shade, shade, shade);
        pickSprite(sprites);
    }

    @Override
    public void tick() {
        if (age++ >= lifetime) {
            remove();
        }

        float $$0 = (float) age / lifetime;
        $$0 = 1 - $$0;
        float $$1 = 1 - $$0;
        $$1 *= $$1;
        $$1 *= $$1;
        xo = x;
        yo = y;
        zo = z;
        x = xStart + xd * $$0 + (($$1 * 1.2) * -direction.getStepX());
        y = yStart + yd * $$0 + (($$1 * 1.2) * -direction.getStepY());
        z = zStart + zd * $$0 + (($$1 * 1.2) * -direction.getStepZ());
    }

    @Override
    public void move(double x, double y, double z) {
        setBoundingBox(getBoundingBox().move(x, y, z));
        setLocationFromBoundingbox();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240 | super.getLightColor(partialTick) >> 16 & 0xFF << 16;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<CommandBlockParticleOptions> {

        @Override
        public Particle createParticle(CommandBlockParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new CommandBlockParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed, type.direction());
        }
    }
}
