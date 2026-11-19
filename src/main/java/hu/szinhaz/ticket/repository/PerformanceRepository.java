package hu.szinhaz.ticket.repository;

import hu.szinhaz.ticket.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findAllByOrderByDateTimeAsc();

    List<Performance> findByGenreIgnoreCaseOrderByDateTimeAsc(String genre);

    @Query("SELECT p FROM Performance p WHERE " +
           "(:query IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.author) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:genre IS NULL OR :genre = '' OR LOWER(p.genre) = LOWER(:genre)) AND " +
           "(:fromTime IS NULL OR p.dateTime >= :fromTime) " +
           "ORDER BY p.dateTime ASC")
    List<Performance> searchPerformances(@Param("query") String query,
                                        @Param("genre") String genre,
                                        @Param("fromTime") LocalDateTime fromTime);

    @Query("SELECT DISTINCT p.genre FROM Performance p ORDER BY p.genre ASC")
    List<String> findAllDistinctGenres();
}
