package com.batch.processor.batch;

import com.batch.processor.batch.listener.JobLogListener;
import com.batch.processor.domain.dto.FatoPedidoDTO;
import com.batch.processor.domain.dto.FatoPedidoRaw;
import com.batch.processor.domain.mapper.FatoPedidoRawRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableTransactionManagement
public class GerarPedidosJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager appTransactionManager;
    private final ItemProcessor<FatoPedidoRaw, FatoPedidoDTO> fatoPedidoProcessor;
    private final JobLogListener jobListener;

    public GerarPedidosJobConfig(@Qualifier("jobRepository") JobRepository jobRepository,@Qualifier("appTransactionManager") PlatformTransactionManager appTransactionManager,
                                 JobLogListener jobListener, ItemProcessor<FatoPedidoRaw, FatoPedidoDTO> fatoPedidoProcessor) {
        this.jobRepository = jobRepository;
        this.appTransactionManager = appTransactionManager;
        this.jobListener = jobListener;
        this.fatoPedidoProcessor = fatoPedidoProcessor;
    }

    @Bean
    public Job legadoToFato(Step fromLegadoToFato) {
        return new JobBuilder("legadoToFatoJob", jobRepository)
                .listener(jobListener)
                .incrementer(new RunIdIncrementer())
                .start(fromLegadoToFato)
                .build();
    }

    @Bean
    public Step fromLegadoToFato(JdbcPagingItemReader<FatoPedidoRaw> fatoPedidoReader,
                                 JdbcBatchItemWriter<FatoPedidoDTO> fatoPedidoWriter) {
        return new StepBuilder("fromLegadoToFato", jobRepository)
                .<FatoPedidoRaw, FatoPedidoDTO>chunk(500, appTransactionManager)
                .reader(fatoPedidoReader)
                .processor(fatoPedidoProcessor)
                .writer(fatoPedidoWriter)
                .listener(jobListener)
                .build();
    }



    @Bean
    public JdbcPagingItemReader<FatoPedidoRaw> fatoPedidoReader(
            @Qualifier("appDataSource") DataSource appDataSource) throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(appDataSource);
        queryProvider.setSelectClause("""
            oi.order_id, oi.order_item_id, oi.product_id, oi.seller_id, oi.price, oi.freight_value,
            o.customer_id, o.order_purchase_timestamp, o.order_approved_at,
            o.order_delivered_carrier_date, o.order_delivered_customer_date, o.order_estimated_delivery_date,
            c.customer_unique_id, c.customer_city, c.customer_state,
            p.product_category_name,
            pay.payment_type, pay.payment_installments,
            r.review_score
    """);
        queryProvider.setFromClause("""
            olist_order_items_dataset oi
            JOIN olist_orders_dataset o ON oi.order_id = o.order_id
            JOIN olist_customers_dataset c ON o.customer_id = c.customer_id
            LEFT JOIN olist_products_dataset p ON oi.product_id = p.product_id
            LEFT JOIN olist_order_payments_dataset pay ON oi.order_id = pay.order_id
            LEFT JOIN olist_order_reviews_dataset r ON oi.order_id = r.order_id
    """);

        Map<String, Order> sortKeys = new LinkedHashMap<>();
        sortKeys.put("oi.order_id", Order.ASCENDING);
        sortKeys.put("oi.order_item_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return new JdbcPagingItemReaderBuilder<FatoPedidoRaw>()
                .name("fatoPedidoReader")
                .dataSource(appDataSource)
                .queryProvider(queryProvider.getObject())
                .pageSize(500)
                .rowMapper(new FatoPedidoRawRowMapper())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<FatoPedidoDTO> fatoPedidoWriter(@Qualifier("novoDataSource") DataSource novoDataSource) {
        String sql = """
                    INSERT INTO fato_pedidos (id_item_pedido, id_pedido, id_cliente_unico, id_produto, id_vendedor,
                                            cidade_cliente, estado_cliente, categoria_produto, categoria_produto_ingles,
                                            data_compra, data_aprovacao, data_entrega_transportadora, data_entrega_cliente, data_estimada_entrega,
                                            tempo_entrega_dias, diferenca_dias_entrega_estimada,
                                            preco_item, valor_frete, tipo_pagamento, parcelas_pagamento, nota_avaliacao)
                                            VALUES (:itemPedido, :idPedido, :idClienteUnico, :idProduto, :idVendedor,
                                            :cidadeCliente, :estadoCliente, :categoriaProduto, :categoriaProdutoIngles,
                                            :dataCompra, :dataAprovacao, :dataEntregaTransportadora, :dataEntregaCliente, :dataEstimadaEntrega,
                                            :diasEntrega, :diasDiferencaEntregaEstimativa,
                                            :precoItem, :valorFrete, :tipoPagamento, :parcelasPagamento, :notaAvaliacao)
                """;
        return new JdbcBatchItemWriterBuilder<FatoPedidoDTO>()
                .dataSource(novoDataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
}
