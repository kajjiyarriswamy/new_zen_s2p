package com.vendor.repository;

	import com.vendor.entity.DeliveryNoteLine;
	import org.springframework.data.jpa.repository.JpaRepository;

	public interface DeliveryNoteLineRepository
	        extends JpaRepository<DeliveryNoteLine, Long> {
	}

