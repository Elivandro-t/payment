package com.zapdai.payments.infra;

import com.zapdai.payments.domain.vo.referecieDTO.ReferencieDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.Random;

@Entity
@Table(name = "referencia")
public class Reference {
    @Id
    private String id;
    private String emailUsuario;
    private String name;
    private Double valor;
    private String planoId;
    private String namePlano;

    public Reference(ReferencieDTO referencieDTO) {
        this.emailUsuario = referencieDTO.email();
        this.valor = referencieDTO.valor();
        this.name = referencieDTO.name();
        this.namePlano = referencieDTO.planoName();
        this.planoId = referencieDTO.planoId();
    }
    @PrePersist
    public void gerarId() {
        if (id == null) {
            long timestamp = System.currentTimeMillis();
            int random = new Random().nextInt(99999);
            this.id = "zapdai_reference_id-" + timestamp + "-" + random;
        }
    }

    public Reference() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getEmail() {
        return emailUsuario;
    }

    public void setEmail(String email) {
        this.emailUsuario = email;
    }


    public String getName() {
        return name;
    }

    public String getPlanoId() {
        return planoId;
    }

    public String getNamePlano() {
        return namePlano;
    }
}
