package pl.sda.refactoring.customers;

interface MailSender {

    void sendEmail(String address, String subj, String msg);
}
