package einstein.subtle_effects.mixin.client.block.entity;

import einstein.subtle_effects.util.ChestAccessor;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin implements ChestAccessor {

    @Shadow
    @Final
    private ChestLidController chestLidController;

    @Override
    public ChestLidController subtleEffects$getLidController() {
        return chestLidController;
    }
}
