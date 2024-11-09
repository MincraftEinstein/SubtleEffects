package einstein.subtle_effects.util;

import com.google.common.base.Suppliers;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Util {

    public static final int BREATH_DELAY = 60;
    public static final int SNORE_DELAY = 10;
    public static final int MAX_Z_COUNT = 3;
    public static final int STOMACH_GROWL_DELAY = 300;
    public static final DustParticleOptions GLOWSTONE_DUST_PARTICLES = new DustParticleOptions(Vec3.fromRGB24(0xFFBC5E).toVector3f(), 1);
    public static final ResourceLocation CREEPER_SHADER = ResourceLocation.withDefaultNamespace("shaders/post/creeper.json");
    public static final ResourceLocation INVERT_SHADER = ResourceLocation.withDefaultNamespace("shaders/post/invert.json");
    public static final Supplier<Item> ENDERMAN_HEAD = Suppliers.memoize(() -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("supplementaries", "enderman_head")));
    public static final Supplier<Boolean> IS_SUPPLEMENTARIES_LOADED = Suppliers.memoize(() -> Services.PLATFORM.isModLoaded("supplementaries"));
    public static final Supplier<Boolean> IS_SERENE_SEANSONS_LOADED = Suppliers.memoize(() -> Services.PLATFORM.isModLoaded("sereneseasons"));
    public static final Supplier<ResourceLocation> BCWP_PACK_LOCATION = Suppliers.memoize(() -> SubtleEffects.loc("biome_color_water_particles").withPrefix(Services.PLATFORM.getPlatformName().equals("NeoForge") ? "resourcepacks/" : ""));
    public static final Supplier<String> BCWP_PACK_ID = Suppliers.memoize(() -> (Services.PLATFORM.getPlatformName().equals("NeoForge") ? "mod/" : "") + BCWP_PACK_LOCATION.get().toString());
    public static final Component BCWP_PACK_NAME = Component.translatable("resourcePack.subtle_effects.biome_water_color_particles.name");
    public static final List<ParticleType<?>> BIOME_COLORED_PARTICLES = net.minecraft.Util.make(new ArrayList<>(), particles -> {
        particles.add(ParticleTypes.BUBBLE);
        particles.add(ParticleTypes.FISHING);
        particles.add(ParticleTypes.BUBBLE_POP);
        particles.add(ParticleTypes.BUBBLE_COLUMN_UP);
        particles.add(ParticleTypes.CURRENT_DOWN);
        particles.add(ParticleTypes.RAIN);
        particles.add(ParticleTypes.SPLASH);
        particles.add(ParticleTypes.UNDERWATER);
        particles.add(ParticleTypes.DRIPPING_WATER);
        particles.add(ParticleTypes.FALLING_WATER);
        particles.add(ParticleTypes.DRIPPING_DRIPSTONE_WATER);
        particles.add(ParticleTypes.FALLING_DRIPSTONE_WATER);
    });
    public static final ResourceLocation COLORLESS_RAIN_TEXTURE = SubtleEffects.loc("textures/environment/colorless_rain.png");

    public static void playClientSound(Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level != null && level.isClientSide) {
            level.playSound(minecraft.player, entity, sound, source, volume, pitch);
        }
    }

    public static void playClientSound(BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level != null && level.isClientSide) {
            level.playSound(minecraft.player, pos.getX(), pos.getY(), pos.getZ(), sound, source, volume, pitch);
        }
    }

    public static boolean isSolidOrNotEmpty(Level level, BlockPos pos) {
        return level.getBlockState(pos).isSolidRender(level, pos) || !level.getFluidState(pos).isEmpty();
    }

    public static int getLightColor(int superLight) {
        return 240 | superLight >> 16 & 0xFF << 16;
    }

    public static void applyHelmetShader(ItemStack stack) {
        ShaderManager shaderManager = (ShaderManager) Minecraft.getInstance().gameRenderer;
        if (stack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof SkullBlock skullBlock) {
                if (skullBlock.getType() == SkullBlock.Types.CREEPER) {
                    shaderManager.subtleEffects$loadShader(CREEPER_SHADER);
                    return;
                }
                else if (skullBlock.getType() == SkullBlock.Types.DRAGON) {
                    shaderManager.subtleEffects$loadShader(INVERT_SHADER);
                    return;
                }
                else if (isEndermanHead(stack)) {
                    shaderManager.subtleEffects$loadShader(INVERT_SHADER);
                    return;
                }
            }
        }
        shaderManager.subtleEffects$clearShader();
    }

    private static boolean isEndermanHead(ItemStack stack) {
        if (IS_SUPPLEMENTARIES_LOADED.get()) {
            return stack.is(ENDERMAN_HEAD.get());
        }
        return false;
    }

    public static void setColorFromHex(Particle particle, int hexColor) {
        particle.setColor((hexColor >> 16) / 255F, (hexColor >> 8) / 255F, hexColor / 255F);
    }

    public static boolean isBCWPPackLoaded() {
        return Minecraft.getInstance().getResourcePackRepository().getSelectedIds().contains(BCWP_PACK_ID.get());
    }
}
