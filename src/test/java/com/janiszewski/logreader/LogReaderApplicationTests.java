package com.janiszewski.logreader;

import com.janiszewski.logreader.component.FileStreamReader;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class LogReaderApplicationTests {

	@Autowired
	private FileStreamReader fileReader;

	@Test
	void contextLoads() {
	}
}
