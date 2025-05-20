package com.zapdai.payments.infra.pagamentos;

import com.zapdai.payments.domain.vo.PagamentoResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentosRepository extends JpaRepository<PagamentosEntity,Long> {

    @Query("SELECT p FROM PagamentosEntity p WHERE p.pagamentoIdMercadoPago=:id and p.PagamentoRef=:ref")
    PagamentosEntity findOneByNameByEmail(@Param("id") Long id,String ref);
    @Query("SELECT p FROM PagamentosEntity p WHERE p.email=:email and p.statusPagoPlano=:pago")
    PagamentosEntity findOneByEmail(String email, StatusPagamento pago);
}
