package com.janiszewski.logreader;

import com.google.gson.Gson;
import com.janiszewski.logreader.component.EventRecord;
import com.janiszewski.logreader.component.LogJsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EventRecordTests {
    private  final static String lineStart = "{\"id\":\"abc\", \"state\":\"STARTED\", \"timestamp\":1491377495210}";
    private  final static String lineFinish = "{\"id\":\"abc\", \"state\":\"FINISHED\", \"timestamp\":1491377495216}";

    private LogJsonHelper logEventStart;
    private LogJsonHelper logEventFinish;

    @BeforeEach
    public void setUp() {
        Gson gson = new Gson();
        logEventStart = gson.fromJson(lineStart, LogJsonHelper.class);
        logEventFinish = gson.fromJson(lineFinish, LogJsonHelper.class);
    }

    @Test
    @DisplayName("EventRecord class: insert method")
    void testInsert() {
        EventRecord currentRec = new EventRecord(logEventFinish);
        assertThat(currentRec.getStateEnd().equals("FINISHED"));
        assertThat(currentRec.getTimestampEnd().equals(new BigInteger("1491377495216")));
    }

    @Test
    @DisplayName("EventRecord class: adding two pairing events sets the EventRecord to the proper state")
    void testInsertUpdate() {
        EventRecord currentRec = new EventRecord(logEventStart);
        currentRec.updateWithPair(logEventFinish);
        assertThat(currentRec.getAlert());
        assertThat(currentRec.getEventDuration()==6);
    }

    @Test
    @DisplayName("EventRecord class: method isEventPaired returning false for the same event state sent twice")
    void testIsEventPaired() {
        EventRecord currentRec = new EventRecord(logEventStart);
        currentRec.updateWithPair(logEventStart);
        assertThat(!currentRec.isEventPaired());
    }

}
