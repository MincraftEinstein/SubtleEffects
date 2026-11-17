package einstein.subtle_effects.util;

import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.compat.EndRemasteredCompat;
import einstein.subtle_effects.data.MobSkullShaderData;
import einstein.subtle_effects.data.MobSkullShaderReloadListener;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.EnderEyePlacedRingParticle;
import einstein.subtle_effects.util.accesssors.RenderStateAttachmentAccessor;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static einstein.subtle_effects.init.ModRenderStateAttachmentKeys.IS_SLEEPING;

public class ClientUtil {

    public static void playClientSound(Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level != null && level.isClientSide()) {
            level.playSound(minecraft.player, entity, sound, source, volume, pitch);
        }
    }

    public static void playClientSound(BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level != null && level.isClientSide()) {
            level.playSound(minecraft.player, pos.getX(), pos.getY(), pos.getZ(), sound, source, volume, pitch);
        }
    }

    public static void applyHelmetShader(ItemStack stack, CameraType cameraType) {
        if (!cameraType.isFirstPerson()) {
            return;
        }

        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        for (MobSkullShaderData shaderData : MobSkullShaderReloadListener.MOB_SKULL_SHADERS.values()) {
            if (shaderData.stackHolder().matches(stack)) {
                loadShaderEffect(shaderData.shaderId(), gameRenderer);
                return;
            }
        }
        gameRenderer.clearSpectatedEntityPostEffect();
    }

    private static void loadShaderEffect(Identifier shaderId, GameRenderer gameRenderer) {
        Identifier effect = gameRenderer.spectatedEntityPostEffect();
        if (effect == null || !effect.equals(shaderId)) {
            gameRenderer.setSpectatedEntityPostEffect(shaderId);
        }
    }

    public static float getPartialTicks() {
        return getPartialTicks(false);
    }

    public static float getPartialTicks(boolean runsNormally) {
        return Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(runsNormally);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static Vec3 adjustedNameTagPosition(LivingEntityRenderState renderState, Vec3 nameTagPos) {
        if (!ModConfigs.ENTITIES.sleeping.adjustNameTagWhenSleeping) {
            return nameTagPos;
        }

        boolean isSleeping = ((RenderStateAttachmentAccessor) renderState).subtleEffects$get(IS_SLEEPING, false);
        if (isSleeping) {
            Direction facing = renderState.bedOrientation;

            if (facing != null) {
                Direction.Axis axis = facing.getAxis();
                double x = nameTagPos.x() + (0.3 * (axis == Direction.Axis.Z ? -1 : 0));
                double y = nameTagPos.y() + 0.5;
                double z = nameTagPos.z() + (0.3 * (axis == Direction.Axis.X ? -1 : 0));

                return switch (facing) {
                    case NORTH -> new Vec3(z, x, -y);
                    case SOUTH -> new Vec3(z, x, y);
                    case EAST -> new Vec3(y, z, x);
                    case WEST -> new Vec3(-y, z, x);
                    default -> nameTagPos;
                };
            }
        }
        return nameTagPos;
    }

    public static void setColorFromHex(SingleQuadParticle particle, int hexColor) {
        particle.setColor((hexColor >> 16) / 255F, (hexColor >> 8) / 255F, hexColor / 255F);
    }

    public static void setRandomizedColor(SingleQuadParticle particle, RandomSource random, float r, float g, float b) {
        float multiplier = random.nextFloat() * 0.4F + 0.6F;
        particle.setColor(
                randomizeColor(random, r, multiplier),
                randomizeColor(random, g, multiplier),
                randomizeColor(random, b, multiplier)
        );
    }

    public static ValidatedColor.ColorHolder getEyeColorHolder(Level level, BlockPos pos) {
        if (CompatHelper.IS_END_REMASTERED_LOADED.get()) {
            ValidatedColor.ColorHolder color = EndRemasteredCompat.getEyeColor(level, pos);

            if (color != null) {
                return color;
            }
        }

        ValidatedColor.ColorHolder vanillaColor = BLOCKS.eyeColors.get().get(Util.VANILLA_EYE);
        if (vanillaColor != null) {
            return vanillaColor;
        }

        return toColorHolder(EnderEyePlacedRingParticle.DEFAULT_COLOR);
    }

    public static ValidatedColor.ColorHolder toColorHolder(int color) {
        return new ValidatedColor.ColorHolder((color >> 16) & 255, (color >> 8) & 255, color & 255, 255, false);
    }

    public static float randomizeColor(RandomSource random, float color, float multiplier) {
        return (random.nextFloat() * 0.2F + 0.8F) * color * multiplier;
    }
}
