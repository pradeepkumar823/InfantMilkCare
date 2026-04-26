package com.InfantMilk.Care.dto;

import lombok.Data;

@Data
public class ChildDetailsRequest {
    private String name;
    private Integer ageMonths;
    private Double weightKg;
    private String gender;
    private String countryRegion;
    private Boolean privacyShieldEnabled;
}
