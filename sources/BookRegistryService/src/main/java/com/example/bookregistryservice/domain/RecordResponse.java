package com.example.bookregistryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RecordResponse {
    private int id;
    private Date reservedDate;
    private Date returnDate;
}
