package com.vendor.dto;

public class ReceiptDTO {
    private Long id;
    private String receiptNumber;
    private String Status;

    public ReceiptDTO() {
    }

    public ReceiptDTO(Long id, String receiptNumber, String status) {
        this.id = id;
        this.receiptNumber = receiptNumber;
        Status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
