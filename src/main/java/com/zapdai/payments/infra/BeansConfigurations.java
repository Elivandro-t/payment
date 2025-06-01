package com.zapdai.payments.infra;

import com.zapdai.payments.application.service.emailService.EmailService;
import com.zapdai.payments.application.service.payment_service.PagamentoServiceZapdai;
import com.zapdai.payments.gateways.PagamentoRepository;
import com.zapdai.payments.infra.pagamentos.PagamentosFacture;
import com.zapdai.payments.infra.pagamentos.PagamentosRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfigurations {


    @Bean
    public PagamentosFacture pagamentosFacture(PagamentosRepository pagamentosRepository){
        return new PagamentosFacture(pagamentosRepository);
    }
    @Bean
    public PagamentoServiceZapdai pagamentoServiceZapdai(PagamentoRepository repository){
        return new PagamentoServiceZapdai(repository);
    }
}

