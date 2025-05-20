package com.zapdai.payments.infra;

import com.zapdai.payments.application.service.emailService.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class ConfigurationPayment {
    @Bean
    public EmailService serviceEmail(JavaMailSender javaMailSender){
        return new EmailService(javaMailSender);
    }
}
