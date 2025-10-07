package ru.practicum.memory;


import org.junit.jupiter.api.BeforeEach;
import ru.practicum.controller.Managers;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void createTest() {
        manager = (InMemoryTaskManager) Managers.getDefault();
    }
}