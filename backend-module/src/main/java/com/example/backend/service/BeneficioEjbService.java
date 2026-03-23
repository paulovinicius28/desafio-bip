package com.example.backend.service;

import com.example.common.entity.Beneficio;
import com.example.common.repository.BeneficioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BeneficioEjbService implements IBeneficioEjbService {

    @Autowired
    private BeneficioRepository beneficioRepository;


    /**
     * Lista todos os benefícios registrados
     * 
     * @return Lista com todos os benefícios
     */
    public List<Beneficio> findAll() {
        return this.beneficioRepository.findAll();
    }

    /**
     * Busca um benefício pelo ID
     *
     * @param id ID do benefício
     * @return Benefício encontrado
     * @throws RuntimeException se benefício não for encontrado
     */
    public Optional<Beneficio> findById(Long id) {

        return this.beneficioRepository.findById(id);
    }

    /**
     * Salva um novo benefício
     * 
     * @param beneficio Dados do benefício a ser criado
     * @return Benefício criado
     * @throws IllegalArgumentException se dados forem inválidos
     */
    @Transactional
    public Beneficio save(Beneficio beneficio) {
        validarBeneficio(beneficio);
        return this.beneficioRepository.save(beneficio);
    }

    /**
     * Atualiza um benefício existente
     * 
     * @param beneficio Dados do benefício a ser atualizado
     * @return Benefício atualizado
     * @throws RuntimeException se benefício não for encontrado
     * @throws IllegalArgumentException se dados forem inválidos
     */
    @Transactional
    public Beneficio update(Beneficio beneficio) {
        validarBeneficio(beneficio);
        // Verifica se benefício existe antes de atualizar
        this.beneficioRepository.findById(beneficio.getId());
        return this.beneficioRepository.save(beneficio);
    }

    /**
     * Deleta um benefício
     * 
     * @param id ID do benefício a ser deletado
     * @throws RuntimeException se benefício não for encontrado
     */
    @Transactional
    public void delete(Long id) {
        Beneficio beneficio = this.beneficioRepository.findById(id).orElseThrow(() -> new RuntimeException("Benefício não encontrado"));
        this.beneficioRepository.delete(beneficio);
    }

    @Override
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        try {
            // Valida os parâmetros
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Valor deve ser maior que zero");
            }

            // Busca os benefícios
            Beneficio from = this.beneficioRepository.findById(fromId).orElseThrow(() -> new RuntimeException("Benefício origem não encontrado"));
            Beneficio to = this.beneficioRepository.findById(toId).orElseThrow(() -> new RuntimeException("Benefício destino não encontrado"));

            // Valida se tem saldo suficiente
            if (!temSaldoSuficiente(from.getValor(), amount)) {
                throw new RuntimeException("Saldo insuficiente");
            }

            // Realiza a transferência
            from.setValor(from.getValor().subtract(amount));
            to.setValor(to.getValor().add(amount));

            this.beneficioRepository.save(from);
            this.beneficioRepository.save(to);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar transferência: " + e.getMessage());
        }
    }

    /**
     * Verifica se o saldo é suficiente para a transferência
     */
    private boolean temSaldoSuficiente(BigDecimal saldo, BigDecimal amount) {
        return saldo.compareTo(amount) >= 0;
    }

    /**
     * Valida os dados do benefício
     */
    private void validarBeneficio(Beneficio beneficio) {
        if (beneficio.getNome() == null || beneficio.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do benefício é obrigatório");
        }
        if (beneficio.getValor() == null || beneficio.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor deve ser maior ou igual a zero");
        }
    }
}
