package com.vendor.repository;

import com.vendor.entity.ReceiptHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReciptHeaderRepo extends JpaRepository<ReceiptHeader,Long> {
}
