package einstein.subtle_effects.compat;

import com.anthonyhilyard.iceberg.util.Selectors;
import com.anthonyhilyard.itemborders.ItemBordersConfig;
import com.anthonyhilyard.itemborders.compat.LegendaryTooltipsHandler;
import com.mojang.datafixers.util.Pair;
import einstein.subtle_effects.mixin.client.ItemBordersConfigAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static com.anthonyhilyard.itemborders.ItemBordersConfig.getColor;

public class ItemBordersCompat {

    private static final String ITEM_BORDERS_COLORS_TAG = "itemborders_colors";

    public static TextColor getManualBorderColor(ItemStack stack) {
        Map<String, Object> manualBorders = ((ItemBordersConfigAccessor) ItemBordersConfigAccessor.getInstance()).getManualBorders().get().valueMap();

        for (String colorString : manualBorders.keySet()) {
            TextColor color = getColor(colorString);

            if (color != null) {
                Object object = manualBorders.get(colorString);

                if (matchesStack(stack, object)) {
                    return color;
                }
                else if (object instanceof List<?> list) {
                    for (Object obj : list) {
                        if (matchesStack(stack, obj)) {
                            return color;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean matchesStack(ItemStack stack, Object object) {
        if (object instanceof String string) {
            return Selectors.itemMatches(stack, string);
        }
        return false;
    }

    public static List<TextColor> getNBTBorderColor(ItemStack stack) {
        if (stack.hasTag()) {
            // noinspection all
            CompoundTag tag = stack.getTag();

            // noinspection all
            if (tag.contains(ITEM_BORDERS_COLORS_TAG)) {
                CompoundTag colorsTag = tag.getCompound(ITEM_BORDERS_COLORS_TAG);
                List<TextColor> colors = new ArrayList<>();

                if (colorsTag.contains("top", Tag.TAG_STRING)) {
                    // noinspection all
                    colors.add(getColor(colorsTag.getString("top")));
                }

                if (colorsTag.contains("bottom", Tag.TAG_STRING)) {
                    // noinspection all
                    colors.add(getColor(colorsTag.getString("bottom")));
                }

                return colors.stream().filter(Objects::nonNull).toList();
            }
        }

        // noinspection all
        if (CompatHelper.IS_LEGENDARY_TOOLTIPS_LOADED.get() && ItemBordersConfigAccessor.getInstance().legendaryTooltipsSync.get()) {
            Pair<Supplier<Integer>, Supplier<Integer>> borderColors = LegendaryTooltipsHandler.getBorderColors(stack);
            List<TextColor> colors = new ArrayList<>();
            colors.add(TextColor.fromRgb(borderColors.getFirst().get()));
            colors.add(TextColor.fromRgb(borderColors.getSecond().get()));
            return colors;
        }
        return List.of();
    }
}
