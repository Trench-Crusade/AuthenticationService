package org.tc.authservice.infrastructure.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.tc.authservice.infrastructure.api.dto.request.LoginRequestDto;
import org.tc.infrastructure.postgres.entities.UserEntity;
import org.tc.infrastructure.postgres.enums.AccountStatus;
import org.tc.infrastructure.postgres.enums.AccountType;
import org.tc.infrastructure.postgres.enums.LanguageEnum;
import org.tc.authservice.infrastructure.postgres.repositories.UserActivationRepository;
import org.tc.authservice.infrastructure.postgres.repositories.UserRepository;
import org.tc.authservice.infrastructure.redis.repositories.TokenBlacklistEntryRepository;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginRestControllerIT {

    ObjectMapper objectMapper;
    ObjectWriter objectWriter;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserActivationRepository userActivationRepository;

    @Autowired
    TokenBlacklistEntryRepository tokenBlacklistEntryRepository;

    @Autowired
    private MockMvc mvc;

    private void configureObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

    }

    @Test
    public void loginForNonExistentUserAndGetNotFound() throws Exception{
        configureObjectMapper();
        String requestJson = objectWriter.writeValueAsString(new LoginRequestDto("email", "password"));
        mvc.perform(MockMvcRequestBuilders
                        .post("/authentication/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void loginWithTokenAndGetForbidden() throws Exception{
        configureObjectMapper();
        String requestJson = objectWriter.writeValueAsString(new LoginRequestDto("email", "password"));
        mvc.perform(MockMvcRequestBuilders
                        .post("/authentication/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .header("Authorization", "a")
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void loginWithWrongPasswordAndGetBadRequest() throws Exception{
        configureObjectMapper();
        String requestJson = objectWriter.writeValueAsString(new LoginRequestDto("test@email", "password"));
        userRepository.save(new UserEntity(
                null,
                UUID.randomUUID(),
                "username",
                "test@email",
                AccountType.PLAYER,
                LanguageEnum.EN,
                AccountStatus.ACTIVE,
                "1",
                null,
                null,
                null

        ));
        mvc.perform(MockMvcRequestBuilders
                        .post("/authentication/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginWithCorrectPasswordAndGetOK() throws Exception{
        configureObjectMapper();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String requestJson = objectWriter.writeValueAsString(new LoginRequestDto("test@email", "password"));
        userRepository.save(new UserEntity(
                null,
                UUID.randomUUID(),
                "username",
                "test@email",
                AccountType.PLAYER,
                LanguageEnum.EN,
                AccountStatus.ACTIVE,
                bCryptPasswordEncoder.encode("password"),
                null,
                null,
                null

        ));
        mvc.perform(MockMvcRequestBuilders
                        .post("/authentication/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
