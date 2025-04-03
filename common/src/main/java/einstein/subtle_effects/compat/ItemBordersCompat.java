package einstein.subtle_effects.compat;

import com.anthonyhilyard.iceberg.util.Selectors;
import com.anthonyhilyard.itemborders.compat.LegendaryTooltipsHandler;
import com.anthonyhilyard.itemborders.config.ItemBordersConfig;
import com.mojang.datafixers.util.Pair;
import einstein.subtle_effects.mixin.client.ItemBordersConfigAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Supplier;

import static com.anthonyhilyard.itemborders.config.ItemBordersConfig.getColor;

public class ItemBordersCompat {

    private static final String ITEM_BORDERS_COLORS_TAG = "itemborders_colors";

    public static TextColor getManualBorderColor(Level level, ItemStack stack) {
        Map<String, Object> manualBorders = ((ItemBordersConfigAccessor) ItemBordersConfig.getInstance()).getManualBorders().get();
        RegistryAccess access = level.registryAccess();

        for (String colorString : manualBorders.keySet()) {
            TextColor color = getColor(colorString);

            if (color != null) {
                Object object = manualBorders.get(colorString);

                if (matchesStack(access, stack, object)) {
                    return color;
                }
                else if (object instanceof List<?> list) {
                    for (Object obj : list) {
                        if (matchesStack(access, stack, obj)) {
                            return color;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean matchesStack(RegistryAccess access, ItemStack stack, Object object) {
        if (object instanceof String string) {
            return Selectors.itemMatches(stack, string, access);
        }
        return false;
    }

    public static List<TextColor> getNBTBorderColor(ItemStack stack) {
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            // noinspection all
            CompoundTag tag = stack.get(DataComponents.CUSTOM_DATA).copyTag();

            if (tag.contains(ITEM_BORDERS_COLORS_TAG)) {
                Optional<CompoundTag> colorsTagOptional = tag.getCompound(ITEM_BORDERS_COLORS_TAG);

                if (colorsTagOptional.isPresent()) {
                    CompoundTag colorsTag = colorsTagOptional.get();
                    List<TextColor> colors = new ArrayList<>();

                    if (colorsTag.contains("top")) {
                        // noinspection all
                        colors.add(getColor(colorsTag.getString("top")));
                    }

                    if (colorsTag.contains("bottom")) {
                        // noinspection all
                        colors.add(getColor(colorsTag.getString("bottom")));
                    }

                    return colors.stream().filter(Objects::nonNull).toList();
                }

            }
        }

        if (CompatHelper.IS_LEGENDARY_TOOLTIPS_LOADED.get() && ItemBordersConfig.getInstance().legendaryTooltipsSync.get()) {
            Pair<Supplier<Integer>, Supplier<Integer>> borderColors = LegendaryTooltipsHandler.getBorderColors(stack);
            if (borderColors != null) {
                List<TextColor> colors = new ArrayList<>();
                colors.add(TextColor.fromRgb(borderColors.getFirst().get()));
                colors.add(TextColor.fromRgb(borderColors.getSecond().get()));
                return colors;
            }
        }
        return List.of();
    }
}
