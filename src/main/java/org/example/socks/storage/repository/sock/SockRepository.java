package org.example.socks.storage.repository.sock;

import org.example.socks.storage.model.sock.entity.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SockRepository extends JpaRepository<Sock, Long> {
    Sock findSockByColorAndCottonPercent(String color, Integer cottonPercent);

    @Query("select SUM(s.count) from Sock s where s.color=:color and s.cottonPercent < :lessThan and s.cottonPercent > :moreThan")
    Optional<Long> findSockCountByConstraints(String color, Integer lessThan, Integer moreThan);

    @Query("select SUM(s.count) from Sock as s where s.cottonPercent < :lessThan and s.cottonPercent > :moreThan")
    Optional<Long> findSockCountByConstraints(Integer lessThan, Integer moreThan);

    @Query("select SUM(s.count) from Sock as s where s.color=:color")
    Optional<Long> findSockCountByConstraint(String color);

    @Query("select SUM(s.count) from Sock as s where s.cottonPercent=:equal")
    Optional<Long> findSockCountByConstraint(Integer equal);

    @Query("select SUM(s.count) from Sock as s where s.color=:color and s.cottonPercent=:equal")
    Optional<Long> findSockCountByConstraints(String color, Integer equal);
}
