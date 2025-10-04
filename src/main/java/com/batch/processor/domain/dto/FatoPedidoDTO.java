package com.batch.processor.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Setter@Getter
@NoArgsConstructor@AllArgsConstructor
public class FatoPedidoDTO {

    private String idItemPedido;
    private String idPedido;
    private String idClienteUnico;
    private String idProduto;
    private String idVendedor;
    private String cidadeCliente;
    private String estadoCliente;
    private String categoriaProduto;
    private String categoriaProdutoIngles;
    private LocalDateTime dataCompra;
    private LocalDateTime dataAprovacao;
    private LocalDateTime dataEntregaTransportadora;
    private LocalDateTime dataEntregaCliente;
    private LocalDateTime dataEstimadaEntrega;
    private Integer diasEntrega;
    private Integer diasDiferencaEntregaEstimativa;
    private BigDecimal precoItem;
    private BigDecimal valorFrete;
    private String tipoPagamento;
    private Integer parcelasPagamento;
    private Integer notaAvaliacao;

}
