package einstein.subtle_effects.tickers;

import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class DrowningTicker extends Ticker<Player> {

    public DrowningTicker(Player entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (ENTITIES.drowningBubbles != ModEntityConfigs.PerspectiveType.OFF) {
            if (entity.isCreative() || entity.isSpectator()) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (entity.equals(player) && !ENTITIES.drowningBubbles.test(minecraft)) {
                return;
            }

            if (random.nextInt(ENTITIES.drowningBubblesDensity.get()) == 0 && entity.isUnderWater()) {
                ParticleSpawnUtil.spawnEntityFaceParticles(ParticleTypes.BUBBLE,
                        entity, level, new Vec3(0, -0.1, 0), Vec3.ZERO);
            }
        }
    }
}
