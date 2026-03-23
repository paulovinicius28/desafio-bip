package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;


@Schema(description = "Dados de um benefício")
public class BeneficioDTO {

    @Schema(description = "Identificador único do benefício", example = "1")
    private Long id;

    @Schema(description = "Nome do benefício", example = "Auxílio Alimentação", required = true)
    @NotBlank(message = "Nome do benefício é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Descrição do benefício", example = "Auxílio Alimentação", required = true)
    @NotBlank(message = "Nome do benefício é obrigatório")
    @Size(min = 1, max = 255, message = "Descrição deve ter entre 1 e 255 caracteres")
    private String descricao;

    @Schema(description = "Valor do benefício", example = "1500.00", required = true)
    @NotNull(message = "Valor do benefício é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    // Constructors
    public BeneficioDTO() {
    }

    public BeneficioDTO(Long id, String nome, String descricao, BigDecimal valor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
    }

    public BeneficioDTO(String nome, BigDecimal valor) {
        this.nome = nome;
        this.valor = valor;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "BeneficioDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                '}';
    }
}

