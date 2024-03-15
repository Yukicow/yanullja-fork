package com.battlecruisers.yanullja.review;

import com.battlecruisers.yanullja.review.domain.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long>,
    CustomReviewRepository {


}
