package com.ecom.store.payments.service;

import com.stripe.exception.StripeException;

public interface PaymentService {
    String createLink(String orderId, long amount) throws StripeException;
}
