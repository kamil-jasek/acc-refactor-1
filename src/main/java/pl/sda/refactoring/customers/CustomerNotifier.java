package pl.sda.refactoring.customers;

import java.util.UUID;

interface CustomerNotifier {

    class RegistrationNotification {
        private final UUID id;
        private final String name;
        private final String email;
        private final boolean verified;

        public RegistrationNotification(UUID id, String name, String email, boolean verified) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.verified = verified;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public boolean isVerified() {
            return verified;
        }
    }

    void notifyRegistration(RegistrationNotification notification);
}
