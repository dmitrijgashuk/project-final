package com.javarush.jira.profile.web;

import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest()
@ActiveProfiles("test")
//@TestPropertySource("classpath:application-test.yaml")
class ProfileRestControllerTest {
    @Autowired
    public ProfileRestController profileRestController;
    @Autowired
    public ProfileRepository profileRepository;
    @Autowired
    public ProfileMapper profileMapper;

    @Autowired
    public UserRepository userRepository;


    @Test
    @Transactional(readOnly = true)
    void get_profile_by_authUser() {
        User existAdminUser = userRepository.getExistedByEmail("admin@gmail.com");
        AuthUser authUser = new AuthUser(existAdminUser);

        ProfileTo expected = profileMapper.toTo(profileRepository.getById(authUser.id()));

        ProfileTo current = profileRestController.get(authUser);

        assertNotNull(current);
        assertEquals(expected, current);
    }

    @Test
    @Transactional
    void update() {
        User existAdminUser = userRepository.getExistedByEmail("admin@gmail.com");
        AuthUser authUser = new AuthUser(existAdminUser);

        ProfileTo updatable = profileMapper.toTo(profileRepository.getById(authUser.id()));

        updatable.setMailNotifications(Set.of("assigned","three_days_before_deadline"));

        profileRestController.update(updatable,authUser);

        ProfileTo profileTo = profileRestController.get(authUser);

        assertEquals(updatable,profileTo);
    }

}