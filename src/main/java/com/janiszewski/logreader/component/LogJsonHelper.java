package com.janiszewski.logreader.component;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.math.BigInteger;


@Component
@Getter
@ToString
public class LogJsonHelper {
    private String id;
    private String state;
    private BigInteger timestamp;
    private String type;
    private String host;
}
