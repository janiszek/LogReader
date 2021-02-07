package com.janiszewski.logreader.repository;

import com.janiszewski.logreader.entity.LogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogRecordRepository extends JpaRepository<LogRecord,Long> {
    Optional<LogRecord> findOneByEventId(String eventId);
}
