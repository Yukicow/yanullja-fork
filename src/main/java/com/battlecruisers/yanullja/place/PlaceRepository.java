package com.battlecruisers.yanullja.place;

import com.battlecruisers.yanullja.place.domain.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long>,
    CustomPlaceRepository {

    @Query("SELECT p FROM Place p JOIN FETCH p.rooms JOIN FETCH p.subRegion WHERE p.id IN (:ids)")
    List<Place> findAllWithEntityGraphById(@Param("ids") List<Long> ids);
}
