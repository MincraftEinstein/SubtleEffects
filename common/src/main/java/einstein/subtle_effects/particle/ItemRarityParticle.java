package einstein.subtle_effects.particle;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.entities.ItemRarityConfigs;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemRarityParticle extends TextureSheetParticle {

    private static final TextColor WHITE_TEXT = TextColor.fromLegacyFormat(ChatFormatting.WHITE);

    protected ItemRarityParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ItemStack stack) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        Util.setColorFromHex(this, 0xFF000000 | getItemColor(stack).getValue());
        pickSprite(sprites);
        int max = ModConfigs.ENTITIES.itemRarity.particleMaxHeight;
        int min = Math.min(5, max);
        lifetime = Math.max(min, random.nextInt(max));
        xd = 0;
        zd = 0;
    }

    private static TextColor getItemColor(ItemStack stack) {
        if (ModConfigs.ENTITIES.itemRarity.particleColor == ItemRarityConfigs.ColorType.NAME_COLOR) {
            TextColor nameColor = getItemNameColor(stack);
            if (nameColor != null) {
                return nameColor;
            }
        }

        TextColor rarityColor = TextColor.fromLegacyFormat(stack.getRarity().color());
        if (rarityColor != null) {
            return rarityColor;
        }

        SubtleEffects.LOGGER.error("Failed to get text color for item: {}", stack.getDisplayName());
        return WHITE_TEXT;
    }

    public static @Nullable TextColor getItemNameColor(ItemStack stack) {
        TextColor nameColor = stack.getHoverName().getStyle().getColor();
        if (nameColor != null && !nameColor.equals(WHITE_TEXT)) {
            return nameColor;
        }
        return null;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return Util.getLightColor(super.getLightColor(partialTick));
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<ItemParticleOption> {

        @Nullable
        @Override
        public Particle createParticle(ItemParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ItemRarityParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed, type.getItem());
        }
    }
}
