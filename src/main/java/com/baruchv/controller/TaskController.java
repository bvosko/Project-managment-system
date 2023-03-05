package com.baruchv.controller;

import com.baruchv.model.Task;
import com.baruchv.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/tasks")
public class TaskController {

    private TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    // all tasks
    @GetMapping("/tasks/list")
    @ApiOperation(value = "To get all list",response = Task.class,code = 200)
    public Iterable<Task> list() {
        return service.list();
    }

    //new task
    @PostMapping("/tasks")
    @ApiOperation(value = "Add new task")
    Task newEmployee(@RequestBody Task newTask) {
        return service.saveNewTask(newTask);
    }

    //Task by Uid
    @GetMapping("/tasks/{uid}")
    @ApiOperation(value = "Find task or project by uid")
    Task oneTask(@PathVariable String uid) {
        return service.findTask(uid);
    }

    //completion status task by Uid
    @GetMapping("/tasks/{uid}/{date}")
    @ApiOperation(value = "See completion status task or project by uid and date ('YYYY-MM-DD')")
    String status(@PathVariable String uid,@PathVariable String date) {
        return service.statusTask(uid,date);
    }

    //Update Date of task
    @PutMapping("/tasks/{uid}")
    @ApiOperation(value = "Update startDate ot endDate (another update ignored")
    Task updateTask(@RequestBody Task newTask, @PathVariable String uid) {
        return service.updateDateTask(newTask,uid);
    }

    // delete by Uid
    @DeleteMapping("/tasks/{uid}")
    @ApiOperation(value = "Delete by uid ( if that last task , not deleted , run exception) ")
    public void delete(@PathVariable String uid) {
        service.deleteTask(uid);
    }
}

