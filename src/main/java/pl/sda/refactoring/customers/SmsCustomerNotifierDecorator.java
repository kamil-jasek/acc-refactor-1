package pl.sda.refactoring.customers;

final class SmsCustomerNotifierDecorator extends SmsCustomerNotifier {

    private final CustomerNotifier nextNotifier;

    SmsCustomerNotifierDecorator(CustomerNotifier nextNotifier) {
        if (nextNotifier instanceof SmsCustomerNotifier) {
            throw new IllegalArgumentException("cannot be sms notifier");
        }
        this.nextNotifier = nextNotifier;
    }

    @Override
    public void notifyRegistration(RegistrationNotification notification) {
        super.notifyRegistration(notification);
        nextNotifier.notifyRegistration(notification);
    }
}
