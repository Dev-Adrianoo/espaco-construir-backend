package br.com.espacoconstruir.tutoring_backend.service; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service 
public class EmailService {

    @Autowired
    private JavaMailSender mailSender; 

    public void sendPasswordResetEmail(String to, String link) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            
            message.setFrom("desenvolver.instituto.sender@gmail.com");
            message.setTo(to);
            message.setSubject("Redefinição de Senha - Desenvolver Instituto");
            message.setText("Olá,\n\nVocê solicitou a redefinição de sua senha. Clique no link abaixo para continuar:\n" + link + "\n\nSe você não solicitou isso, por favor, ignore este e-mail.");
            
            mailSender.send(message);
            System.out.println("E-mail de redefinição enviado com sucesso para: " + to);

        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail de redefinição via SMTP: " + e.getMessage());
            throw new RuntimeException("Falha ao enviar e-mail de recuperação.", e);
            
        }
    }
}