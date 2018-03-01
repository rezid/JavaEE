package fr.upec.m2.projects.JavaEE.business.email;

import java.util.Map;

public class VoteEmailService extends EmailService {

    @Override
    public void sendTemplateMessage(EmailTemplate templateEmail, Map<String, Object> messageParameters, Map<String, String> templateContent) {
        // TODO: implement.
        System.out.println("VoteEmailService.sendTemplateMessage()");
        System.out.println("Call to action URL: " + templateEmail.getCallToActionURL());
        System.out.println("Message parameters: " + messageParameters);
        System.out.println("Template content: " + templateContent);
    }

    @Override
    public void sendPlainTextMessage(EmailUser to, EmailUser from, String subject, String body, String replyTo) {
        // TODO: implement.
        System.out.println("VoteEmailService.sendPlainTextMessage()");
        System.out.println("To: " + to);
        System.out.println("From: " + from);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("Reply to: " + replyTo);
    }

}
