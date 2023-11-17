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

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    private final RecordDto mapper;

    Logger logger = LoggerFactory.getLogger(LibraryService.class);

    public LibraryRecordResponse deleteRecord(int id) {
        logger.info("deleteRecord:start -> Id:{}", id);
        LibraryRecord libraryRecord = libraryRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        if (libraryRecord.getReservedDate() != null) {
            logger.info("deleteRecord:Conflict -> Book is reserved and can't be deleted");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        libraryRepository.delete(libraryRecord);
        logger.info("deleteRecord:end -> Id:{} - OK", id);
        return mapper.toDto(libraryRecord);
    }

    public List<LibraryRecordResponse> getReservedRecords() {
        logger.info("getReservedRecords:start");
        List<LibraryRecord> records = libraryRepository.findAllByReservedDateIsNotNull();
        logger.info("getReservedRecords:end");
        return records.stream().map(mapper::toDto).toList();
    }

    public List<LibraryRecordResponse> getAvailableRecords() {
        logger.info("getAvailableRecords:start");
        List<LibraryRecord> records = libraryRepository.findAllByReservedDateIsNull();
        logger.info("getAvailableRecords:end");
        return records.stream().map(mapper::toDto).toList();
    }


    public LibraryRecordResponse updateReserved(int id) {
        logger.info("updateReserved:start -> Id:{}", id);
        LibraryRecord libraryRecord = libraryRepository.findById(id).orElseThrow(RecordNotFoundException::new);
        if (libraryRecord.getReservedDate() != null) {
            logger.info("updateReserved:Conflict -> Book is already reserved");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        libraryRecord.setId(id);
        Date dateRes = new Date();
        Date dateRet = new Date(dateRes.getTime() + 2678400000L);
        libraryRecord.setReservedDate(dateRes);
        libraryRecord.setReturnDate(dateRet);
        libraryRepository.save(libraryRecord);
        logger.info("updateReserved:end -> Id:{} - OK", id);
        return mapper.toDto(libraryRecord);
    }

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
        boolean libraryRecordExists = libraryRepository.existsById(id);
        if (libraryRecordExists) {
            logger.info("addNewAvailableBook:Conflict -> Library record already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        LibraryRecord libraryRecord = new LibraryRecord(id, null, null);
        libraryRepository.save(libraryRecord);
        logger.info("addNewAvailableBook:end -> Record:{}", libraryRecord);
        return mapper.toDto(libraryRecord);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Element does not exist")
    static class RecordNotFoundException extends NoSuchElementException {
    }

}
