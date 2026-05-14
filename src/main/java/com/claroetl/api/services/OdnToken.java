package com.claroetl.api.services;

import java.time.LocalDateTime;

public class OdnToken {
    private String accessSession;
    private String roaRand;
    private Long expires;
    private String additionalInfo;
    private LocalDateTime expiresAt;
    
    public String getAccessSession() {
        return accessSession;
    }
    public void setAccessSession(String accessSession) {
        this.accessSession = accessSession;
    }
    public String getRoaRand() {
        return roaRand;
    }
    public void setRoaRand(String roaRand) {
        this.roaRand = roaRand;
    }
    public Long getExpires() {
        return expires;
    }
    public void setExpires(Long expires) {
        this.expires = expires;
    }
    public String getAdditionalInfo() {
        return additionalInfo;
    }
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    @Override
    public String toString() {
        return "OdnToken [accessSession=" + accessSession + ", roaRand=" + roaRand + ", expires=" + expires
                + ", additionalInfo=" + additionalInfo + "]";
    }
}
