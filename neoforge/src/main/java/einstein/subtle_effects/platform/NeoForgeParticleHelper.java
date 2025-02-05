package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.ParticleHelper;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Rarity;

public class NeoForgeParticleHelper implements ParticleHelper {

    @Override
    public TextColor getRarityColor(Rarity rarity) {
        return rarity.getStyleModifier().apply(Style.EMPTY).getColor();
    }
}
