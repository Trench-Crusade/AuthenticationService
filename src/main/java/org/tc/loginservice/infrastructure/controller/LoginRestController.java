package org.tc.loginservice.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tc.loginservice.core.ports.internal.LogInPort;
import org.tc.loginservice.core.ports.internal.RegisterPort;
import org.tc.loginservice.infrastructure.controller.dto.request.LoginRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.request.RegisterRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.RegisterResponseDto;
import org.tc.loginservice.infrastructure.controller.dto.response.TokenResponseDto;
import org.tc.loginservice.shared.exceptions.TCEntityNotFoundException;
import org.tc.loginservice.shared.exceptions.TCInvalidRequestDataException;
import org.tc.loginservice.shared.exceptions.TCTokenProvidedException;

import java.util.Objects;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginRestController {

    private final LogInPort logInUseCase;
    private final RegisterPort registerUseCase;
    @PostMapping
    public ResponseEntity<RegisterResponseDto> register(RegisterRequestDto registerRequestDto){
        return new ResponseEntity<>(registerUseCase.register(registerRequestDto), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<TokenResponseDto> logIn(HttpServletRequest request, LoginRequestDto loginRequestDto) throws TCEntityNotFoundException, TCInvalidRequestDataException, TCTokenProvidedException {
        if(Objects.nonNull(request.getHeader("Authorization"))){
            throw new TCTokenProvidedException("Another token has been provided");
        }
        return new ResponseEntity<>(logInUseCase.logIn(loginRequestDto), HttpStatus.OK);
    }
}
