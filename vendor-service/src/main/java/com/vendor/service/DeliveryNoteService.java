package com.vendor.service;


	import com.vendor.dto.DeliveryNoteLineRequestDto;
	import com.vendor.dto.DeliveryNoteRequestDto;
	import com.vendor.dto.DeliveryNoteResponseDto;
	import com.vendor.entity.DeliveryNoteHeader;
	import com.vendor.entity.DeliveryNoteLine;
	import com.vendor.repository.DeliveryNoteHeaderRepository;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

	import java.math.BigDecimal;
	import java.time.LocalDate;
	import java.time.LocalDateTime;

	@Service
	@Transactional
	public class DeliveryNoteService {

	    private final DeliveryNoteHeaderRepository deliveryNoteHeaderRepository;

	    public DeliveryNoteService(
	            DeliveryNoteHeaderRepository deliveryNoteHeaderRepository) {
	        this.deliveryNoteHeaderRepository = deliveryNoteHeaderRepository;
	    }

	    public DeliveryNoteResponseDto createDeliveryNote(
	            DeliveryNoteRequestDto requestDto) {

	        // Create Header
	        DeliveryNoteHeader header = new DeliveryNoteHeader();

	        header.setPoId(requestDto.getPoId());
	        header.setPoNumber(requestDto.getPoNumber());

	        header.setVendorId(requestDto.getVendorId());
	        header.setVendorCode(requestDto.getVendorCode());
	        header.setVendorName(requestDto.getVendorName());

	        header.setDeliveryNoteDate(LocalDate.now());
	        header.setStatus("SUBMITTED");

	        header.setCreatedBy("SYSTEM");
	        header.setCreatedDate(LocalDateTime.now());

	        // Generate Delivery Note Number
	        String deliveryNoteNumber =
	                "DN-" + LocalDate.now().getYear() + "-0001";

	        header.setDeliveryNoteNumber(deliveryNoteNumber);

	        BigDecimal totalQuantity = BigDecimal.ZERO;

	        // Add Lines
	        for (DeliveryNoteLineRequestDto lineDto : requestDto.getLines()) {

	            DeliveryNoteLine line = new DeliveryNoteLine();

	            line.setPoLineId(lineDto.getPoLineId());
	            line.setDeliveredQuantity(lineDto.getDeliveredQuantity());

	            // Set Parent Reference
	            line.setDeliveryNoteHeader(header);

	            // Add Line To Header
	            header.getDeliveryNoteLines().add(line);

	            // Calculate Total Quantity
	            totalQuantity = totalQuantity.add(
	                    lineDto.getDeliveredQuantity());
	        }

	        header.setTotalQuantity(totalQuantity);

	        // Save Header and Lines
	        DeliveryNoteHeader savedHeader =
	                deliveryNoteHeaderRepository.save(header);

	        // Prepare Response
	        DeliveryNoteResponseDto response = new DeliveryNoteResponseDto();
	        response.setId(savedHeader.getId());
	        response.setDeliveryNoteNumber(
	                savedHeader.getDeliveryNoteNumber());
	        response.setStatus(savedHeader.getStatus());

	        return response;
	    }
	}

