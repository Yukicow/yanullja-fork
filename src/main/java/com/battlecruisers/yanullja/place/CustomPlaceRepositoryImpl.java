package com.battlecruisers.yanullja.place;


import static com.battlecruisers.yanullja.place.domain.QPlace.place;
import static com.battlecruisers.yanullja.region.domain.QSubRegion.subRegion;
import static com.battlecruisers.yanullja.reservation.domain.QReservation.reservation;
import static com.battlecruisers.yanullja.room.domain.QRoom.room;

import com.battlecruisers.yanullja.place.domain.Place;
import com.battlecruisers.yanullja.place.dto.SearchConditionDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CustomPlaceRepositoryImpl implements CustomPlaceRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Place> searchPlacesWithConditions(
        SearchConditionDto searchConditionDto, Pageable pageable) {

        return jpaQueryFactory
            .selectFrom(place)
            .distinct()
            .leftJoin(place.subRegion, subRegion).fetchJoin()
            .leftJoin(place.rooms, room).fetchJoin()
            .where(
                makeBooleanBuilderForSearch(searchConditionDto)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    }


    @Override
    public List<Place> queryPlacesInRegion(String regionName,
        Pageable pageable) {
        return jpaQueryFactory
            .selectFrom(place)
            .distinct()
            .join(place.rooms).fetchJoin()
            .innerJoin(place.subRegion).fetchJoin()
            .where(place.subRegion.name.eq(regionName))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    }

    @Override
    public List<Place> queryPlaceInCategory(String categoryName,
        PlaceCategory placeCategory) {
        return jpaQueryFactory
            .selectFrom(place)
            .distinct()
            .join(place.rooms).fetchJoin()
            .where(place.category.eq(placeCategory))
            .fetch();
    }

    @Override
    public List<Place> queryPlacesRanking(Pageable pageable) {
        return jpaQueryFactory
            .select(place)
            .from(reservation)
            .innerJoin(reservation.room)
            .rightJoin(room.place, place)
            .groupBy(place)
            .orderBy(reservation.count().desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    private BooleanExpression goeGuestCount(Integer guestCount) {
        return room.capacity.goe(guestCount);
    }


    private BooleanBuilder makeBooleanBuilderForSearch(
        SearchConditionDto searchConditionDto) {

        BooleanBuilder builder = new BooleanBuilder();
        Integer capacity = searchConditionDto.getGuest();
        String keyword = searchConditionDto.getName();

        if (org.springframework.util.StringUtils.hasText(keyword)
            && !keyword.equals("null")) {
            builder.and(eqKeyword(keyword));
        }

        return builder;
    }


    private BooleanExpression eqKeyword(String keyword) {
        return place.name.containsIgnoreCase(keyword);
    }
}
