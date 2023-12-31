package com.example.libraryservice.service;

import com.example.libraryservice.domain.LibraryRecordResponse;
import com.example.libraryservice.dto.RecordDto;
import com.example.libraryservice.entity.LibraryRecord;
import com.example.libraryservice.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    private final RecordDto mapper;

    Logger logger = LoggerFactory.getLogger(LibraryService.class);

    public LibraryRecordResponse deleteRecord(int id) {
        logger.info("deleteRecord:start -> Id:{}", id);

        //Find a record by id in Library (if not found throw RecordNotFoundException)
        LibraryRecord libraryRecord = libraryRepository.findById(id).orElseThrow(RecordNotFoundException::new);

        //if book is reserved it can't be deleted (throw ResponseStatusException 409)
        if (libraryRecord.getReservedDate() != null) {
            logger.info("deleteRecord:Conflict -> Book is reserved and can't be deleted");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        //if book is not reserved delete its record from Library
        libraryRepository.delete(libraryRecord);

        logger.info("deleteRecord:end -> Id:{} - OK", id);
        return mapper.toDto(libraryRecord);
    }

    //TODO: add paging to getReservedRecords method
    public List<LibraryRecordResponse> getReservedRecords() {
        logger.info("getReservedRecords:start");

        //Find all reserved books in Library
        List<LibraryRecord> records = libraryRepository.findAllByReservedDateIsNotNull();

        logger.info("getReservedRecords:end");
        return records.stream().map(mapper::toDto).toList();
    }

    //TODO: add paging to getAvailableRecords method
    public List<LibraryRecordResponse> getAvailableRecords() {
        logger.info("getAvailableRecords:start");

        //Find all available books in Library
        List<LibraryRecord> records = libraryRepository.findAllByReservedDateIsNull();

        logger.info("getAvailableRecords:end");
        return records.stream().map(mapper::toDto).toList();
    }

    //TODO: update method updateReserved to be able to set reservation/return dates manually
    public LibraryRecordResponse updateReserved(int id) {
        logger.info("updateReserved:start -> Id:{}", id);

        //Find a library record by id for reservation
        LibraryRecord libraryRecord = libraryRepository.findById(id).orElseThrow(RecordNotFoundException::new);

        //If book is already reserved it can't be re-reserved
        if (libraryRecord.getReservedDate() != null) {
            logger.info("updateReserved:Conflict -> Book is already reserved");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        //set books reservation and return dates
        libraryRecord.setId(id);
        Date dateRes = new Date();
        Date dateRet = new Date(dateRes.getTime() + 2678400000L);
        libraryRecord.setReservedDate(dateRes);
        libraryRecord.setReturnDate(dateRet);

        //Save updated book record
        libraryRepository.save(libraryRecord);
        logger.info("updateReserved:end -> Id:{} - OK", id);
        return mapper.toDto(libraryRecord);
    }
    //TODO: update method updateAvailable to check if book is already available (?)
    public LibraryRecordResponse updateAvailable(int id) {
        logger.info("updateAvailable:start -> Id:{}", id);
        LibraryRecord libraryRecord = libraryRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        libraryRecord.setId(id);
        libraryRecord.setReservedDate(null);
        libraryRecord.setReturnDate(null);
        libraryRepository.save(libraryRecord);
        logger.info("updateAvailable:end -> Id:{} - OK", id);
        return mapper.toDto(libraryRecord);
    }

    public LibraryRecordResponse addNewAvailableBook(int id) {
        logger.info("addNewAvailableBook:start -> Id:{}", id);

        //Check if book record already exists
        boolean libraryRecordExists = libraryRepository.existsById(id);
        if (libraryRecordExists) {
            logger.info("addNewAvailableBook:Conflict -> Library record already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        //Add new record to Library
        LibraryRecord libraryRecord = new LibraryRecord(id, null, null);
        libraryRepository.save(libraryRecord);

        logger.info("addNewAvailableBook:end -> Record:{}", libraryRecord);
        return mapper.toDto(libraryRecord);
    }

    public List<LibraryRecordResponse> getAllRecords(){
        logger.info("getAllRecords:start");

        //Get a list of all books (used for data synchronization)
        List<LibraryRecord> records = libraryRepository.findAll();
        return records.stream().map(mapper::toDto).toList();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Element does not exist")
    static class RecordNotFoundException extends NoSuchElementException {
    }

}
