package com.batch.processor.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter@Getter
public class FatoPedidoRaw {
    private String orderId;
    private Integer orderItemId;
    private String productId;
    private String sellerId;
    private BigDecimal price;
    private BigDecimal freightValue;
    private String customerId;
    private LocalDateTime purchaseTimestamp;
    private LocalDateTime approvedAt;
    private LocalDateTime deliveredCarrierDate;
    private LocalDateTime deliveredCustomerDate;
    private LocalDateTime estimatedDeliveryDate;
    private String customerUniqueId;
    private String customerCity;
    private String customerState;
    private String productCategoryName;
    private String productCategoryNameEnglish;
    private String paymentType;
    private Integer paymentInstallments;
    private Integer reviewScore;
}
