package ru.practicum.fileCSV;

import org.junit.jupiter.api.BeforeEach;
import ru.practicum.memory.TaskManagerTest;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest  extends TaskManagerTest<FileBackedTaskManager> {
    private static File file;

    @BeforeEach
    public void createTest() throws IOException {
        //file = File.createTempFile("test", "txt");
        file = new File("src\\test\\java\\ru\\practicum\\fileCSV\\test.txt");
        manager = new FileBackedTaskManager(file);
    }
}