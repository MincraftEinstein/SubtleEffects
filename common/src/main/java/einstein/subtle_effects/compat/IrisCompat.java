package einstein.subtle_effects.compat;

import net.irisshaders.iris.Iris;

public class IrisCompat {

    public static boolean areShadersEnabled() {
        return Iris.getCurrentPack().isPresent();
    }
}
