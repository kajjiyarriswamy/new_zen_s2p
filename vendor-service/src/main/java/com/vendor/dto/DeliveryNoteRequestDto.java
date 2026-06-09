package com.vendor.dto;

	import java.util.List;

	public class DeliveryNoteRequestDto {

	    private Long poId;
	    private String poNumber;

	    private Long vendorId;
	    private String vendorCode;
	    private String vendorName;

	    private String vehicleNumber;
	    private String transporterName;

	    private List<DeliveryNoteLineRequestDto> lines;

	    public Long getPoId() {
	        return poId;
	    }

	    public void setPoId(Long poId) {
	        this.poId = poId;
	    }

	    public String getPoNumber() {
	        return poNumber;
	    }

	    public void setPoNumber(String poNumber) {
	        this.poNumber = poNumber;
	    }

	    public Long getVendorId() {
	        return vendorId;
	    }

	    public void setVendorId(Long vendorId) {
	        this.vendorId = vendorId;
	    }

	    public String getVendorCode() {
	        return vendorCode;
	    }

	    public void setVendorCode(String vendorCode) {
	        this.vendorCode = vendorCode;
	    }

	    public String getVendorName() {
	        return vendorName;
	    }

	    public void setVendorName(String vendorName) {
	        this.vendorName = vendorName;
	    }

	    public String getVehicleNumber() {
	        return vehicleNumber;
	    }

	    public void setVehicleNumber(String vehicleNumber) {
	        this.vehicleNumber = vehicleNumber;
	    }

	    public String getTransporterName() {
	        return transporterName;
	    }

	    public void setTransporterName(String transporterName) {
	        this.transporterName = transporterName;
	    }

	    public List<DeliveryNoteLineRequestDto> getLines() {
	        return lines;
	    }

	    public void setLines(List<DeliveryNoteLineRequestDto> lines) {
	        this.lines = lines;
	    }
	}


