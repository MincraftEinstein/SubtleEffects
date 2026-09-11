package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.IPlatformHelper;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public Platform getPlatform() {
        return Platform.FORGE;
    }
}