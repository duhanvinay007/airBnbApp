package com.duhanvinay.projects.airBnbApp.service;

import com.duhanvinay.projects.airBnbApp.dto.HotelDto;
import com.duhanvinay.projects.airBnbApp.dto.HotelInfoDto;
import com.duhanvinay.projects.airBnbApp.dto.RoomDto;
import com.duhanvinay.projects.airBnbApp.entity.Hotel;
import com.duhanvinay.projects.airBnbApp.entity.Room;
import com.duhanvinay.projects.airBnbApp.entity.User;
import com.duhanvinay.projects.airBnbApp.exception.ResourceNotFoundException;
import com.duhanvinay.projects.airBnbApp.exception.UnAuthorisedException;
import com.duhanvinay.projects.airBnbApp.repository.HotelRepository;
import com.duhanvinay.projects.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.duhanvinay.projects.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    private final ModelMapper modelMapper;
    private final InventoryServices inventoryServices;
    private  final RoomRepository roomRepository;


    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);
        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID ;{}", hotelDto.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting the hotel with ID: {}", id);
        Hotel hotel = hotelRepository.
                findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id ));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Hotel does not belong to this User with ID: " + id);
        }

        return modelMapper.map(hotel, HotelDto.class);

    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("updating the hotel with ID: {}", id);
        Hotel hotel = hotelRepository.
                findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id ));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Hotel does not belong to this User with ID: " + id);
        }

        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.
                findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id ));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Hotel does not belong to this User with ID: " + id);
        }
        for(Room room : hotel.getRooms()) {
            inventoryServices.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating the hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository.
                findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId ));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Hotel does not belong to this User with ID: " + hotelId);
        }
        hotel.setActive(true);


        // assuming only do  it once
        for(Room room : hotel.getRooms()){
            inventoryServices.initializeRoomForAYear(room);
        }
    }


    // public method
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel = hotelRepository.
                findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId ));

        List<RoomDto> rooms = hotel.getRooms().stream().map((element) -> modelMapper.map(element, RoomDto.class))
                .toList();
      return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }

    @Override
    public List<HotelDto> getAllHotels() {
        User user = getCurrentUser();
        log.info("Getting all the hotels for the admin user with ID: {}", user.getId());

        List<Hotel> hotels = hotelRepository.findByOwner(user);
        return hotels.stream().map((element) -> modelMapper.map(element, HotelDto.class)).toList();
    }
}