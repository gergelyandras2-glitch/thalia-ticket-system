package hu.szinhaz.ticket.repository;

import hu.szinhaz.ticket.model.PerformanceSeat;
import hu.szinhaz.ticket.model.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceSeatRepository extends JpaRepository<PerformanceSeat, Long> {

    List<PerformanceSeat> findByPerformanceIdOrderByRowNumberAscSeatNumberAsc(Long performanceId);

    List<PerformanceSeat> findByPerformanceIdAndStatus(Long performanceId, SeatStatus status);

    long countByPerformanceIdAndStatus(Long performanceId, SeatStatus status);

    long countByPerformanceId(Long performanceId);

    // Pessimistic Locking for Concurrency Safety (ADR-002)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM PerformanceSeat s WHERE s.id IN :ids")
    List<PerformanceSeat> findByIdInWithLock(@Param("ids") Collection<Long> ids);

    // Pessimistic Locking for Single Seat
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM PerformanceSeat s WHERE s.id = :id")
    Optional<PerformanceSeat> findByIdWithLock(@Param("id") Long id);
}
