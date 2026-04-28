package com.duhanvinay.projects.airBnbApp.controller;


import com.duhanvinay.projects.airBnbApp.dto.HotelDto;
import com.duhanvinay.projects.airBnbApp.dto.HotelInfoDto;
import com.duhanvinay.projects.airBnbApp.dto.HotelPriceDto;
import com.duhanvinay.projects.airBnbApp.dto.HotelSearchRequest;
import com.duhanvinay.projects.airBnbApp.service.HotelService;
import com.duhanvinay.projects.airBnbApp.service.InventoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {
    private final InventoryServices inventoryServices;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest  hotelSearchRequest){

        var page = inventoryServices.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}
