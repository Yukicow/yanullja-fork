package com.battlecruisers.yanullja.room;

import com.battlecruisers.yanullja.room.domain.Room;
import com.battlecruisers.yanullja.room.dto.RoomNameDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>,
    CustomRoomRepository {

    @Query("SELECT new com.battlecruisers.yanullja.room.dto.RoomNameDto(r.id, r.name) FROM Room r WHERE r.place.id = :placeId")
    List<RoomNameDto> findAllRoomNameDtosByPlaceId(
        @Param("placeId") Long placeId);

    @Query("SELECT r "
        + "FROM Room r "
        + "JOIN FETCH r.place p "
        + "WHERE r.place.id = :placeId AND r.capacity >= :guestCount")
    List<Room> findRoomsWithPlaceByPlaceIdAndCapacityGreaterThan(
        @Param("placeId") Long placeId,
        @Param("guestCount") Integer guestCount);
}

