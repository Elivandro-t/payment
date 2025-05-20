package com.zapdai.payments.domain.vo;

import java.time.LocalDateTime;

public class MercadoPagoNotification {
    private String action;
    private String api_version;
    private Data data;
    private String date_created;
    private Long id;
    private boolean live_mode;
    private String type;
    private String user_id;

    public Data getData() { return data; }

    public static class Data {
        private String id;

        public String getId() { return id; }
    }

    public String getPaymentId() {
        return data != null ? data.getId() : null;
    }
}
