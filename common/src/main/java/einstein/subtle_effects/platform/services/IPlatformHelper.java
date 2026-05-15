package einstein.subtle_effects.platform.services;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    Platform getPlatform();

    enum Platform {
        FABRIC("fabric"),
        FORGE("forge"),
        NEOFORGE("neoforge");

        private final String name;

        Platform(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean isForgeLike() {
            return this == NEOFORGE || this == FORGE;
        }
    }
}