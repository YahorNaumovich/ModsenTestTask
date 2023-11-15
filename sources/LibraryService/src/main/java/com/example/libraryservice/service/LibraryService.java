package com.example.libraryservice.service;

import com.example.libraryservice.domain.LibraryRecordResponse;
import com.example.libraryservice.dto.RecordDto;
import com.example.libraryservice.entity.LibraryRecord;
import com.example.libraryservice.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    private final RecordDto mapper;

    public LibraryRecordResponse deleteRecord(int id){
        LibraryRecord libraryRecord = libraryRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        if (libraryRecord.getReservedDate() != null) throw new ResponseStatusException(HttpStatus.CONFLICT);
        libraryRepository.delete(libraryRecord);
        return mapper.toDto(libraryRecord);
    }
    public List<LibraryRecordResponse> getReservedRecords(){
        List<LibraryRecord> records = libraryRepository.findAllByReservedDateIsNotNull();
        return records.stream().map(mapper::toDto).toList();
    }
    public List<LibraryRecordResponse> getAvailableRecords(){
        List<LibraryRecord> records = libraryRepository.findAllByReservedDateIsNull();
        return records.stream().map(mapper::toDto).toList();
    }

    //rewrite method using request?
    public LibraryRecordResponse updateReserved(int id){
        LibraryRecord libraryRecord = libraryRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        if (libraryRecord.getReservedDate() != null) throw new ResponseStatusException(HttpStatus.CONFLICT);
        libraryRecord.setId(id);
        libraryRecord.setReservedDate(new Date());
        libraryRecord.setReturnDate(new Date(1212121212121L));
        libraryRepository.save(libraryRecord);
        return mapper.toDto(libraryRecord);
    }

    public LibraryRecordResponse updateAvailable(int id){
        LibraryRecord libraryRecord = libraryRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        libraryRecord.setId(id);
        libraryRecord.setReservedDate(null);
        libraryRecord.setReturnDate(null);
        libraryRepository.save(libraryRecord);
        return mapper.toDto(libraryRecord);
    }

    public LibraryRecordResponse addNewAvailableBook(int id){
        boolean libraryRecordExists = libraryRepository.existsById(id);
        if (libraryRecordExists)
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        LibraryRecord libraryRecord = new LibraryRecord(id, null, null);
        libraryRepository.save(libraryRecord);
        return mapper.toDto(libraryRecord);
    }
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Element does not exist")
    static class RecordNotFoundException extends NoSuchElementException {}

}
