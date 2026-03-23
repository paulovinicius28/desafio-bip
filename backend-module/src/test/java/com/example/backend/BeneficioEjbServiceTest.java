package com.example.backend;

import com.example.common.entity.Beneficio;
import com.example.common.repository.BeneficioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BeneficioEjbServiceTest {

    @Mock
    private BeneficioRepository beneficioRepository;

    @InjectMocks
    private com.example.backend.service.BeneficioEjbService beneficioEjbService;


    @Test
    void findByIdOk() {
        Beneficio beneficio = criarBeneficio();

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficio));

        Optional<Beneficio> resultado = beneficioEjbService.findById(1L);

        assertNotNull(resultado);

        verify(beneficioRepository, times(1)).findById(1L);
    }

    @Test
    void createOk() {
        Beneficio beneficio = criarBeneficio();

        when(beneficioRepository.save(any(Beneficio.class))).thenReturn(beneficio);

        Beneficio resultado = beneficioEjbService.save(beneficio);

        assertNotNull(resultado);

        verify(beneficioRepository, times(1)).save(beneficio);
    }

    @Test
    void updateOk() {
        Beneficio beneficio = criarBeneficio();

        beneficio.setValor(new BigDecimal("5000"));

        when(beneficioRepository.save(any(Beneficio.class))).thenReturn(beneficio);

        Beneficio retorno = beneficioEjbService.save(beneficio);

        verify(beneficioRepository, times(1)).save(retorno);
    }

    @Test()
    void transferErrorAmount() {
        Beneficio beneficioA = criarBeneficio();
        Beneficio beneficioB = criarBeneficio();
        BigDecimal amount = new BigDecimal("0");

        //Alterando ID e Valor do Beneficio B para não ficar igual ao Beneficio A e não causar inconsistência.
        beneficioB.setId(2L);
        beneficioB.setValor(new BigDecimal("500"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            beneficioEjbService.transfer(beneficioA.getId(), beneficioB.getId(), amount);
        });

        assertEquals("Valor deve ser maior que zero", exception.getMessage());

    }

    @Test
    void deleteBeneficioException() {

        when(beneficioRepository.findById(1L)).thenThrow(new RuntimeException("Benefício não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            beneficioEjbService.delete(1L);
        });

        assertEquals("Benefício não encontrado", exception.getMessage());
    }

    public Beneficio criarBeneficio() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setNome("BeneficioTeste");
        beneficio.setDescricao("TesteDescricao");
        beneficio.setValor(new BigDecimal("100"));
        return beneficio;
    }
}