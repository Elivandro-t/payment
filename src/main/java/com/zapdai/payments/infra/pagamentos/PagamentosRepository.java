package com.zapdai.payments.infra.pagamentos;

import com.zapdai.payments.domain.Pagamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentosRepository extends JpaRepository<PagamentosEntity,Long> {
}
