package com.janiszewski.logreader.entity;

import com.janiszewski.logreader.component.EventRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable=false)
    private String eventId;
    private Integer eventDuration;
    private String type;
    private String host;
    private Boolean alert;

    public LogRecord(EventRecord log) {
        this.eventId = log.getEventId();
        this.eventDuration = log.getEventDuration();
        this.type = log.getType();
        this.host = log.getHost();
        this.alert = log.getAlert();
    }
}
