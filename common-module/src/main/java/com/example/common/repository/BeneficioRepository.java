package com.example.common.repository;

import com.example.common.entity.Beneficio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Beneficio> findById(Long idBeneficio);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Beneficio save(Beneficio beneficio);
}

