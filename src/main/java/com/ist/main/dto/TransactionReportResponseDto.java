package com.ist.main.dto;

import lombok.Data;

@Data
public class TransactionReportResponseDto {
    //
    //    public String getType();
    //    public String getTotalTransaction();
    //
    //    public String getTotalAmount();

    private String type;
    private Integer totalTransaction;
    private String totalAmount;
}
