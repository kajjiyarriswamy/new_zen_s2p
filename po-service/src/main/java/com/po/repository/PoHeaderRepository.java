package com.po.repository;

import com.po.entity.PoHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PoHeaderRepository extends JpaRepository<PoHeader, Long> {
    Optional<PoHeader> findByPoNumber(String poNumber);
}
