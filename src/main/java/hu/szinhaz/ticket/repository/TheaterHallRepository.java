package hu.szinhaz.ticket.repository;

import hu.szinhaz.ticket.model.TheaterHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheaterHallRepository extends JpaRepository<TheaterHall, Long> {
    Optional<TheaterHall> findByName(String name);
}
