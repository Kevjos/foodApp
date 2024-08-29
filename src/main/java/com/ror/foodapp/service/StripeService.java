package com.ror.foodapp.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    /*
    public StripeService() {
        Stripe.apiKey = stripeApiKey;
    }
    */


    @PostConstruct
    public void init() {
        Stripe.apiKey = this.stripeApiKey; // Aquí es donde se configura la clave API
    }

    public PaymentIntent createPaymentIntent(double amount) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100)) // Stripe usa centavos
                .setCurrency("eur")
                .build();

        return PaymentIntent.create(params);
    }
}

