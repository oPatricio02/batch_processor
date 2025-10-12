package com.batch.processor.domain.mapper;

import com.batch.processor.domain.dto.FatoPedidoRaw;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FatoPedidoRawRowMapper implements RowMapper<FatoPedidoRaw> {

    @Override
    public FatoPedidoRaw mapRow(ResultSet rs, int rowNum) throws SQLException {
        FatoPedidoRaw raw = new FatoPedidoRaw();
        raw.setOrderId(rs.getString("order_id"));
        raw.setOrderItemId(rs.getInt("order_item_id"));
        raw.setProductId(rs.getString("product_id"));
        raw.setSellerId(rs.getString("seller_id"));
        raw.setPrice(rs.getBigDecimal("price"));
        raw.setFreightValue(rs.getBigDecimal("freight_value"));
        raw.setCustomerId(rs.getString("customer_id"));
        raw.setPurchaseTimestamp(rs.getTimestamp("order_purchase_timestamp").toLocalDateTime());
        raw.setApprovedAt(getDataTime("order_approved_at", rs));
        raw.setDeliveredCarrierDate(getDataTime("order_delivered_carrier_date",rs));
        raw.setDeliveredCustomerDate(getDataTime("order_delivered_customer_date", rs));
        raw.setEstimatedDeliveryDate(getDataTime("order_estimated_delivery_date", rs));
        raw.setCustomerUniqueId(rs.getString("customer_unique_id"));
        raw.setCustomerCity(rs.getString("customer_city"));
        raw.setCustomerState(rs.getString("customer_state"));
        raw.setProductCategoryName(rs.getString("product_category_name"));
        raw.setPaymentType(rs.getString("payment_type"));
        raw.setPaymentInstallments(rs.getInt("payment_installments"));
        raw.setReviewScore(rs.getInt("review_score"));
        return raw;
    }

    private LocalDateTime getDataTime(String campo, ResultSet rs) throws SQLException {
        return rs.getTimestamp(campo) != null
                ? rs.getTimestamp(campo).toLocalDateTime()
                : null;
    }

}
