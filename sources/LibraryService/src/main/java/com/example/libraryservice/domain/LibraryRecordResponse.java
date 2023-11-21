package com.example.libraryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//Response body for libraryRecord object
public class LibraryRecordResponse {
    private int id;

    private Date reservedDate;

    private Date returnDate;
}
