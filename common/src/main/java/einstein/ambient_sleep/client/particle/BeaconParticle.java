package einstein.ambient_sleep.client.particle;

import einstein.ambient_sleep.client.particle.option.PositionParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BeaconParticle extends SparkParticle {

    private final BlockPos beaconPos;

    protected BeaconParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, BlockPos beaconPos) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, 0, sprites);
        this.beaconPos = beaconPos;
        lifetime = 1;
    }

    @Override
    public void tick() {
        super.tick();

        // every 1 second check if the beacon is still there
        if (age % 20 == 0) {
            BlockEntity blockEntity = level.getBlockEntity(beaconPos);
            if (!(blockEntity instanceof BeaconBlockEntity)) {
                remove();
                return;
            }
        }

        if (y == yo || y >= level.getMaxBuildHeight()) {
            remove();
            return;
        }

        lifetime++;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return quadSize * Mth.clamp((age + scaleFactor) / 20 * 32.0F, 0.0F, 1.0F);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<PositionParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(PositionParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BeaconParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, type.pos());
        }
    }
}
