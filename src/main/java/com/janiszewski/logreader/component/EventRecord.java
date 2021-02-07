package com.janiszewski.logreader.component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@Getter
@ToString
@NoArgsConstructor
public class EventRecord {
    private static final String EVENT_STARTED = "STARTED";
    private static final int ALERT_DURATION = 4;


    private String eventId;
    private String stateStart;
    private String stateEnd;
    private BigInteger timestampStart;
    private BigInteger timestampEnd;
    private Integer eventDuration;
    private String type;
    private String host;
    private Boolean alert;

    public EventRecord(LogJsonHelper logEvent) {
        this.eventId = logEvent.getId();
        this.setStartEnd(logEvent.getState(), logEvent.getTimestamp());
        this.type = logEvent.getType();
        this.host = logEvent.getHost();
        this.alert = false;
    }

    public void updateWithPair (LogJsonHelper logEvent) {
        this.setStartEnd(logEvent.getState(), logEvent.getTimestamp());
        calculateAlert();
    }

    private void setStartEnd (String logState, BigInteger logTimeStamp){
        if (logState.equals(EVENT_STARTED)) {
            this.stateStart = logState;
            this.timestampStart = logTimeStamp;
        }
        else {
            this.stateEnd = logState;
            this.timestampEnd = logTimeStamp;
        }
    }

    private void calculateAlert() {
        if ((timestampStart!=null) && (timestampEnd!=null)){
            this.eventDuration = this.timestampEnd.subtract(this.timestampStart).intValue();
            this.alert = (this.eventDuration.compareTo(ALERT_DURATION) >0);
        }
    }

    public boolean isEventPaired(){
        return (stateStart!=null && stateEnd!=null && eventDuration!=null);
    }

}
