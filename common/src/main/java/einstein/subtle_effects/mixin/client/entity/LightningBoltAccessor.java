package einstein.subtle_effects.mixin.client.entity;

import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LightningBolt.class)
public interface LightningBoltAccessor {

    @Accessor("life")
    int getLife();
}
