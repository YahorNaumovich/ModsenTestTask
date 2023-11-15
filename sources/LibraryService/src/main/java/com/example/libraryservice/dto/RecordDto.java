package com.example.libraryservice.dto;

import com.example.libraryservice.domain.LibraryRecordRequest;
import com.example.libraryservice.domain.LibraryRecordResponse;
import com.example.libraryservice.entity.LibraryRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RecordDto {
    LibraryRecordResponse toDto(LibraryRecord libraryRecord);
    LibraryRecord fromDto(LibraryRecordRequest libraryRecordRequest);
}
