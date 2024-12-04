package einstein.subtle_effects.tickers;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.configs.entities.ItemRarityConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.IntegerParticleOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.init.ModTickers.shouldSpawn;
import static einstein.subtle_effects.util.MathUtil.nextDouble;

public class ItemRarityTicker extends Ticker<ItemEntity> {

    private static final TextColor WHITE_TEXT = TextColor.fromLegacyFormat(ChatFormatting.WHITE);

    private final List<TextColor> nameColors = new ArrayList<>();
    private final ItemStack stack = entity.getItem();

    public ItemRarityTicker(ItemEntity entity) {
        super(entity);
        getItemNameColors();

        if (stack.getRarity() == Rarity.COMMON) {
            if (ENTITIES.itemRarity.particlesDisplayType == ItemRarityConfigs.DisplayType.NOT_COMMON) {
                if (ENTITIES.itemRarity.particleColor != ItemRarityConfigs.ColorType.NAME_COLOR && nameColors.size() == 1 && nameColors.getFirst().equals(WHITE_TEXT)) {
                    nameColors.clear();
                }
            }
        }
    }

    private void getItemNameColors() {
        if (ENTITIES.itemRarity.particleColor == ItemRarityConfigs.ColorType.NAME_COLOR) {
            Component hoverName = stack.getHoverName();
            TextColor baseColor = hoverName.getStyle().getColor();
            List<TextColor> colors = new ArrayList<>(hoverName.getSiblings().stream().map(component -> component.getStyle().getColor()).filter(Objects::nonNull).toList());
            boolean usesSingleColor = colors.isEmpty() || !ENTITIES.itemRarity.mixedColorName; // isEmpty needs to be stored before adding the baseColor to the 'colors' list

            if (baseColor != null) {
                colors.add(baseColor);
            }

            if (usesSingleColor) {
                TextColor color = (baseColor != null ? baseColor : (!colors.isEmpty() ? colors.getFirst() : null));
                if (color != null && !color.equals(WHITE_TEXT)) {
                    nameColors.add(color);
                    return;
                }
            }
            else {
                nameColors.addAll(colors);
                return;
            }
        }

        TextColor rarityColor = TextColor.fromLegacyFormat(stack.getRarity().color());
        if (rarityColor != null) {
            nameColors.add(rarityColor);
            return;
        }

        SubtleEffects.LOGGER.warn("Failed to get item display name color for item: {}", stack.getDisplayName());
        nameColors.add(WHITE_TEXT);
    }

    @Override
    public void tick() {
        if (nameColors.isEmpty()) {
            return;
        }

        if (shouldSpawn(random, ENTITIES.itemRarity.particleDensity)) {
            level.addParticle(
                    new IntegerParticleOptions(
                            ModParticles.ITEM_RARITY.get(),
                            (nameColors.size() > 1 ? nameColors.get(random.nextInt(nameColors.size())) : nameColors.getFirst()).getValue()
                    ),
                    entity.getRandomX(1),
                    entity.getY(),
                    entity.getRandomZ(1),
                    0,
                    nextDouble(random, 0.02),
                    0
            );
        }
    }
}
