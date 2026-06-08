package com.vendor.controller;


	import com.vendor.dto.ApiResponse;
	import com.vendor.dto.DeliveryNoteRequestDto;
	import com.vendor.dto.DeliveryNoteResponseDto;
	import com.vendor.service.DeliveryNoteService;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.*;

	@RestController
	@RequestMapping("/api/delivery-notes")
	public class DeliveryNoteController {

	    private final DeliveryNoteService deliveryNoteService;

	    public DeliveryNoteController(
	            DeliveryNoteService deliveryNoteService) {
	        this.deliveryNoteService = deliveryNoteService;
	    }

	    @PostMapping
	    public ResponseEntity<ApiResponse<DeliveryNoteResponseDto>>
	    createDeliveryNote(
	            @RequestBody DeliveryNoteRequestDto requestDto) {

	        DeliveryNoteResponseDto response =
	                deliveryNoteService.createDeliveryNote(requestDto);

	        return ResponseEntity.ok(
	                ApiResponse.success(
	                        response,
	                        "Delivery Note Created Successfully"));
	    }
	}


