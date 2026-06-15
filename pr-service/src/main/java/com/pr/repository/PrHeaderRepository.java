package com.pr.repository;

import com.pr.entity.PrHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrHeaderRepository extends JpaRepository<PrHeader, Long> {
    Optional<PrHeader> findByPrNumber(String prNumber);

    long countByStatus(String submitted);
}
