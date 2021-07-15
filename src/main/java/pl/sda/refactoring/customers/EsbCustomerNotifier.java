package pl.sda.refactoring.customers;

final class EsbCustomerNotifier implements CustomerNotifier {

    @Override
    public void notifyRegistration(RegistrationNotification notification) {
        System.out.println("send to ESB system");
    }
}
