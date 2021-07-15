package pl.sda.refactoring.customers;

class SmsCustomerNotifier implements CustomerNotifier {

    @Override
    public void notifyRegistration(RegistrationNotification notification) {
        System.out.println("send sms");
    }
}
