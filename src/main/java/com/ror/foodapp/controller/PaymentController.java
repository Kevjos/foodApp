package com.ror.foodapp.controller;

import com.ror.foodapp.model.Dish;
import com.ror.foodapp.model.Order;
import com.ror.foodapp.repository.DishRepository;
import com.ror.foodapp.repository.OrderRepository;
import com.ror.foodapp.service.EmailService;
import com.ror.foodapp.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*
@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private OrderRepository orderRepository;

    @Value("${stripe.api.key}")
    private String apiKey;

    public PaymentController() {
        // Configurar la API Key de Stripe
        Stripe.apiKey = apiKey;
    }

    @PostMapping("/create-checkout-session")
    public Map<String, Object> createCheckoutSession(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Obtener los detalles del pedido
            String fullName = (String) payload.get("fullName");
            String phone = (String) payload.get("phone");
            String email = (String) payload.get("email");
            Long dishId = Long.parseLong((String) payload.get("dishId"));
            int quantity = Integer.parseInt((String) payload.get("quantity"));

            // Buscar el plato en la base de datos
            Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new RuntimeException("Dish not found"));

            // Crear una sesión de checkout
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/payment/confirmation?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:8080/payment/cancel")
                    .setCustomerEmail(email)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity((long) quantity)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("eur")
                                                    .setUnitAmount((long) (dish.getPrice() * 100)) // Stripe usa centavos
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(dish.getName())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            response.put("sessionId", session.getId());
            response.put("success", true);

        } catch (StripeException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @GetMapping("/confirmation")
    public String confirmOrder(@RequestParam("session_id") String sessionId, Model model) {
        try {
            Session session = Session.retrieve(sessionId);

            if ("payment_intent.succeeded".equals(session.getPaymentIntent())) {
                // Aquí puedes registrar la orden en la base de datos, ya que el pago fue exitoso
                // Buscar la orden y marcarla como pagada


                // Redirigir a la página de confirmación
                return "order-confirmation";
            } else {
                // Manejar pagos fallidos
                return "error";
            }
        } catch (StripeException e) {
            model.addAttribute("error", "Failed to confirm payment");
            return "error";
        }
    }

    @GetMapping("/cancel")
    public String showCancel() {
        return "cancel";
    }

}
*/

//===========================================
@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Value("${stripe.api.key}")
    private String apiKey;

    public PaymentController() {
        // Configurar la API Key de Stripe
        Stripe.apiKey = apiKey;
    }

    @PostMapping("/create-checkout-session")
    @ResponseBody
    public Map<String, Object> createCheckoutSession(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Obtener los detalles del pedido
            String fullName = (String) payload.get("fullName");
            String phone = (String) payload.get("phone");
            String email = (String) payload.get("email");
            Long dishId = Long.parseLong((String) payload.get("dishId"));
            int quantity = Integer.parseInt((String) payload.get("quantity"));

            // Buscar el plato en la base de datos
            Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new RuntimeException("Dish not found"));

            // Crear una sesión de checkout
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/payment/confirmation?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:8080/payment/cancel")
                    .setCustomerEmail(email)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity((long) quantity)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("eur")
                                                    .setUnitAmount((long) (dish.getPrice() * 100)) // Stripe usa centavos
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(dish.getName())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .putMetadata("fullName", fullName)
                    .putMetadata("phone", phone)
                    .putMetadata("dishId", dishId.toString())  // Almacenar dishId como String
                    .putMetadata("quantity", String.valueOf(quantity))  // Almacenar cantidad como String
                    .build();

            Session session = Session.create(params);
            response.put("sessionId", session.getId());
            response.put("success", true);

        } catch (StripeException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @GetMapping("/confirmation")
    public String confirmOrder(@RequestParam("session_id") String sessionId, Model model) {
        try {
            // Obtener la sesión de pago de Stripe
            Session session = Session.retrieve(sessionId);
            String paymentIntentId = session.getPaymentIntent();

            // Obtener el PaymentIntent asociado
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            // Verificar el estado del PaymentIntent
            if ("succeeded".equals(paymentIntent.getStatus())) {
                // Aquí puedes registrar la orden en la base de datos, ya que el pago fue exitoso
                String email = session.getCustomerEmail();
                Long dishId = Long.parseLong(session.getMetadata().get("dishId"));
                int quantity = Integer.parseInt(session.getMetadata().get("quantity"));
                String fullName = session.getMetadata().get("fullName");
                String phone = session.getMetadata().get("phone");

                Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new RuntimeException("Dish not found"));

                // Crear la orden en la base de datos
                Order order = new Order();
                order.setFullName(fullName);
                order.setPhone(phone);
                order.setEmail(email);
                order.setDish(dish);
                order.setQuantity(quantity);
                order.setOrderDate(LocalDate.from(LocalDateTime.now())); // Fecha actual
                order.setAmount(paymentIntent.getAmountReceived() / 100.0); // Convertir de centavos a unidades
                order.setStripeSessionId(sessionId);

                orderRepository.save(order);

                boolean emailSent = emailService.sendOrderConfirmationEmail(order.getEmail(), order);
                // Añadir el objeto 'order' al modelo
                model.addAttribute("order", order);

                if (emailSent) {
                    model.addAttribute("message", "Su pedido ha sido confirmado y el correo de confirmación ha sido enviado.");
                    return "order-confirmation";
                } else {
                    model.addAttribute("message", "Su pedido ha sido confirmado, pero hubo un problema al enviar el correo de confirmación.");
                    return "order-confirmation"; // Podrías redirigir a una página de error si prefieres
                }
                //return "redirect:/payment/confirmation";
            } else {
                // Manejar pagos fallidos
                model.addAttribute("error", "El pago no se pudo completar.");
                return "error";
            }
        } catch (StripeException e) {
            model.addAttribute("error", "Fallo al confirmar el pago:" + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/cancel")
    public String showCancel() {
        return "cancel";
    }
}