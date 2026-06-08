package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.compat.ItemBordersCompat;
import einstein.subtle_effects.configs.items.ItemRarityConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.ColorProviderParticleOptions;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static einstein.subtle_effects.init.ModConfigs.ITEMS;
import static einstein.subtle_effects.init.ModEntityTickers.shouldSpawn;
import static einstein.subtle_effects.util.MathUtil.nextDouble;

public class ItemRarityTicker extends EntityTicker<ItemEntity> {

    private static final TextColor WHITE_TEXT = TextColor.fromLegacyFormat(ChatFormatting.WHITE);
    private static final TagKey<DimensionType> DIMENSIONS = TagKey.create(Registries.DIMENSION_TYPE, SubtleEffects.loc("no_item_rarities"));

    private final List<Integer> colors = new ArrayList<>();
    private final ItemStack stack = entity.getItem();
    private final boolean isCommon = stack.getRarity() == Rarity.COMMON;

    public ItemRarityTicker(ItemEntity entity) {
        super(entity, true);
        getItemNameColors();

        if (isCommon) {
            if (ITEMS.itemRarity.particlesDisplayType.get() == ItemRarityConfigs.DisplayType.NOT_COMMON) {
                // noinspection ConstantConditions
                if (colors.size() == 1 && colors.getFirst().equals(WHITE_TEXT.getValue())) {
                    colors.clear();
                }
            }
        }
    }

    private void getItemNameColors() {
        if (level.dimensionTypeRegistration().is(DIMENSIONS)) {
            return;
        }

        if (!ITEMS.itemRarity.colorOverrides.get().isEmpty()) {
            for (Map.Entry<ValidatedIngredient.IngredientProvider, ? extends ValidatedColor.ColorHolder> entry : ITEMS.itemRarity.colorOverrides.get().entrySet()) {
                if (entry.getKey().provide().test(stack)) {
                    addColor(TextColor.fromRgb(entry.getValue().argb()));

                    if (!ITEMS.itemRarity.mixedColorName.get()) {
                        break;
                    }
                }
            }

            if (ITEMS.itemRarity.particleColorType.get() == ItemRarityConfigs.ParticleColorType.ONLY_COLOR_OVERRIDES) {
                return;
            }
        }

        if (ITEMS.itemRarity.particleColorType.get() == ItemRarityConfigs.ParticleColorType.NAME_COLOR) {
            Component hoverName = stack.getHoverName();
            TextColor baseColor = hoverName.getStyle().getColor();
            List<TextColor> colors = new ArrayList<>(hoverName.getSiblings().stream().map(component -> component.getStyle().getColor()).filter(Objects::nonNull).toList());

            Component displayName = stack.getDisplayName();
            TextColor dispColor = displayName.getStyle().getColor();
            if (baseColor == null && dispColor != null && !dispColor.equals(WHITE_TEXT)) {
                baseColor = dispColor;
            }

            boolean usesSingleColor = colors.isEmpty() || !ITEMS.itemRarity.mixedColorName.get(); // isEmpty needs to be stored before adding the baseColor to the 'colors' list

            if (baseColor != null) {
                colors.add(baseColor);
            }

            if (usesSingleColor) {
                TextColor color = (baseColor != null ? baseColor : (!colors.isEmpty() ? colors.getFirst() : null));
                if (color != null && (!isCommon || !color.equals(WHITE_TEXT))) {
                    addColor(color);
                    return;
                }
            }
            else {
                addColors(colors);
                return;
            }
        }

        if (ITEMS.itemRarity.useItemBorder.get() && CompatHelper.IS_ITEM_BORDERS_LOADED.get()) {
            TextColor borderColor = ItemBordersCompat.getManualBorderColor(level, stack);
            if (borderColor != null) {
                addColor(borderColor);
                return;
            }

            List<TextColor> componentColors = ItemBordersCompat.getNBTBorderColor(stack);
            if (!componentColors.isEmpty()) {
                addColors(componentColors);
                return;
            }
        }

        TextColor rarityColor = Services.PARTICLE_HELPER.getRarityColor(stack.getRarity());
        if (rarityColor != null) {
            addColor(rarityColor);
            return;
        }

        SubtleEffects.LOGGER.warn("Failed to get item display name color for item: {}", stack.getDisplayName());
        addColor(WHITE_TEXT);
    }

    public void addColors(List<TextColor> colors) {
        colors.forEach(this::addColor);
    }

    private void addColor(TextColor textColor) {
        int color = textColor.getValue();
        color = (((color >> 24) & 255) != 0 ? color : 0xFF000000 | color);
        int rCol = (color >> 16) & 255;
        int gCol = (color >> 8) & 255;
        int bCol = color & 255;
        colors.add(rCol << 16 | gCol << 8 | bCol);
    }

    @Override
    public void entityTick() {
        if (colors.isEmpty()) {
            return;
        }

        if (shouldSpawn(random, ITEMS.itemRarity.particleDensity)) {
            level.addParticle(
                    new ColorProviderParticleOptions(
                            ModParticles.ITEM_RARITY.get(),
                            colors.size() > 1 ? colors.get(random.nextInt(colors.size())) : colors.getFirst()
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
