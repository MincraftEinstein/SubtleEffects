package einstein.subtle_effects.particle;

import einstein.subtle_effects.configs.ModBlockConfigs;
import einstein.subtle_effects.particle.option.PositionParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;

public class BeaconParticle extends SparkParticle {

    private final BlockPos beaconPos;
    private final List<BeaconBeamOwner.Section> beamSections = new ArrayList<>();

    protected BeaconParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, BlockPos beaconPos) {
        super(level, x, y, z, 0, 0, 0, 0, sprites);
        this.beaconPos = beaconPos;
        xd = 0;
        yd = 0;
        zd = 0;
        gravity = -0.1F * BLOCKS.beaconParticlesSpeed.get();
        lifetime = 1;
        hasPhysics = false;
        speedUpWhenYMotionIsBlocked = false;
    }

    @Override
    public void tick() {
        super.tick();

        // every 1 second check if the beacon is still there
        if (age % 20 == 0) {
            BlockEntity blockEntity = level.getBlockEntity(beaconPos);
            if (!(blockEntity instanceof BeaconBlockEntity beaconBlockEntity)) {
                remove();
                return;
            }

            List<BeaconBeamOwner.Section> sections = beaconBlockEntity.getBeamSections();
            boolean isNotColored = BLOCKS.beaconParticlesDisplayType == ModBlockConfigs.BeaconParticlesDisplayType.NOT_COLORED;
            boolean hasMultipleSections = sections.size() > 1;

            if (sections.isEmpty() || (hasMultipleSections && isNotColored)) {
                remove();
                return;
            }

            boolean hadMultipleSections = beamSections.size() > 1;
            beamSections.clear();

            if ((!isNotColored && hasMultipleSections) || hadMultipleSections) {
                beamSections.addAll(sections);
            }
        }

        if (y >= Mth.floor(y) && !beamSections.isEmpty()) {
            BeaconBeamOwner.Section beamSection = null;
            int lastHeight = beaconPos.getY() - 1;

            for (BeaconBeamOwner.Section section : beamSections) {
                lastHeight += section.getHeight();
                if (y >= lastHeight) {
                    beamSection = section;
                }
            }

            if (beamSection != null) {
                int color = beamSection.getColor();
                rCol = ((color >> 16) & 255) / 255F;
                gCol = ((color >> 8) & 255) / 255F;
                bCol = (color & 255) / 255F;
            }
        }

        if (y == yo || !level.isInsideBuildHeight(Mth.floor(y))) {
            remove();
            return;
        }

        lifetime++;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return quadSize * Mth.clamp((age + partialTicks) / 20 * 32.0F, 0.0F, 1.0F);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<PositionParticleOptions> {

        @Override
        public Particle createParticle(PositionParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BeaconParticle(level, x, y, z, sprites, type.pos());
        }
    }
}
