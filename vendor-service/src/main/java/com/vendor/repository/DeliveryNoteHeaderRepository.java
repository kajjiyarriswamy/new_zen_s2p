package com.vendor.repository;


	import com.vendor.entity.DeliveryNoteHeader;
	import org.springframework.data.jpa.repository.JpaRepository;

	public interface DeliveryNoteHeaderRepository
	        extends JpaRepository<DeliveryNoteHeader, Long> {
	}

