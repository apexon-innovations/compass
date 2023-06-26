package com.apexon.compass.exception;

import com.apexon.compass.exception.custom.*;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ApiExceptionHandlerTest {

    private static MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RestExceptionThrowingController())
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
    }

    @Controller
    @RequestMapping("/tests")
    public static class RestExceptionThrowingController {

        @GetMapping(value = "/badRequestException")
        public @ResponseBody String badRequestException() {
            throw new BadRequestException("Bad Request");
        }

        @GetMapping(value = "/forbiddenException")
        public @ResponseBody String forbiddenException() {
            throw new ForbiddenException("FORBIDDEN");
        }

        @GetMapping(value = "/jwtTokenException")
        public @ResponseBody String jwtTokenException() {
            throw new JwtTokenException("INTERNAL_SERVER_ERROR");
        }

        @GetMapping(value = "/recordNotFoundException")
        public @ResponseBody String recordNotFound() {
            throw new RecordNotFoundException("NOT_FOUND");
        }

        @GetMapping(value = "/serviceException")
        public @ResponseBody String serviceException() {
            throw new ServiceException("INTERNAL_SERVER_ERROR");
        }

        @GetMapping(value = "/typeConversionException")
        public @ResponseBody String typeConversionException() {
            throw new ServiceException("INTERNAL_SERVER_ERROR");
        }

        @GetMapping(value = "/unauthorizedException")
        public @ResponseBody String unauthorizedException() {
            throw new UnauthorizedException("UNAUTHORIZED");
        }

    }

    @Test
    void Bad_Request_Exception() throws Exception {
        mockMvc.perform(get("/tests/badRequestException"))
            .andExpectAll(status().isBadRequest(), jsonPath("$.apiError.status").value("BAD_REQUEST"));
    }

    @Test
    void Forbidden_Exception() throws Exception {
        mockMvc.perform(get("/tests/forbiddenException"))
            .andExpectAll(status().isForbidden(), jsonPath("$.apiError.status").value("FORBIDDEN"));
    }

    @Test
    void Jwt_Token_Exception() throws Exception {
        mockMvc.perform(get("/tests/jwtTokenException"))
            .andExpectAll(status().isInternalServerError(),
                    jsonPath("$.apiError.status").value("INTERNAL_SERVER_ERROR"));
    }

    @Test
    void Record_Not_Found_Exception() throws Exception {
        mockMvc.perform(get("/tests/recordNotFoundException"))
            .andExpectAll(status().isNotFound(), jsonPath("$.apiError.status").value("NOT_FOUND"));
    }

    @Test
    void Service_Exception() throws Exception {
        mockMvc.perform(get("/tests/serviceException"))
            .andExpectAll(status().isInternalServerError(),
                    jsonPath("$.apiError.status").value("INTERNAL_SERVER_ERROR"));
    }

    @Test
    void Type_Conversion_Exception() throws Exception {
        mockMvc.perform(get("/tests/typeConversionException"))
            .andExpectAll(status().isInternalServerError(),
                    jsonPath("$.apiError.status").value("INTERNAL_SERVER_ERROR"));
    }

    @Test
    void Unauthorized_Exception() throws Exception {
        mockMvc.perform(get("/tests/unauthorizedException"))
            .andExpectAll(status().isUnauthorized(), jsonPath("$.apiError.status").value("UNAUTHORIZED"));
    }

}
