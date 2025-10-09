package ru.practicum.memory;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.controller.Managers;
import ru.practicum.model.Epic;
import ru.practicum.model.Subtask;
import ru.practicum.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.model.Status.DONE;
import static ru.practicum.model.Status.NEW;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void createTest() {
        manager = (InMemoryTaskManager) Managers.getDefault();
    }


}