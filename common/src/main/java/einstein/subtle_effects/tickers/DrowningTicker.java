package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class DrowningTicker extends Ticker<LivingEntity> {

    public DrowningTicker(LivingEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (entity.isSpectator() || entity.getAirSupply() <= 0) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (entity instanceof Player player) {
            if (player.equals(minecraft.player) && !ENTITIES.humanoids.drowningBubblesDisplayType.test(minecraft)) {
                return;
            }
        }

        if (random.nextInt() < ENTITIES.humanoids.drowningBubblesDensity.get() && entity.isUnderWater()) {
            ParticleSpawnUtil.spawnEntityFaceParticle(ModParticles.DROWNING_BUBBLE.get(),
                    entity, level, new Vec3(0, -0.1, 0), Vec3.ZERO,
                    minecraft.getFrameTime()
            );
        }
    }
}
