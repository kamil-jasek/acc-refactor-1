package pl.sda.refactoring.customers;

interface MailSender {

    boolean sendEmail(String address, String subj, String msg);
}
