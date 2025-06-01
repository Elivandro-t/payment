package com.zapdai.payments.infra.pagamentos;

import com.zapdai.payments.infra.Reference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenciEntityRepository extends JpaRepository<Reference,String> {
}
