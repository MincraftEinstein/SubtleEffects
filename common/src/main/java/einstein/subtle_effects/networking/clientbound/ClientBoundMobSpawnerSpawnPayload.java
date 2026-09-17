package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.SubtleEffects;
import me.fzzyhmstrs.fzzy_config.networking.FzzyPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public record ClientBoundMobSpawnerSpawnPayload(BlockPos pos) implements FzzyPayload {

    public static final ResourceLocation ID = SubtleEffects.loc("mob_spawner_spawn");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static ClientBoundMobSpawnerSpawnPayload read(FriendlyByteBuf buf) {
        return new ClientBoundMobSpawnerSpawnPayload(buf.readBlockPos());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
