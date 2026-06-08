package com.vendor.dto;


	public class DeliveryNoteResponseDto {

	    private Long id;
	    private String deliveryNoteNumber;
	    private String status;

	    public DeliveryNoteResponseDto() {
	    }

	    public DeliveryNoteResponseDto(Long id,
	                                   String deliveryNoteNumber,
	                                   String status) {
	        this.id = id;
	        this.deliveryNoteNumber = deliveryNoteNumber;
	        this.status = status;
	    }

	    public Long getId() {
	        return id;
	    }

	    public String getDeliveryNoteNumber() {
	        return deliveryNoteNumber;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public void setDeliveryNoteNumber(String deliveryNoteNumber) {
	        this.deliveryNoteNumber = deliveryNoteNumber;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }
	}

