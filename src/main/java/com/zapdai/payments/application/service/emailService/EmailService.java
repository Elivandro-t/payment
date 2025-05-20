package com.zapdai.payments.application.service.emailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender= javaMailSender;
    }

    @Async
    public void emailSend(String email, BigDecimal valor,String msg) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("zapdaicompany@gmail.com", "Equipe ZAPDAI");
            helper.setTo(email);
            helper.setSubject(msg);

            String htmlContent = """
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <style>
        .container {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 20px;
        }
        .title {
            color: orange;
            font-size: 22px;
            margin-bottom: 20px;
        }
        .button {
            display: inline-block;
            margin: 10px;
            padding: 15px 30px;
            background-color: orange;
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-size: 16px;
        }
        .secondary-button {
            display: inline-block;
            margin: 10px;
            padding: 12px 28px;
            background-color: white;
            color: orange;
            border: 2px solid orange;
            text-decoration: none;
            border-radius: 25px;
            font-size: 16px;
        }
        .footer {
            font-size: 12px;
            color: #666;
            margin-top: 30px;
        }
    </style>
</head>
<body>
    <div class="container">
        <img src="https://upload.wikimedia.org/wikipedia/commons/1/14/Kwai_logo.png" alt="Logo" width="100"/>
        <div class="title">
           Sua Seu pagamento foi criado com sucesso!<br>
           Valor: %s <br>
           Email: %s <br>
            Clique para ver >>>
        </div>
          <a href="http://localhost:8080/empresas" class="secondary-button">Acesse o app</a>
        
        <div class="footer">
            Você recebeu este e-mail ao criar sua conta na plataforma.<br>
            Se não quiser mais receber, clique <a href="#">aqui</a> para cancelar.
        </div>
    </div>
</body>
</html>
""";
            String htmlContents = String.format(htmlContent, valor, email);

            helper.setText(htmlContents, true); // true para ser HTML
            javaMailSender.send(mimeMessage);
        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

}
