O Batch Processor tem como objetivo processar uma base de dados volumosa utilizando Java e Spring Batch, gerando informações relevantes a partir do tratamento dos dados.
Após o processamento, é possível visualizar os logs detalhados de cada etapa da aplicação por meio do stack ELK (Elasticsearch, Logstash e Kibana).

A base de dados escolhida foi o Brazilian E-Commerce Public Dataset by Olist, disponível no Kaggle Datasets. Link: https://www.kaggle.com/datasets/olistbr/brazilian-ecommerce
.
As planilhas originais em formato CSV foram importadas para um banco de dados MySQL.

A aplicação foi desenvolvida para ler os dados dessa base e popular uma nova, contendo apenas as informações que agregam valor e podem ser úteis para gestores e analistas de negócios.

Durante o desenvolvimento, busquei manter baixa complexidade ciclomática nas funções, visando melhor desempenho, legibilidade e manutenibilidade do código.
