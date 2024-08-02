package com.Java020.MonieFlex;

import com.Java020.MonieFlex.domain.enums.EmailTemplateName;
import com.Java020.MonieFlex.service.impl.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {
    private JavaMailSender mailSender;
    private SpringTemplateEngine templateEngine;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        mailSender = Mockito.mock(JavaMailSender.class);
        templateEngine = Mockito.mock(SpringTemplateEngine.class);
        emailService = new EmailService(mailSender, templateEngine);
    }

    @Test
    void testSendEmail() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Processed Template");

        String to = "test@example.com";
        String username = "testuser";
        EmailTemplateName emailTemplate = EmailTemplateName.ACTIVATE_ACCOUNT;
        String subject = "Account Activation";

        Map<String, Object> properties = new HashMap<>();
        properties.put("confirmationUrl", "http://example.com");
        properties.put("activation_code", "123456");

        emailService.sendEmail(to, username, emailTemplate, subject, properties);

        ArgumentCaptor<String> templateNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);

        verify(templateEngine, times(1)).process(templateNameCaptor.capture(), contextCaptor.capture());
        assertEquals("ACTIVATE_ACCOUNT", templateNameCaptor.getValue());

        Context capturedContext = contextCaptor.getValue();
        assertEquals(username, capturedContext.getVariable("username"));
        assertEquals("http://example.com", capturedContext.getVariable("confirmationUrl"));
        assertEquals("123456", capturedContext.getVariable("activation_code"));

        verify(mailSender, times(1)).send(mimeMessage);
    }
}
