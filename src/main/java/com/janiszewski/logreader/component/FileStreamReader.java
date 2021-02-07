package com.janiszewski.logreader.component;

import com.google.gson.Gson;
import com.janiszewski.logreader.entity.LogRecord;
import com.janiszewski.logreader.repository.LogRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class FileStreamReader {
    private final static int THREAD_POOL_SIZE = 10;
    private String fileStreamPath;

    @Autowired
    private final LogRecordRepository logRecordRepo;
    @Autowired
    private final LogMapManager logManager;

    public FileStreamReader(LogRecordRepository logRecordRepo, LogMapManager logManager) {
        this.logRecordRepo = logRecordRepo;
        this.logManager = logManager;
        this.fileStreamPath = "";
    }

    public boolean init(String[] args){
        boolean result = false;
        if (args.length > 0){
            File file = new File(args[0]);
            if (file.exists()) {
                fileStreamPath = args[0];
                logManager.initMap();
                result = true;
            }
            else
                log.error("ERROR: The argument does not specify a valid file");
        }
        else
            log.error("ERROR: Please provide the path to the log file in the Command line params");
        return result;
    }

    public void finish(){
        logManager.clearMap();
    }

    public void execute() throws Exception {
        if (!fileStreamPath.isEmpty())
            this.readStreamFile(fileStreamPath);
    }

    private void readJsonLine(String line) {
        Gson gson = new Gson();
        LogJsonHelper logEvent = gson.fromJson(line, LogJsonHelper.class);

        if (logManager.addEvent(logEvent)){
            // 2 events happened, save to the database
            LogRecord logRec = new LogRecord(logManager.findEventRecord(logEvent.getId()));
            logRecordRepo.save(logRec);
            logManager.removeEventRecord(logEvent.getId());
        }
    }

    private void readStreamFile(String filePath) throws Exception {
        try (FileInputStream inputStream = new FileInputStream(filePath); Scanner sc = new Scanner(inputStream, "UTF-8")) {
            //multi-threaded processing of stream lines
            ExecutorService streamExServer = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            try {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    streamExServer.execute(() -> readJsonLine(line));
                }
                // Scanner suppresses exceptions
                if (sc.ioException() != null)
                    throw sc.ioException();
            } finally {
                streamExServer.shutdown();
                try {
                    if (!streamExServer.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS))
                        streamExServer.shutdownNow();
                } catch (InterruptedException e) {
                    streamExServer.shutdownNow();
                }
            }
        } finally {
            logRecordRepo.flush();
        }
    }

    public boolean showResults() {
        //DEBUG - for control list all records from the database
        Page<LogRecord> logPage;
        log.debug("Showing all records from the database: ");

        //as amount of records might be big, make it pagable
        Pageable pageRequest = PageRequest.of(0, 10);
        logPage = logRecordRepo.findAll(pageRequest);
        while (!logPage.isEmpty()) {
            pageRequest = pageRequest.next();
            //logPage.forEach(System.out::println);
            logPage.forEach(s->log.debug(String.valueOf(s)));
            logPage = logRecordRepo.findAll(pageRequest);
        }

        return logManager.validateStatus();
    }
}
