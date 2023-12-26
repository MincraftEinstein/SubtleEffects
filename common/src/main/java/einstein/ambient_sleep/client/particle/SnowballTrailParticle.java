package einstein.ambient_sleep.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class SnowballTrailParticle extends TextureSheetParticle {

    private final BlockPos.MutableBlockPos pos;

    protected SnowballTrailParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        pos = new BlockPos.MutableBlockPos();
        lifetime = 250;
        quadSize = 0.1F;
        gravity = 0.1F;
        xd = 0;
        yd = -gravity;
        zd = 0;
        setSize(0.1F, 0.1F);
        setSprite(sprites.get(level.random));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        pos.set(x, y, z);
        oRoll = roll;
        roll = oRoll + 0.1F;
        if (onGround || level.getFluidState(pos).is(Fluids.WATER)) {
            remove();
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SnowballTrailParticle(level, x, y, z, sprites);
        }
    }
}
