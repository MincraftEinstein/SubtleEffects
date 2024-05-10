package einstein.ambient_sleep.util;

import net.minecraft.resources.ResourceLocation;

public interface ShaderManager {

    void ambientSleep$loadShader(ResourceLocation location);

    void ambientSleep$clearShader();
}
