package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityScore {

    private String cisq;

    private String cwe;

    private String owasp;

    private String gdpr;

    private String sqlInjections;

    private String xss;

    private String commandInjection;

    private String misconfiguration;

}
