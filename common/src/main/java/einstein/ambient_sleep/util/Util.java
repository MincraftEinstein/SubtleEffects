package einstein.ambient_sleep.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Util {

    public static final int BREATH_DELAY = 60;
    public static final int SNORE_DELAY = 10;
    public static final int MAX_Z_COUNT = 3;
    public static final int STOMACH_GROWL_DELAY = 300;
    public static final DustParticleOptions GLOWSTONE_DUST_PARTICLES = new DustParticleOptions(Vec3.fromRGB24(0xFFBC5E).toVector3f(), 1);

    public static void playClientSound(SoundSource source, Entity entity, SoundEvent sound, float volume, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level.isClientSide) {
            level.playSeededSound(minecraft.player, entity, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), source, volume, pitch, level.random.nextLong());
        }
    }

    public static boolean isSolidOrNotEmpty(Level level, BlockPos pos) {
        return level.getBlockState(pos).isSolidRender(level, pos) || !level.getFluidState(pos).isEmpty();
    }

    public static int getLightColor(int superLight) {
        return 240 | superLight >> 16 & 0xFF << 16;
    }
}
