package com.example.backend.service;

import com.example.common.entity.Beneficio;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IBeneficioEjbService {

    /**
     * Lista todos os benefícios registrados
     * 
     * @return Lista com todos os benefícios
     */
    List<Beneficio> findAll();

    /**
     * Busca um benefício pelo ID
     *
     * @param id ID do benefício
     * @return Benefício encontrado
     * @throws RuntimeException se benefício não for encontrado
     */
    Optional<Beneficio> findById(Long id);

    /**
     * Salva um novo benefício
     * 
     * @param beneficio Dados do benefício a ser criado
     * @return Benefício criado
     * @throws IllegalArgumentException se dados forem inválidos
     */
    Beneficio save(Beneficio beneficio);

    /**
     * Atualiza um benefício existente
     * 
     * @param beneficio Dados do benefício a ser atualizado
     * @return Benefício atualizado
     * @throws RuntimeException se benefício não for encontrado
     * @throws IllegalArgumentException se dados forem inválidos
     */
    Beneficio update(Beneficio beneficio);

    /**
     * Deleta um benefício
     * 
     * @param id ID do benefício a ser deletado
     * @throws RuntimeException se benefício não for encontrado
     */
    void delete(Long id);

    /**
     * Realiza transferência de valor entre dois benefícios
     * 
     * @param fromId ID do benefício origem
     * @param toId ID do benefício destino
     * @param amount Valor a transferir
     * @throws RuntimeException se saldo insuficiente ou erro inesperado
     */
    void transfer(Long fromId, Long toId, BigDecimal amount);
}
