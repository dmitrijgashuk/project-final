package com.javarush.jira.bugtracking.internal.web;

import com.javarush.jira.bugtracking.internal.model.Tag;
import com.javarush.jira.bugtracking.internal.repository.TagRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping(value = TagRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TagRestController {
    public static final String REST_URL = "/api/tags";
    @Autowired
    private TagRepository tagRepository;

    @GetMapping(value = "/{id}")
    public Set<Tag> getAllTagsByTaskId(@PathVariable Long id) {
        return tagRepository.getAllTaskTags(id);
    }

    public void create(@RequestBody Tag tag) {

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Tag tag) {

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@RequestBody Tag tag) {

    }
}
