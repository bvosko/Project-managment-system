package com.baruchv.service;

import com.baruchv.exception.TaskNotFoundException;
import com.baruchv.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TaskServiceTest {
    Task task1 = new Task();
    Task project1 = new Task();

    Task testTask = new Task();

    @BeforeEach
    void init() {

        task1.setUid("123abcde456");
        task1.setName("name1");
        task1.setType("TASK");
        task1.setStartDate(java.sql.Date.valueOf("2021-10-02"));
        task1.setEndDate(java.sql.Date.valueOf("2021-10-11"));
        task1.setParentUid("987abcde654");

        project1.setUid("987abcde654");
        project1.setName("name1");
        project1.setType("TASK");
        project1.setStartDate(java.sql.Date.valueOf("2021-10-02"));
        project1.setEndDate(java.sql.Date.valueOf("2021-10-11"));
        project1.setParentUid("111");
    }

    @Autowired
    TaskService service;

    @Test
    void save() {
        service.save(task1);
        testTask = service.findTask(task1.getUid());
        assertEquals(testTask.getUid(), task1.getUid());
    }

    @Test
    void saveNewTask() {
        service.save(project1);
        testTask = service.findTask(project1.getUid());
        assertEquals(testTask.getUid(), project1.getUid());
    }

    @Test
    void statusTask() {

        String result = service.statusTask(task1.getUid(), "2021-10-10");
        String test_string = "At 2021-10-10:80.0 %";
        assertEquals(test_string, result);
    }

    @Test
    void findTask() {
        service.save(task1);
        Task task = service.findTask("123abcde456");
        assertEquals(task, task1);
    }

}