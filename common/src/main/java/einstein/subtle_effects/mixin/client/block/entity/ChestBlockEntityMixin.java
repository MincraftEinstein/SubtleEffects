package einstein.subtle_effects.mixin.client.block.entity;

import einstein.subtle_effects.util.ChestAccessor;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin implements ChestAccessor {

    @Override
    @Accessor("chestLidController")
    public abstract ChestLidController subtleEffects$getLidController();
}
