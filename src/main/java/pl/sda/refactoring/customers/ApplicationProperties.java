package pl.sda.refactoring.customers;

import java.io.File;

/**
 * Good singleton example
 */
final class ApplicationProperties {

    private static final ApplicationProperties PROPERTIES;

    static {
        PROPERTIES = new ApplicationProperties();
    }

    private ApplicationProperties() {
    }

    public static ApplicationProperties getInstance() {
        return PROPERTIES;
    }
}
