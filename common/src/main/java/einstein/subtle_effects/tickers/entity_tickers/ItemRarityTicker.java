package einstein.subtle_effects.tickers.entity_tickers;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.compat.ItemBordersCompat;
import einstein.subtle_effects.configs.entities.ItemRarityConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.IntegerParticleOptions;
import einstein.subtle_effects.platform.Services;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIngredient;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;
import static einstein.subtle_effects.init.ModEntityTickers.shouldSpawn;
import static einstein.subtle_effects.util.MathUtil.nextDouble;

public class ItemRarityTicker extends EntityTicker<ItemEntity> {

    private static final TextColor WHITE_TEXT = TextColor.fromLegacyFormat(ChatFormatting.WHITE);
    private static final TagKey<DimensionType> DIMENSIONS = TagKey.create(Registries.DIMENSION_TYPE, SubtleEffects.loc("no_item_rarities"));

    private final List<TextColor> nameColors = new ArrayList<>();
    private final ItemStack stack = entity.getItem();
    private final boolean isCommon = stack.getRarity() == Rarity.COMMON;

    public ItemRarityTicker(ItemEntity entity) {
        super(entity);
        getItemNameColors();

        if (isCommon) {
            if (ENTITIES.itemRarity.particlesDisplayType == ItemRarityConfigs.DisplayType.NOT_COMMON) {
                if (nameColors.size() == 1 && nameColors.getFirst().equals(WHITE_TEXT)) {
                    nameColors.clear();
                }
            }
        }
    }

    private void getItemNameColors() {
        if (level.dimensionTypeRegistration().is(DIMENSIONS)) {
            return;
        }

        if (!ENTITIES.itemRarity.colorOverrides.isEmpty()) {
            for (Map.Entry<ValidatedIngredient.IngredientProvider, ValidatedColor.ColorHolder> entry : ENTITIES.itemRarity.colorOverrides.entrySet()) {
                if (entry.getKey().provide().test(stack)) {
                    nameColors.add(TextColor.fromRgb(entry.getValue().argb()));

                    if (!ENTITIES.itemRarity.mixedColorName) {
                        break;
                    }
                }
            }

            if (ENTITIES.itemRarity.particleColorType == ItemRarityConfigs.ParticleColorType.ONLY_COLOR_OVERRIDES) {
                return;
            }
        }

        if (ENTITIES.itemRarity.particleColorType == ItemRarityConfigs.ParticleColorType.NAME_COLOR) {
            Component hoverName = stack.getHoverName();
            TextColor baseColor = hoverName.getStyle().getColor();
            List<TextColor> colors = new ArrayList<>(hoverName.getSiblings().stream().map(component -> component.getStyle().getColor()).filter(Objects::nonNull).toList());
            boolean usesSingleColor = colors.isEmpty() || !ENTITIES.itemRarity.mixedColorName; // isEmpty needs to be stored before adding the baseColor to the 'colors' list

            if (baseColor != null) {
                colors.add(baseColor);
            }

            if (usesSingleColor) {
                TextColor color = (baseColor != null ? baseColor : (!colors.isEmpty() ? colors.getFirst() : null));
                if (color != null && (!isCommon || !color.equals(WHITE_TEXT))) {
                    nameColors.add(color);
                    return;
                }
            }
            else {
                nameColors.addAll(colors);
                return;
            }
        }

        if (ENTITIES.itemRarity.useItemBorder && CompatHelper.IS_ITEM_BORDERS_LOADED.get()) {
            TextColor borderColor = ItemBordersCompat.getManualBorderColor(level, stack);
            if (borderColor != null) {
                nameColors.add(borderColor);
                return;
            }

            List<TextColor> componentColors = ItemBordersCompat.getNBTBorderColor(stack);
            if (!componentColors.isEmpty()) {
                nameColors.addAll(componentColors);
                return;
            }
        }

        TextColor rarityColor = Services.PARTICLE_HELPER.getRarityColor(stack.getRarity());
        if (rarityColor != null) {
            nameColors.add(rarityColor);
            return;
        }

        SubtleEffects.LOGGER.warn("Failed to get item display name color for item: {}", stack.getDisplayName());
        nameColors.add(WHITE_TEXT);
    }

    @Override
    public void entityTick() {
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
