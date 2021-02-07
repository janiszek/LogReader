package com.janiszewski.logreader;

import com.google.gson.Gson;
import com.janiszewski.logreader.component.EventRecord;
import com.janiszewski.logreader.component.LogJsonHelper;
import com.janiszewski.logreader.entity.LogRecord;
import com.janiszewski.logreader.repository.LogRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LogRecordRepositoryIntegrationTests {

    @Autowired
    private LogRecordRepository logRecordRepo;

    private  final static String line = "{\"id\":\"abs\", \"state\":\"FINISHED\", \"timestamp\":1491377495216}";
    private LogJsonHelper logEvent;

    @BeforeEach
    public void setUp() {
        Gson gson = new Gson();
        logEvent = gson.fromJson(line, LogJsonHelper.class);
    }

    @Test
    @DisplayName("LogRecordRepository class integration: check if saving and retrieving a record from the database works properly")
    public void testSaveAndFind() {
        EventRecord currentRec = new EventRecord(logEvent);
        LogRecord newRec = new LogRecord(currentRec);
        logRecordRepo.save(newRec);
        LogRecord foundRec = logRecordRepo.findOneByEventId("abs").orElse(null);
        assertThat(foundRec).isEqualTo(newRec);
    }
}
