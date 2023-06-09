package com.javarush.jira.profile.web;

import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yaml")
@AutoConfigureWebTestClient
class ProfileRestControllerTest {
    @Autowired
    public ProfileRestController profileRestController;
    @Autowired
    public ProfileRepository profileRepository;
    @Autowired
    public ProfileMapper profileMapper;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public WebTestClient webTestClient;


    @Test
    public void check_profiler_get_request(){
        EntityExchangeResult<ProfileTo> profileToEntityExchangeResult = webTestClient
                //.mutateWith(SecurityMockServerConfigurers.mockAuthentication(new UsernamePasswordAuthenticationToken("admin@gmail.com","admin")))
                .get()
                .uri("/api/profile")
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(ProfileTo.class)
                .returnResult();

        Assertions.assertInstanceOf(ProfileTo.class,profileToEntityExchangeResult.getResponseBody());


           /*     EntityExchangeResult<List<Customer>> result = this.webTestClient
                .get()
                .uri("/api/customers")
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Customer.class)
                .returnResult();

        assertThat(result.getResponseBody())
                .hasSizeGreaterThan(1);*/
    }


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