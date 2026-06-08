package com.vendor.dto;

import java.math.BigDecimal;

public class DeliveryNoteLineRequestDto {
	
	private Long poLineId;
    private BigDecimal deliveredQuantity;

    public Long getPoLineId() {
        return poLineId;
    }

    public void setPoLineId(Long poLineId) {
        this.poLineId = poLineId;
    }

    public BigDecimal getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(BigDecimal deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;

}
}
