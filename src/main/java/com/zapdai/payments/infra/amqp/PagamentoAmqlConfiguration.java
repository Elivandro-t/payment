package com.zapdai.payments.infra.amqp;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoAmqlConfiguration {

//     @Bean
//    public Queue pagamentoConcluido(){
//        return new Queue("pagamento.concluido",false);
//    }
//    @Bean
//    public Queue pagamentoConcluido(){
//        return QueueBuilder.nonDurable("pagamento.concluido").build();
//    }
    @Bean
   public RabbitAdmin criarCabbit(ConnectionFactory connectionFactory){
        return  new RabbitAdmin(connectionFactory);
   }
   @Bean
   public ApplicationListener<ApplicationReadyEvent> inicializarAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
   }
   //converte o payload
   @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
   }
   @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory,
                                         Jackson2JsonMessageConverter converter){
       RabbitTemplate rabbitTemplate =  new RabbitTemplate(factory);
       rabbitTemplate.setMessageConverter(converter);
       return rabbitTemplate;
   }
   @Bean
   public FanoutExchange fanoutExchange(){

        return new FanoutExchange("pagamento.ex");
   }

}
