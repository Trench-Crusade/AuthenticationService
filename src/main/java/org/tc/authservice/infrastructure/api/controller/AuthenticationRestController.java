package org.tc.authservice.infrastructure.api.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tc.authservice.core.ports.internal.*;
import org.tc.authservice.infrastructure.api.dto.request.ActivateUserByTokenRequestDto;
import org.tc.authservice.infrastructure.api.dto.request.LoginRequestDto;
import org.tc.authservice.infrastructure.api.dto.request.RegisterRequestDto;
import org.tc.authservice.infrastructure.api.dto.response.*;
import org.tc.authservice.shared.consts.ConstValues;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenExpiredException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenMalformedException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenNotProvidedException;
import org.tc.authservice.shared.exceptions.access.detailed.TCTokenProvidedException;
import org.tc.authservice.shared.exceptions.api.detailed.TCInvalidRequestDataException;
import org.tc.authservice.shared.exceptions.general.detailed.TCIllegalStateException;
import org.tc.authservice.shared.exceptions.out.detailed.TCEmailSendingFailedException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCEntityNotFoundException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCInsertFailedException;
import org.tc.authservice.shared.exceptions.persistence.detailed.TCUpdateFailedException;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("authentication")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final LogInPort logInUseCase;
    private final RegisterPort registerUseCase;
    private final ActivateUserByRequestPort activateUserByRequestPort;
    private final ActivateUserByLinkPort activateUserByLinkPort;
    private final ValidateProvidedTokenPort validateProvidedTokenPort;
    private final LogoutPort logoutPort;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegisterResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Provided passwords did not match or security could not be set for user"),
            @ApiResponse(responseCode = "403", description = "Token has been provided"),
            @ApiResponse(responseCode = "424", description = "Database operation failure or email sending failure"),
    })
    @PostMapping(value = "register")
    public ResponseEntity<RegisterResponseDto> register(HttpServletRequest request, RegisterRequestDto registerRequestDto) throws TCTokenProvidedException, TCInsertFailedException, TCEmailSendingFailedException, TCInvalidRequestDataException, TCIllegalStateException {
        checkIfNoTokenProvided(request);
        return new ResponseEntity<>(registerUseCase.register(registerRequestDto), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Wrong password provided"),
            @ApiResponse(responseCode = "403", description = "Token has been provided"),
            @ApiResponse(responseCode = "424", description = "Database operation failure"),
    })
    @PostMapping(value = "login")
    public ResponseEntity<TokenResponseDto> logIn(HttpServletRequest request, @RequestBody LoginRequestDto loginRequestDto) throws TCEntityNotFoundException, TCInvalidRequestDataException, TCTokenProvidedException, TCInsertFailedException, TCUpdateFailedException {
        checkIfNoTokenProvided(request);
        return new ResponseEntity<>(logInUseCase.logIn(request, loginRequestDto), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ActivateUserResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "Token has been provided"),
            @ApiResponse(responseCode = "424", description = "Database operation failure"),
    })
    @GetMapping(value = "activation/{validateToken}")
    public ResponseEntity<ActivateUserResponseDto> activateByLink(HttpServletRequest request, @PathVariable UUID validateToken) throws TCEntityNotFoundException, TCUpdateFailedException, TCInvalidRequestDataException, TCTokenProvidedException {
        checkIfNoTokenProvided(request);
        ActivateUserByTokenRequestDto activateUserByTokenRequestDto = new ActivateUserByTokenRequestDto(validateToken);
        return new ResponseEntity<>(activateUserByLinkPort.activateUser(activateUserByTokenRequestDto), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ActivateUserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Token belonging to another user has been provided"),
            @ApiResponse(responseCode = "403", description = "Token has not been provided or provided token did not grant access"),
            @ApiResponse(responseCode = "424", description = "Database operation failure"),
    })
    @GetMapping(value = "activation")
    public ResponseEntity<ActivateUserResponseDto> activate(HttpServletRequest request) throws TCEntityNotFoundException, TCUpdateFailedException, TCInvalidRequestDataException, TCTokenMalformedException, TCTokenNotProvidedException, TCTokenExpiredException {
        checkIfCorrectTokenProvided(request);
        return new ResponseEntity<>(activateUserByRequestPort.activateUser(request), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenValidationResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Blacklisted token provided"),
            @ApiResponse(responseCode = "403", description = "Token has not been provided or provided token did not grant access")
    })
    @GetMapping(value = "validation")
    public ResponseEntity<TokenValidationResponseDto> validate(HttpServletRequest request) throws TCInvalidRequestDataException, TCTokenNotProvidedException, TCTokenMalformedException, TCTokenExpiredException {
        checkIfCorrectTokenProvided(request);
        return new ResponseEntity<>(validateProvidedTokenPort.validateProvidedToken(request), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LogoutResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "Token has not been provided or provided token did not grant access"),
            @ApiResponse(responseCode = "424", description = "Database operation failure"),
    })
    @GetMapping(value = "logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpServletRequest request) throws TCTokenNotProvidedException, TCTokenExpiredException, TCInsertFailedException {
        if (Objects.isNull(request.getHeader(ConstValues.AUTHORIZATION_HEADER))) {
            throw new TCTokenNotProvidedException("Token has not been provided");
        }
        return new ResponseEntity<>(logoutPort.logOut(request), HttpStatus.OK);
    }

    private static void checkIfNoTokenProvided(HttpServletRequest request) throws TCTokenProvidedException {
        if (Objects.nonNull(request.getHeader(ConstValues.AUTHORIZATION_HEADER))) {
            throw new TCTokenProvidedException("Token has been provided");
        }
    }

    private static void checkIfCorrectTokenProvided(HttpServletRequest request) throws TCTokenNotProvidedException, TCTokenMalformedException {
        if (Objects.isNull(request.getHeader(ConstValues.AUTHORIZATION_HEADER))) {
            throw new TCTokenNotProvidedException("Token has not been provided");
        }
        String token = request.getHeader(ConstValues.AUTHORIZATION_HEADER);
        if (!token.startsWith("Bearer ")) {
            throw new TCTokenMalformedException("Token is malformed");
        }
    }
}
