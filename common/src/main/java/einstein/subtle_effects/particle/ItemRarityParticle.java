package einstein.subtle_effects.particle;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.entities.ItemRarityConfigs;
import einstein.subtle_effects.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class ItemRarityParticle extends TextureSheetParticle {

    private static final TextColor WHITE_TEXT = TextColor.fromLegacyFormat(ChatFormatting.WHITE);

    protected ItemRarityParticle(ClientLevel level, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ItemStack stack) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        int color = getItemColor(stack, random).getValue();
        color = (((color >> 24) & 255) != 0 ? color : 0xFF000000 | color);
        rCol = ((color >> 16) & 255) / 255F;
        gCol = ((color >> 8) & 255) / 255F;
        bCol = (color & 255) / 255F;
        pickSprite(sprites);
        int max = ENTITIES.itemRarity.particleMaxHeight;
        int min = Math.min(5, max);
        lifetime = Math.max(min, random.nextInt(max));
        xd = 0;
        zd = 0;
    }

    private static TextColor getItemColor(ItemStack stack, RandomSource random) {
        if (ENTITIES.itemRarity.particleColor == ItemRarityConfigs.ColorType.NAME_COLOR) {
            TextColor nameColor = getItemNameColor(stack, random);
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

    public static @Nullable TextColor getItemNameColor(ItemStack stack, RandomSource random) {
        Component hoverName = stack.getHoverName();
        TextColor color = hoverName.getStyle().getColor();
        List<TextColor> colors = new ArrayList<>(hoverName.getSiblings().stream().map(component -> component.getStyle().getColor()).filter(Objects::nonNull).toList());
        boolean usesSingleColor = colors.isEmpty() || !ENTITIES.itemRarity.mixedColorName; // isEmpty needs to be stored before adding the main color to the 'colors' list

        if (color != null) {
            colors.add(color);
        }

        TextColor nameColor = usesSingleColor ? (color != null ? color : (!colors.isEmpty() ? colors.getFirst() : null)) : colors.get(random.nextInt(colors.size()));
        if (nameColor != null && (!usesSingleColor || !nameColor.equals(WHITE_TEXT))) {
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
