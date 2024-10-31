package einstein.subtle_effects.tickers;

import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class FrostyBreathTicker extends Ticker<Player> {

    private int breatheTimer = 0;
    private int breatheOutTimer = 0;

    public FrostyBreathTicker(Player entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (ENTITIES.frostyBreath != ModEntityConfigs.PerspectiveType.OFF) {
            BlockPos pos = entity.blockPosition();
            if (entity.isCreative() || entity.isSpectator()) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (entity.equals(player) && !ENTITIES.frostyBreath.test(minecraft)) {
                return;
            }

            if (entity.isInWater() || entity.isEyeInFluid(FluidTags.LAVA)) {
                return;
            }

            if (level.getBiome(pos).value().coldEnoughToSnow(pos)) {
                if (breatheTimer >= Util.BREATH_DELAY) {
                    ParticleSpawnUtil.spawnEntityFaceParticle(ModParticles.FROSTY_BREATH.get(),
                            entity, level, random, new Vec3(0, -0.1, 0),
                            new Vec3(0, 0, Mth.nextDouble(random, 0.005, 0.01))
                    );

                    if (breatheOutTimer >= 7) {
                        breatheOutTimer = 0;
                        breatheTimer = 0;
                    }
                    breatheOutTimer++;
                }
                breatheTimer++;
            }
        }
    }
}
