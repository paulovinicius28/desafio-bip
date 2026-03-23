package com.example.dao;

import com.example.common.entity.Beneficio;
import com.example.common.repository.BeneficioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BeneficioDAO {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BeneficioRepository beneficioRepository;

    public Beneficio findById(Long id) {
        return beneficioRepository.findById(id).orElse(null);
    }
}

