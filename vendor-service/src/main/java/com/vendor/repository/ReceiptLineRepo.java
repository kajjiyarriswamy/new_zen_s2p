package com.vendor.repository;

import com.vendor.entity.ReceiptLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptLineRepo extends JpaRepository<ReceiptLine,Long> {
}
