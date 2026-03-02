package einstein.subtle_effects.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.neoforged.neoforge.client.ClientCommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    // work around for NeoForge not checking this method for client commands before sending to the server
    @Inject(method = "sendUnsignedCommand", at = @At("HEAD"), cancellable = true)
    private void onSendUnsignedCommand(String command, CallbackInfoReturnable<Boolean> cir) {
        if (command.equals("configure subtle_effects general")) {
            if (ClientCommandHandler.runCommand(command)) {
                cir.setReturnValue(true);
            }
        }
    }
}
