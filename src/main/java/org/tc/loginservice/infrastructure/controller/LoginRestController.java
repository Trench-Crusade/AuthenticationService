package org.tc.loginservice.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tc.loginservice.core.ports.internal.ActivateUserPort;
import org.tc.loginservice.core.ports.internal.LogInPort;
import org.tc.loginservice.core.ports.internal.RegisterPort;
import org.tc.loginservice.infrastructure.controller.dto.request.ActivateUserRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.request.LoginRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.request.RegisterRequestDto;
import org.tc.loginservice.infrastructure.controller.dto.response.ActivateUserResponseDto;
import org.tc.loginservice.infrastructure.controller.dto.response.RegisterResponseDto;
import org.tc.loginservice.infrastructure.controller.dto.response.TokenResponseDto;
import org.tc.loginservice.shared.exceptions.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginRestController {

    private final LogInPort logInUseCase;
    private final RegisterPort registerUseCase;
    private final ActivateUserPort activateUserPort;

    @PostMapping(value = "register")
    public ResponseEntity<RegisterResponseDto> register(RegisterRequestDto registerRequestDto) throws TCEmailSendingFailedException, TCRegistrationFailedException, TCInvalidRequestDataException {
        return new ResponseEntity<>(registerUseCase.register(registerRequestDto), HttpStatus.OK);
    }
    @PostMapping(value="login")
    public ResponseEntity<TokenResponseDto> logIn(HttpServletRequest request, LoginRequestDto loginRequestDto) throws TCEntityNotFoundException, TCInvalidRequestDataException, TCTokenProvidedException, TCInsertFailedException, TCIUpdateFailedException {
        if(Objects.nonNull(request.getHeader("Authorization"))){
            throw new TCTokenProvidedException("Another token has been provided");
        }
        return new ResponseEntity<>(logInUseCase.logIn(loginRequestDto), HttpStatus.OK);
    }

    @GetMapping(value="activate/{validateToken}")
    public ResponseEntity<ActivateUserResponseDto> activate(HttpServletRequest request, @PathVariable UUID validateToken) throws TCTokenProvidedException, TCEntityNotFoundException, TCAccessDeniedException, TCIUpdateFailedException, TCInvalidRequestDataException {
        if(Objects.nonNull(request.getHeader("Authorization"))){
            throw new TCTokenProvidedException("Token has been provided");
        }
        ActivateUserRequestDto activateUserRequestDto = new ActivateUserRequestDto(validateToken);
        return new ResponseEntity<>(activateUserPort.activateUser(request, activateUserRequestDto), HttpStatus.OK);
    }
}
