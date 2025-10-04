package com.batch.processor.domain.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TraducaoCategoriaService {

    private final JdbcTemplate jdbcTemplate;
    public TraducaoCategoriaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, String> carregarCategorias() {
        return jdbcTemplate.query(
                "SELECT product_category_name AS categoria_pt, product_category_name_english AS categoria_en " +
                        "FROM product_category_name_translation",
                rs -> {
                    Map<String, String> map = new HashMap<>();
                    while (rs.next()) {
                        map.put(rs.getString("categoria_pt"), rs.getString("categoria_en"));
                    }
                    return map;
                });
    }
}
