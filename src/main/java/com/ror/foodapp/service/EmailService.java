package com.ror.foodapp.service;

import com.ror.foodapp.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    public boolean sendOrderConfirmationEmail(String to, Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de compra");
        message.setText(createOrderEmailBody(order));

        try {
            emailSender.send(message);
            return true;  // Email enviado con éxito
        } catch (MailException e) {
            // Registrar el error con detalles específicos
            logger.error("Error enviando el correo de confirmación al cliente: " + to, e);
            return false;  // Hubo un error al enviar el correo
        }
    }

    private String createOrderEmailBody(Order order) {
        return "Hola " + order.getFullName() + ",\n\n" +
                "Gracias por su compra. A continuación encontrará el detalle de su compra:\n\n" +
                "Fecha de compra: " + order.getOrderDate() + "\n" +
                "Plato: " + order.getDish().getName() + "\n" +
                "Cantidad: " + order.getQuantity() + "\n" +
                "Precio total: " + (order.getQuantity() * order.getDish().getPrice()) + " €\n\n" +
                "Bendiciones,\n" +
                "Iglesia Restaurados para restaurar";
    }
}


