package com.duhanvinay.projects.airBnbApp.service;

import com.duhanvinay.projects.airBnbApp.dto.HotelDto;
import com.duhanvinay.projects.airBnbApp.dto.HotelInfoDto;
import com.duhanvinay.projects.airBnbApp.entity.Hotel;

import java.util.List;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto) ;

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(  Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId);

    List<HotelDto> getAllHotels();
}
