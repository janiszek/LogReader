package com.janiszewski.logreader;

import com.janiszewski.logreader.component.FileStreamReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FileStreamReaderTests {
    @Autowired
    private FileStreamReader fileReader;

    @Test
    @DisplayName("FileStreamReader class - all methods without execute")
    void testExecute() {
        String[] arguments = {"logfile.txt"};
        boolean result = false;
        if (fileReader.init(arguments)) {
            //fileReader.execute();
            result = fileReader.showResults();
            fileReader.finish();
        }
        assertThat(result);
    }
}
