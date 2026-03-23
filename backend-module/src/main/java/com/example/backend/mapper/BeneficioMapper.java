package com.example.backend.mapper;

import com.example.backend.dto.BeneficioDTO;
import com.example.common.entity.Beneficio;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class BeneficioMapper {

    /**
     * Converte entidade Beneficio para BeneficioDTO
     */
    public BeneficioDTO toDTO(Beneficio beneficio) {
        if (beneficio == null) {
            return null;
        }

        return new BeneficioDTO(
                beneficio.getId(),
                beneficio.getNome(),
                beneficio.getDescricao(),
                beneficio.getValor()
        );
    }

    /**
     * Converte BeneficioDTO para entidade Beneficio
     */
    public Beneficio toEntity(BeneficioDTO beneficioDTO) {
        if (beneficioDTO == null) {
            return null;
        }

        Beneficio beneficio = new Beneficio();
        beneficio.setId(beneficioDTO.getId());
        beneficio.setNome(beneficioDTO.getNome());
        beneficio.setDescricao(beneficioDTO.getDescricao());
        beneficio.setValor(beneficioDTO.getValor());

        return beneficio;
    }

    /**
     * Converte lista de entidades para lista de DTOs
     */
    public List<BeneficioDTO> toDTOList(List<Beneficio> beneficios) {
        if (beneficios == null) {
            return null;
        }

        return beneficios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
