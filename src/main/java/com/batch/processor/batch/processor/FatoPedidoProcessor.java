package com.batch.processor.batch.processor;

import com.batch.processor.domain.dto.FatoPedidoDTO;
import com.batch.processor.domain.dto.FatoPedidoRaw;
import com.batch.processor.domain.service.TraducaoCategoriaService;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Component
@StepScope
public class FatoPedidoProcessor implements ItemProcessor<FatoPedidoRaw, FatoPedidoDTO> {

    private Map<String, String> traducoes;

    private final TraducaoCategoriaService traducaoService;

    public FatoPedidoProcessor(TraducaoCategoriaService traducaoService) {
        this.traducaoService = traducaoService;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.traducoes = traducaoService.carregarCategorias();
    }

    @Override
    public FatoPedidoDTO process(FatoPedidoRaw raw) throws Exception {
        return FatoPedidoDTO.builder()
                .idItemPedido(raw.getOrderItemId().toString())
                .idPedido(raw.getOrderId())
                .idClienteUnico(raw.getCustomerUniqueId())
                .idProduto(raw.getProductId())
                .idVendedor(raw.getSellerId())
                .cidadeCliente(raw.getCustomerCity())
                .estadoCliente(raw.getCustomerState())
                .categoriaProduto(raw.getProductCategoryName())
                .categoriaProdutoIngles(traducoes.get(raw.getProductCategoryName()))
                .dataCompra(raw.getPurchaseTimestamp())
                .dataAprovacao(raw.getApprovedAt())
                .dataEntregaTransportadora(raw.getDeliveredCarrierDate())
                .dataEntregaCliente(raw.getDeliveredCustomerDate())
                .dataEstimadaEntrega(raw.getEstimatedDeliveryDate())
                .diasEntrega(diferencaDatas(raw.getPurchaseTimestamp(),raw.getDeliveredCustomerDate()))
                .diasDiferencaEntregaEstimativa(diferencaDatas(raw.getDeliveredCustomerDate(),raw.getEstimatedDeliveryDate()))
                .precoItem(raw.getPrice())
                .valorFrete(raw.getFreightValue())
                .tipoPagamento(raw.getPaymentType())
                .parcelasPagamento(raw.getPaymentInstallments())
                .notaAvaliacao(raw.getReviewScore())
                .build();
    }

    private Integer diferencaDatas(LocalDateTime dt1, LocalDateTime dt2) {
        return (dt1 == null || dt2 == null)
                ? null
                : (int) Math.abs(ChronoUnit.DAYS.between(dt1, dt2));
    }

}
