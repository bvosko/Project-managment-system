package com.baruchv.repository;

import com.baruchv.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findByUid(String Uid);

    @Query(value = "SELECT count(*) FROM Task t WHERE t.parent_uid = ?1",nativeQuery = true)
    int findCountChildren(String Uid);

    @Query(value = "SELECT * FROM Task t WHERE t.parent_uid = ?1",nativeQuery = true)
    List<Task> findAllChildren(String Uid);
}
