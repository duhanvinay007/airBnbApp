package com.duhanvinay.projects.airBnbApp.service;

import com.duhanvinay.projects.airBnbApp.entity.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl , String failureUrl);
}
