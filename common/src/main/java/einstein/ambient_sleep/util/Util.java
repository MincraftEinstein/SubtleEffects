package einstein.ambient_sleep.util;

import einstein.ambient_sleep.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.phys.Vec3;

public class Util {

    public static final int BREATH_DELAY = 60;
    public static final int SNORE_DELAY = 10;
    public static final int MAX_Z_COUNT = 3;
    public static final int STOMACH_GROWL_DELAY = 300;
    public static final DustParticleOptions GLOWSTONE_DUST_PARTICLES = new DustParticleOptions(Vec3.fromRGB24(0xFFBC5E).toVector3f(), 1);
    public static final ResourceLocation CREEPER_SHADER = new ResourceLocation("shaders/post/creeper.json");
    public static final ResourceLocation INVERT_SHADER = new ResourceLocation("shaders/post/invert.json");
    public static final ResourceLocation ENDERMAN_HEAD_LOC = new ResourceLocation("supplementaries", "enderman_head");
    public static final boolean IS_SUPPLEMENTARIES_LOADED = Services.PLATFORM.isModLoaded("supplementaries");

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

    public static void applyHelmetShader(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof SkullBlock skullBlock) {
                if (skullBlock.getType() == SkullBlock.Types.CREEPER) {
                    ((ShaderManager) Minecraft.getInstance().gameRenderer).ambientSleep$loadShader(CREEPER_SHADER);
                    return;
                }
                else if (isEndermanHead(stack)) {
                    ((ShaderManager) Minecraft.getInstance().gameRenderer).ambientSleep$loadShader(INVERT_SHADER);
                    return;
                }
            }
        }
        ((ShaderManager) Minecraft.getInstance().gameRenderer).ambientSleep$clearShader();
    }

    private static boolean isEndermanHead(ItemStack stack) {
        if (IS_SUPPLEMENTARIES_LOADED) {
            return stack.is(BuiltInRegistries.ITEM.get(ENDERMAN_HEAD_LOC));
        }
        return false;
    }
}
