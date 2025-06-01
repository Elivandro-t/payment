package com.zapdai.payments.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zapdai.payments.application.service.jwt.JwtService;
import com.zapdai.payments.application.service.payment_service.ReferenceService;
import com.zapdai.payments.domain.vo.ItensDoCarrinho;
import com.zapdai.payments.domain.vo.referecieDTO.ReferencieDTO;
import com.zapdai.payments.domain.vo.referecieDTO.ReferencieResponseDTO;
import com.zapdai.payments.infra.pagamentos.PagamentosRepository;
import com.zapdai.payments.infra.pagamentos.StatusPagamento;
import com.zapdai.payments.infra.websocketconf.PaymentInteface;
import com.zapdai.payments.infra.websocketconf.PaymentResponse;
import com.zapdai.payments.infra.websocketconf.PaymentResponsePix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentAsaas {

    @Value("${asaas.api.url}")
    private String apiUrl;

    @Value("${asaas.api.key}")
    private String apiKey;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private PagamentosRepository repositoryPagamentos;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public PaymentInteface processPayment(PaymentRequest request) throws IOException, InterruptedException {

        var existePagamento = repositoryPagamentos.findOneByEmailCluster(request.email);
        if(existePagamento != null) {
            for (ItensDoCarrinho item : request.itens) {
                if (existePagamento.isStatusPago() && existePagamento.getPlanoId().equals(item.getId())) {
                    throw new RuntimeException("Plano já incluído");
                }else{
                    existePagamento.setStatusPago(false);
                    existePagamento.setStatusPagoPlano(StatusPagamento.PROCESSANDO_PAGAMENTO);
                    repositoryPagamentos.save(existePagamento);
                    repositoryPagamentos.flush();
                }
            }
        }
        if (request.value.compareTo(10.0) < 0) {
            throw new RuntimeException("O valor mínimo para pagamento é R$10,00.");
        }
       if ("card".equalsIgnoreCase(request.paymentMethodId)){
            String customerId = createCustomer(request);
            String creditCardToken = generateCardToken(request,customerId);
            String subscriptionId = createSubscription(customerId,creditCardToken, request.value,request.itens,request);


          var v =   new PaymentResponse(
                    customerId,
                    subscriptionId,
                    "Assinatura criada com sucesso"
            );
            return  v;
        }else if ("PIX".equalsIgnoreCase(request.paymentMethodId)) {
            String customerId = createCustomer(request);
            var id = createPixPayment(customerId, request.value,request.itens,request);

         return   respostaqr(id);
        }else {
            throw new RuntimeException("pagamento não suportado!");
        }

    }
    private String createCustomer(PaymentRequest request) throws IOException, InterruptedException {
        String url = apiUrl + "/customers";


        Map<String, Object> body = new HashMap<>();
        body.put("name", request.name);
        body.put("cpfCnpj", request.cpfCnpj);
        body.put("email", request.email);
        body.put("phone", request.phone);
        body.put("postalCode", request.postalCode);
        body.put("addressNumber", request.addressNumber);
        body.put("complement", request.addressComplement);

        String requestBody = mapper.writeValueAsString(body);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("access_token", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            Map<String, Object> result = mapper.readValue(response.body(), Map.class);
            return (String) result.get("id");
        } else {
            throw new RuntimeException("Erro ao criar cliente: " + response.body());
        }
    }
    public String generateCardToken(PaymentRequest cardRequest,String customerId) throws IOException, InterruptedException {
        String url = apiUrl + "/creditCard/tokenize";

        Map<String, Object> body = new HashMap<>();
        Map<String, String> creditCard = new HashMap<>();
        body.put("customer", customerId); // Adicione isso!
        creditCard.put("holderName", cardRequest.name);
        creditCard.put("number", cardRequest.creditCardNumber);
        creditCard.put("expiryMonth", cardRequest.creditCardExpiryMonth);
        creditCard.put("expiryYear", cardRequest.creditCardExpiryYear);
        creditCard.put("ccv", cardRequest.creditCardCcv);

        body.put("creditCard", creditCard);

        String requestBody = mapper.writeValueAsString(body);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("access_token", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            Map<String, Object> result = mapper.readValue(response.body(), Map.class);
            return (String) result.get("creditCardToken");
        } else {
            throw new RuntimeException("Erro ao gerar token do cartão: " + response.body());
        }
    }




    private String createSubscription(String customerId, String creditCardToken, Double value,List<ItensDoCarrinho> itens,PaymentRequest request) throws IOException, InterruptedException {
        String url = apiUrl + "/subscriptions";
        String planoId = request.itens.get(0).getId();
        String planoname = request.itens.get(0).getTitle();
         var ref = new ReferencieDTO(planoId,planoname,request.email,value,request.name);
        ReferencieResponseDTO referencieResponseDTO = referenceService.save(ref);
        Map<String, Object> body = new HashMap<>();
        body.put("customer", customerId);
        body.put("billingType", "CREDIT_CARD");
        body.put("creditCardToken", creditCardToken);
        body.put("value", value);
        body.put("cycle", "MONTHLY");
        body.put("description", "Assinatura Mensal");
        body.put("externalReference", referencieResponseDTO.id());
        body.put("charge", "NOW");
        body.put("nextDueDate", "2025-06-24");
        // opcional: data do primeiro vencimento, se quiser diferente da data atual
        String requestBody = mapper.writeValueAsString(body);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("access_token", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            Map<String, Object> result = mapper.readValue(response.body(), Map.class);
            return (String) result.get("id");
        } else {
            throw new RuntimeException("Erro ao criar assinatura: " + response.body());
        }
    }
    private String extractId(String json) {
        int idIndex = json.indexOf("\"id\":\"") + 6;
        int endIndex = json.indexOf("\"", idIndex);
        return json.substring(idIndex, endIndex);
    }
    private String createPixPayment(String customerId, Double value, List<ItensDoCarrinho> itens,PaymentRequest paymentRequest) throws IOException, InterruptedException {
        String url = apiUrl + "/payments";
        String planoId = paymentRequest.itens.get(0).getId();
        String planoname = paymentRequest.itens.get(0).getTitle();
        var ref = new ReferencieDTO(planoId,planoname,paymentRequest.email,value,paymentRequest.name);
        ReferencieResponseDTO referencieResponseDTO = referenceService.save(ref);
        Map<String, Object> body = new HashMap<>();
        body.put("customer", customerId);
        body.put("externalReference",referencieResponseDTO.id());
        body.put("billingType", "PIX");
        body.put("value", value);
        body.put("description", "Pagamento do Plano");
        body.put("dueDate", LocalDateTime.now().plusDays(1).toLocalDate().toString());

        String requestBody = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("access_token", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            Map<String, Object> result = mapper.readValue(response.body(), Map.class);
            return (String) result.get("id");
        } else {
            throw new RuntimeException("Erro ao criar pagamento PIX: " + response.body());
        }
    }
    public PaymentResponsePix respostaqr(String idPaymnetpyx) throws IOException, InterruptedException {
        var url = apiUrl + "/payments/" + idPaymnetpyx + "/pixQrCode";
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("access_token", apiKey)
                .GET()
                .build();
        HttpResponse response = HttpClient.newHttpClient().send(request,HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() >= 200 && response.statusCode() < 300 ){
            Map result = mapper.readValue(response.body().toString(), Map.class);
            String key = (String) result.get("payload");
            String encodedImage = (String) result.get("encodedImage");
            String exp = (String) result.get("expirationDate");

            return new PaymentResponsePix(encodedImage,key,"QR criado com sucesso!",exp);

        }
        return null;
    }




}
