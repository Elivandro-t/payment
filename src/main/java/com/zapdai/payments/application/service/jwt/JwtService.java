package com.zapdai.payments.application.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapdai.payments.domain.vo.ItensDoCarrinho;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JwtService {
    public String GeraToken(List<ItensDoCarrinho> plano) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String planoJson = mapper.writeValueAsString(plano);
        try {
            var algorith = Algorithm.HMAC256("fgdfsgdfgdsfgd");
            return JWT.create()
                    .withIssuer("API Zapidai")
                    .withClaim("plano",planoJson)
                    .sign(algorith);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Erro na geração de token!");
        }
    }

    public List<ItensDoCarrinho> validaToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("fgdfsgdfgdsfgd");
            var jwt = JWT.require(algorithm)
                    .withIssuer("API Zapidai")
                    .build()
                    .verify(token);

            String planoJson = jwt.getClaim("plano").asString();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(planoJson, new TypeReference<List<ItensDoCarrinho>>() {});
        } catch (JWTVerificationException | JsonProcessingException exception){
            throw new RuntimeException(exception);
        }
    }
}
