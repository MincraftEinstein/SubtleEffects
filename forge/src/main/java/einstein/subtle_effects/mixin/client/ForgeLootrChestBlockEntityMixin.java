package einstein.subtle_effects.mixin.client;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import einstein.subtle_effects.util.ChestAccessor;
import net.minecraft.world.level.block.entity.ChestLidController;
import noobanidus.mods.lootr.block.entities.LootrChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfModLoaded("lootr")
@Mixin(LootrChestBlockEntity.class)
public abstract class ForgeLootrChestBlockEntityMixin implements ChestAccessor {

    @Override
    @Accessor("chestLidController")
    public abstract ChestLidController subtleEffects$getLidController();
}
