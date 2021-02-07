package com.janiszewski.logreader.component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@AllArgsConstructor
public class LogMapManager {

    //store in memory events which are "open" - waiting for 2nd from the pair to arrive
    private Map<String, EventRecord> logRecMap;

    public void initMap(){
        logRecMap = new ConcurrentHashMap<>();
    }

    public void clearMap(){
        logRecMap.clear();
    }

    //add event to the list, return true if this is a matching event
    public boolean addEvent(LogJsonHelper logEvent) {
        EventRecord currentRec;
        boolean matchFound;

        //different threads manipulate the map so synchronize it
        synchronized (logRecMap) {
            currentRec = logRecMap.get(logEvent.getId());
            matchFound = (currentRec != null);
            if (matchFound) {
                currentRec.updateWithPair(logEvent);
                //protect against same state being sent twice
                if (!currentRec.isEventPaired())
                    matchFound = false;
            }
            else {
                //add new event record to the Map
                currentRec = new EventRecord(logEvent);
                logRecMap.put(logEvent.getId(), currentRec);
            }
        }
        return matchFound;
    }

    public void removeEventRecord(String eventId) {
        logRecMap.remove(eventId);
    }

    public EventRecord findEventRecord(String eventId) {
        return logRecMap.get(eventId);
    }

    //if the list is not empty (some events not matched), then dump the content to the log - should be part of the tests
    public boolean validateStatus() {
        boolean result = false;
        //ERROR - there should be no unmatched events left
        if ((logRecMap!=null) && (logRecMap.size() > 0)) {
            result = true;
            log.error("ERROR: Unmatched events left in memory: ");
            logRecMap.forEach((key, value) -> log.error(key + ' ' + value));
        }
        return result;
    }
}
