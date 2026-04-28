package com.duhanvinay.projects.airBnbApp.service;

import com.duhanvinay.projects.airBnbApp.dto.RoomDto;
import com.duhanvinay.projects.airBnbApp.entity.Hotel;
import com.duhanvinay.projects.airBnbApp.entity.Room;
import com.duhanvinay.projects.airBnbApp.entity.User;
import com.duhanvinay.projects.airBnbApp.exception.ResourceNotFoundException;
import com.duhanvinay.projects.airBnbApp.exception.UnAuthorisedException;
import com.duhanvinay.projects.airBnbApp.repository.HotelRepository;
import com.duhanvinay.projects.airBnbApp.repository.InventoryRepository;
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
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryServices inventoryServices;

    @Override
    public RoomDto createNewRoom(Long hotelId , RoomDto roomDto) {
        log.info("Creating a new room in hotel with Id :{}", hotelId);
        Hotel hotel = hotelRepository.
                findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId ));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Hotel does not belong to this User with ID: " + hotelId);
        }

        Room room = modelMapper.map (roomDto , Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);


        if(hotel.getActive()){
            inventoryServices.initializeRoomForAYear(room);

        }


        return modelMapper.map(room , RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms in hotel with Id :{}", hotelId);
        Hotel hotel = hotelRepository.
                findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId ));
        return  hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class)).collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting room with id :{}", roomId);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        return modelMapper.map(room , RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting room with id :{}", roomId);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorisedException("Hotel does not belong to this room with ID: " + roomId);
        }
        inventoryServices.deleteAllInventories(room);
        roomRepository.deleteById(roomId);

    }

    @Override
    @Transactional
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {

        log.info("updating the room with ID: {}", roomId );
        Hotel hotel = hotelRepository.
                findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId  ));

        User user = getCurrentUser();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("Hotel does not belong to this User with ID: " + hotelId);

        }

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        modelMapper.map(roomDto , room);
        room.setId(roomId);

        room = roomRepository.save(room);
        return modelMapper.map(room , RoomDto.class);
    }
}
