package com.po.repository;

import com.po.entity.PoLine;
import com.po.entity.PoHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PoLineRepository extends JpaRepository<PoLine, Long> {
    List<PoLine> findByPoHeader(PoHeader poHeader);
}
