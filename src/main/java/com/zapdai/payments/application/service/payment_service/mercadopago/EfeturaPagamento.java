//package com.zapdai.payments.application.service.payment_service;
//
//import com.zapdai.payments.domain.vo.ItensDoCarrinho;
//import com.zapdai.payments.infra.pagamentos.PagamentosEntity;
//import com.zapdai.payments.infra.pagamentos.PagamentosRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class EfeturaPagamento {
//    @Value("${mercado_pago_sample_access_token}")
//    private String mercadoPagoAccessToken;
//    @Autowired
//    private Currentepaiment currentepaiment;
//    @Autowired
//    private PagamentosRepository repositoryPagamentos;
//
//    @Scheduled(cron = "0/10 * * * * *")
//    public void EfetuaPagamentocobrança(){
//        var existePagamento = repositoryPagamentos.findAll();
//        for (PagamentosEntity pg:existePagamento){
//            if(pg != null) {
//               String dadosCar = """
//                       Dados do cartao salvo:
//                       cardID: "%s"
//                       customerID: "%s"
//                       paymentID: "%s"
//
//                       todos os dados do banco!
//                       """.formatted(pg.getCardId(),pg.getConsumerId(),pg.getPaymenteId());
//
//               System.out.println(dadosCar);
//                currentepaiment.createPayment(pg.getValorPago(),pg.getConsumerId(),pg.getCardId(),pg.getPaymenteId(),mercadoPagoAccessToken);
//
//            }
//        }
//
//    }
//}
