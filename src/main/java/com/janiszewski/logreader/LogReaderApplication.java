package com.janiszewski.logreader;

import com.janiszewski.logreader.component.FileStreamReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
@SpringBootApplication
public class LogReaderApplication implements CommandLineRunner {

	@Autowired
	private final FileStreamReader fileReader;

	public static void main(String[] args) {
		SpringApplication.run(LogReaderApplication.class, args);
	}


	@Override
	public void run(String[] args) throws Exception {
		//Logger log = LoggerFactory.getLogger(LogReaderApplication.class);

		log.info("Application LogReader started...");
		if (fileReader.init(args)) {
			fileReader.execute();
			fileReader.showResults();
			fileReader.finish();
		}
		log.info("Application LogReader finished...");
	}
}
