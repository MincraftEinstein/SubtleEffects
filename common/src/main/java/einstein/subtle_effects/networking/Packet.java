package einstein.subtle_effects.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface Packet {

    void encode(FriendlyByteBuf buf);

    void handle(@Nullable ServerPlayer player);

    ResourceLocation id();
}