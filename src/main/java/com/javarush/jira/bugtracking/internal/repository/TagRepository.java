package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.internal.model.Tag;
import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Repository
public interface TagRepository extends BaseRepository<Tag> {
    @Transactional
    @Query(value = "SELECT * FROM tags t WHERE t.task_id=:id", nativeQuery = true)
    public Set<Tag> getAllTaskTags(Long id);
}
