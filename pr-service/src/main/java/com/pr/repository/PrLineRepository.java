package com.pr.repository;

import com.pr.entity.PrLine;
import com.pr.entity.PrHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrLineRepository extends JpaRepository<PrLine, Long> {
    List<PrLine> findByPrHeader(PrHeader prHeader);
}
