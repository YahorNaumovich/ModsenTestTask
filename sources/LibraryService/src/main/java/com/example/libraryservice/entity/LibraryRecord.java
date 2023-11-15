package com.example.libraryservice.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder=true)
@Table(name = "library_records")

public class LibraryRecord {
    @Id
    private int id;
    private Date reservedDate;
    private Date returnDate;
}
