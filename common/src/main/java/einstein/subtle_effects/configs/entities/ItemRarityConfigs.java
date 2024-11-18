package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.ItemRarityParticle;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import static einstein.subtle_effects.init.ModConfigs.BASE_KEY;
import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.itemRarity")
public class ItemRarityConfigs extends ConfigSection {

    public DisplayType particlesDisplayType = DisplayType.ON;
    public ColorType particleColor = ColorType.NAME_COLOR;
    public boolean mixedColorName = true;
    @ValidatedInt.Restrict(min = 3, max = 15)
    public int particleMaxHeight = 7;

    public enum DisplayType implements EnumTranslatable {
        OFF,
        ON,
        NOT_COMMON;

        public boolean test(ItemEntity itemEntity) {
            if (this == OFF) {
                return false;
            }

            ItemStack stack = itemEntity.getItem();
            if (stack.getRarity() == Rarity.COMMON) {
                if (ENTITIES.itemRarity.particlesDisplayType == ItemRarityConfigs.DisplayType.NOT_COMMON) {
                    return ENTITIES.itemRarity.particleColor == ColorType.NAME_COLOR && ItemRarityParticle.getItemNameColor(stack, itemEntity.getRandom()) != null;
                }
            }
            return true;
        }

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "entities.itemRarity.particlesDisplayType";
        }
    }

    public enum ColorType implements EnumTranslatable {
        RARITY_COLOR,
        NAME_COLOR;

        @NotNull
        @Override
        public String prefix() {
            return BASE_KEY + "entities.itemRarity.particleColor";
        }
    }
}
