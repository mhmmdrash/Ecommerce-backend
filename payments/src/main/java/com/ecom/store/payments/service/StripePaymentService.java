package com.ecom.store.payments.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentService implements PaymentService {
    @Override
    public String createLink(String orderId, long amount) throws StripeException {
        Stripe.apiKey = "sk_test_51PSNINP46GLXSgbfreaPCwhm1SvNOqLG3htrP9oW5acS6xZqDrAktVS221a8LuFq4jAOTgtsIWjTdmPnbyBSt6JJ00M4BjmwiE";

        PriceCreateParams priceCreateParams = new PriceCreateParams.Builder()
                .setCurrency("USD")
                .setUnitAmount(1000L)
                .setProductData(PriceCreateParams.ProductData.builder().setName(orderId).build())
                .build();

        Price price = Price.create(priceCreateParams);

        PaymentLinkCreateParams linkCreateParams = new PaymentLinkCreateParams.Builder()
                .addLineItem(
                        PaymentLinkCreateParams.LineItem.builder()
                                .setPrice(price.getId())
                                .setQuantity(1L)
                                .build()
                )
                .setAfterCompletion(
                        PaymentLinkCreateParams.AfterCompletion.builder()
                                .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                .setRedirect(
                                        PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                .setUrl("http://localhost:8080/afterCompletion")
                                                .build()
                                )
                                .build()
                )
                .build();

        PaymentLink link = PaymentLink.create(linkCreateParams);
        return link.getUrl();
    }
}
