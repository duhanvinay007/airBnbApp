package com.duhanvinay.projects.airBnbApp.strategy;

import com.duhanvinay.projects.airBnbApp.entity.Inventory;

import java.math.BigDecimal;
public interface PricingStrategy {


    BigDecimal calculatePrice(Inventory inventory);
}
