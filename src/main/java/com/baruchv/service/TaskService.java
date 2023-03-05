package com.baruchv.service;

import com.baruchv.exception.NotFoundParent;
import com.baruchv.exception.TaskNotFoundException;
import com.baruchv.model.Task;
import com.baruchv.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Iterable<Task> list() {
        return repository.findAll();
    }

    public Task save(Task task) {
        return repository.save(task);
    }

    public void save(List<Task> tasks) {
        repository.saveAll(tasks);
    }

    public Task saveNewTask(Task newTask) {
        Task parent;
        try {
            parent = repository.findByUid(newTask.getParentUid()).get(0);
        } catch (Exception e) {
            throw new NotFoundParent(newTask.getParentUid());
        }
        if (parent.getType().equals("TASK"))
            throw new NotFoundParent(parent.getUid());
        Task task = repository.save(newTask);
        updateStatus(task);
        return task;
    }

    public String statusTask(String Uid, String date) {
        LocalDate dt = LocalDate.parse(date);
        Task task;
        try {
            task = repository.findByUid(Uid).get(0);
        } catch (Exception e) {
            throw new TaskNotFoundException(Uid);
        }
        LocalDate startDate = task.getStartDate().toLocalDate();
        LocalDate endDate = task.getEndDate().toLocalDate();

        if (dt.isBefore(startDate))
            return "At " + dt + ": 0% (not started yet)";
        if (dt.equals(startDate))
            return "At " + dt + ": 0% (just started)";
        if (dt.isAfter(endDate))
            return "At " + dt + ": 100% (finished)";
        double procent = 100 / (ChronoUnit.DAYS.between(startDate, endDate) + 1.0) * ChronoUnit.DAYS.between(startDate, dt);
        procent = Double.parseDouble(new DecimalFormat("##.##").format(procent));
        return "At " + dt + ":" + procent + " % ";
    }

    public Task updateDateTask(Task newTask, String uid) {
        Task task;
        try {
            task = repository.findByUid(uid).get(0);
        } catch (Exception e) {
            throw new TaskNotFoundException(uid);
        }
        task.setStartDate(newTask.getStartDate());
        task.setEndDate(newTask.getEndDate());
        task = repository.save(task);
        updateStatusWithChange(task);
        return task;
    }

    public void deleteTask(String uid) {
        Task task;
        try {
            task = repository.findByUid(uid).get(0);
            if (repository.findCountChildren(task.getParentUid()) > 1) {
                repository.deleteById(task.getId());
                updateStatusWithChange(task);
            } else
                System.out.println("The last task can’t be deleted");
        } catch (Exception e) {
            throw new TaskNotFoundException(uid);
        }
    }

    public Task findTask(String uid) {
        Task task;
        try {
            task = repository.findByUid(uid).get(0);
        } catch (Exception e) {
            throw new TaskNotFoundException(uid);
        }
        return task;
    }

    public void updateStatusWithChange(Task task) {
        Task parent = repository.findByUid(task.getParentUid()).get(0);
        List<Task> children = repository.findAllChildren(parent.getUid());
        LocalDate startDate = children.get(0).getStartDate().toLocalDate();
        LocalDate endDate = children.get(0).getEndDate().toLocalDate();
        for (Task t : children) {
            if (t.getStartDate().toLocalDate().isBefore(startDate))
                startDate = t.getStartDate().toLocalDate();
            if (t.getEndDate().toLocalDate().isAfter(endDate))
                endDate = t.getEndDate().toLocalDate();
        }
        task = children.get(0);
        while (true) {
            List<Task> parents = repository.findByUid(task.getParentUid());
            boolean date1_start;
            boolean date1_end;
            try {
                LocalDate startDate_parent = parents.get(0).getStartDate().toLocalDate();
                date1_start = startDate.isBefore(startDate_parent);
            } catch (NullPointerException e) {
                date1_start = true;
            }
            try {
                LocalDate endDate_parent = parents.get(0).getEndDate().toLocalDate();
                date1_end = endDate.isAfter(endDate_parent);
            } catch (NullPointerException e) {
                date1_end = true;
            }
            if (!date1_end && !date1_start)
                break;
            else {
                if (date1_start) {
                    parents.get(0).setStartDate(java.sql.Date.valueOf(startDate));
                    repository.save(parents.get(0));
                }
                if (date1_end) {
                    parents.get(0).setEndDate(java.sql.Date.valueOf(endDate));
                    repository.save(parents.get(0));
                }
                if (parents.get(0).getParentUid() == null)
                    break;
                task = parents.get(0);
            }
        }

    }

    public void updateStatus(Task task) {
        if (task.getType().equals("TASK")) {
            while (true) {
                LocalDate startDate = task.getStartDate().toLocalDate();
                LocalDate endDate = task.getEndDate().toLocalDate();
                String parentId = task.getParentUid();

                List<Task> parent = repository.findByUid(parentId);
                boolean date1_start;
                boolean date1_end;
                try {
                    LocalDate startDate_parent = parent.get(0).getStartDate().toLocalDate();
                    date1_start = startDate.isBefore(startDate_parent);
                } catch (NullPointerException e) {
                    date1_start = true;
                }
                try {
                    LocalDate endDate_parent = parent.get(0).getEndDate().toLocalDate();
                    date1_end = endDate.isAfter(endDate_parent);
                } catch (NullPointerException e) {
                    date1_end = true;
                }

                if (!date1_end && !date1_start)
                    break;
                else {
                    if (date1_start) {
                        parent.get(0).setStartDate(java.sql.Date.valueOf(startDate));
                        repository.save(parent.get(0));
                    }
                    if (date1_end) {
                        parent.get(0).setEndDate(java.sql.Date.valueOf(endDate));
                        repository.save(parent.get(0));
                    }
                    if (parent.get(0).getParentUid() == null)
                        break;
                    task = parent.get(0);
                }
            }
        }
    }
}




