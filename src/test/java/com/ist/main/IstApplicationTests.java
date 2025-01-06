package com.ist.main;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ist.main.dto.AccountResponseDto;
import com.ist.main.dto.TransactionResponseDto;
import com.ist.main.enums.TransactionType;
import com.ist.main.repository.IAccountRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class IstApplicationTests {

    Logger logger = LoggerFactory.getLogger(IstApplicationTests.class);
    private final PasswordEncoder passwordEncoder;

    IstApplicationTests(@Autowired PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    @Order(0)
    void contextLoads() {
        logger.info(() -> "=======================CONTEXT LOADED=============================");
    }

    @Test
    @Order(0)
    void passwordEncoderTest() {
        logger.info(() -> "password: " + passwordEncoder.encode("password"));
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UserAPITest {
        private final MockMvc mockMvc;
        private final ObjectMapper objectMapper;
        private static String userId = "";

        public UserAPITest(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) {
            this.mockMvc = mockMvc;
            this.objectMapper = objectMapper;
        }

        @Test
        @Order(1)
        void findAllEmpty() throws Exception {
            this.mockMvc
                    .perform(get("/api/users"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(equalTo("[]")));
        }

        @Test
        @Order(1)
        void findByIdNotFound() throws Exception {
            this.mockMvc
                    .perform(get("/api/users/UUID-sample"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse =
                                objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertEquals(1, errorResponse.getErrors().size());
                        assertEquals(400, errorResponse.getStatus());
                        assertEquals("BusinessException", errorResponse.getMessage());
                        assertEquals(
                                "'UUID-sample' id not found",
                                errorResponse.getErrors().get(0));
                    });
        }

        @Test
        @Order(3)
        void addSucceed() throws Exception {
            this.mockMvc
                    .perform(
                            post("/api/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                                    {
                                    "email": "xx@gmail.com",
                                    "name": "John Doe"
                                    }"""))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(result -> {
                        Map<String, String> map = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertTrue(map.containsKey("id"));
                        assertEquals("xx@gmail.com", map.get("email"));
                        assertEquals("John Doe", map.get("name"));
                        userId = map.get("id");
                    });
        }

        @Test
        @Order(4)
        void findAll() throws Exception {
            this.mockMvc
                    .perform(get("/api/users"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        List<Object> list = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertEquals(1, list.size());
                        Map<String, String> map = objectMapper.convertValue(list.get(0), new TypeReference<>() {});
                        assertEquals("xx@gmail.com", map.get("email"));
                        assertEquals("John Doe", map.get("name"));
                    });
        }

        @Test
        @Order(5)
        void findById() throws Exception {
            this.mockMvc
                    .perform(get(String.format("/api/users/%s", userId)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Map<String, Object> map = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertEquals(userId, map.get("id"));
                        assertEquals("xx@gmail.com", map.get("email"));
                        assertEquals("John Doe", map.get("name"));
                    });
        }

        @Test
        @Order(6)
        void update() throws Exception {
            this.mockMvc
                    .perform(
                            put(String.format("/api/users/%s", userId))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                                    {
                                    "email": "johnwick@gmail.com",
                                    "name": "John Wick"
                                    }"""))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(result -> {
                        Map<String, String> map = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertTrue(map.containsKey("id"));
                        assertEquals("johnwick@gmail.com", map.get("email"));
                        assertEquals("John Wick", map.get("name"));
                    });
        }

        @Test
        @Order(7)
        void deleteSucceed() throws Exception {
            this.mockMvc
                    .perform(delete(String.format("/api/users/%s", userId)))
                    .andDo(print())
                    .andExpect(status().isOk());
            this.mockMvc
                    .perform(get(String.format("/api/users/%s", userId)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse =
                                objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertEquals(1, errorResponse.getErrors().size());
                        assertEquals(
                                String.format("'%s' id not found", userId),
                                errorResponse.getErrors().get(0));
                    });
        }

        @Test
        void addEmailFailed() throws Exception {
            this.mockMvc
                    .perform(
                            post("/api/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                                    {
                                    "email": "gmail.com",
                                    "name": "John Doe"
                                    }"""))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ErrorResponse errorResponse =
                                objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
                        assertEquals(1, errorResponse.getErrors().size());
                        assertEquals("Invalid email", errorResponse.getErrors().get(0));
                    });
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class AccountAPITest {
        private final MockMvc mockMvc;
        private final ObjectMapper objectMapper;

        public AccountAPITest(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) {
            this.mockMvc = mockMvc;
            this.objectMapper = objectMapper;
        }

        private static String userId = "";

        @Test
        @Order(1)
        void addAccountSucceed() throws Exception {
            this.mockMvc
                    .perform(
                            post("/api/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                                    {
                                    "email": "mailforAccount@gmail.com",
                                    "name": "User For Account test"
                                    }"""))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(result -> {
                        Map<String, String> map = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertTrue(map.containsKey("id"));
                        assertEquals("mailforAccount@gmail.com", map.get("email"));
                        assertEquals("User For Account test", map.get("name"));
                        userId = map.get("id");
                    });
            this.mockMvc
                    .perform(post("/api/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format(
                                    """
                                        {
                                          "user_id": "%s",
                                          "initial_balance": 1000.00
                                        }
                                    """,
                                    userId)))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(result -> {
                        AccountResponseDto accountResponseDto = objectMapper.readValue(
                                result.getResponse().getContentAsString(), AccountResponseDto.class);
                        assertNotNull(accountResponseDto.getId());
                        assertEquals(
                                BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_DOWN),
                                accountResponseDto.getBalance());
                        assertNotNull(accountResponseDto.getCreatedAt());
                    });
        }

        @Test
        @Order(2)
        void getAccountListByUserIdSucceeded() throws Exception {
            this.mockMvc
                    .perform(get(String.format("/api/account?user_id=%s", userId)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        List<AccountResponseDto> list = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertEquals(1, list.size());
                        AccountResponseDto accountResponseDto =
                                objectMapper.convertValue(list.get(0), AccountResponseDto.class);
                        assertEquals(
                                BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_DOWN),
                                accountResponseDto.getBalance());
                        assertNotNull(accountResponseDto.getCreatedAt());
                    });
        }

        @Test
        @Order(3)
        void findAccountByBalanceGreaterThanSucceeded() throws Exception {
            this.mockMvc
                    .perform(get(String.format("/api/account/balance/gt?balance=%s", BigDecimal.TEN)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        List<AccountResponseDto> list = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertEquals(1, list.size());
                        AccountResponseDto accountResponseDto =
                                objectMapper.convertValue(list.get(0), AccountResponseDto.class);
                        assertEquals(
                                BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_DOWN),
                                accountResponseDto.getBalance());
                        assertNotNull(accountResponseDto.getCreatedAt());
                    });
        }
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class TransactionAPITest {
        private final MockMvc mockMvc;
        private final ObjectMapper objectMapper;
        private final IAccountRepository accountRepository;

        public TransactionAPITest(
                @Autowired MockMvc mockMvc,
                @Autowired ObjectMapper objectMapper,
                @Autowired IAccountRepository accountRepository) {
            this.mockMvc = mockMvc;
            this.objectMapper = objectMapper;
            this.accountRepository = accountRepository;
        }

        private static String userId = "";
        private static AccountResponseDto account = null;

        @Test
        @Order(1)
        void add5Debit5CreditTransactionSucceed() throws Exception {
            this.mockMvc
                    .perform(
                            post("/api/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                                    {
                                    "email": "mailforaccounttransaction@gmail.com",
                                    "name": "User For Account Transaction test"
                                    }"""))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(result -> {
                        Map<String, String> map = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertTrue(map.containsKey("id"));
                        assertEquals("mailforaccounttransaction@gmail.com", map.get("email"));
                        assertEquals("User For Account Transaction test", map.get("name"));
                        userId = map.get("id");
                    });
            this.mockMvc
                    .perform(post("/api/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format(
                                    """
                                        {
                                          "user_id": "%s",
                                          "initial_balance": 2000.00
                                        }
                                    """,
                                    userId)))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(result -> {
                        AccountResponseDto accountResponseDto = objectMapper.readValue(
                                result.getResponse().getContentAsString(), AccountResponseDto.class);
                        assertNotNull(accountResponseDto.getId());
                        assertEquals(
                                BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_DOWN),
                                accountResponseDto.getBalance());
                        assertNotNull(accountResponseDto.getCreatedAt());
                        account = accountResponseDto;
                    });

            for (int i = 0; i < 5; i++) {
                this.mockMvc
                        .perform(post("/api/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format(
                                        "      	{\"account_id\": \"%s\",\"amount\": 2000.00, \"type\": \"CREDIT\"}",
                                        account.getId())))
                        .andDo(print())
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(result -> {
                            TransactionResponseDto transactionResponseDto = objectMapper.readValue(
                                    result.getResponse().getContentAsString(), TransactionResponseDto.class);
                            assertNotNull(transactionResponseDto.getId());
                            assertEquals(
                                    BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_DOWN),
                                    transactionResponseDto.getAmount());
                            assertEquals(TransactionType.CREDIT, transactionResponseDto.getType());
                            assertNotNull(transactionResponseDto.getTimestamp());
                        });
            }

            for (int i = 0; i < 5; i++) {
                this.mockMvc
                        .perform(post("/api/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format(
                                        "      	{\"account_id\": \"%s\",\"amount\": 2000.00, \"type\": \"DEBIT\"}",
                                        account.getId())))
                        .andDo(print())
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(result -> {
                            TransactionResponseDto transactionResponseDto = objectMapper.readValue(
                                    result.getResponse().getContentAsString(), TransactionResponseDto.class);
                            assertNotNull(transactionResponseDto.getId());
                            assertEquals(
                                    BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_DOWN),
                                    transactionResponseDto.getAmount());
                            assertEquals(TransactionType.DEBIT, transactionResponseDto.getType());
                            assertNotNull(transactionResponseDto.getTimestamp());
                        });
            }
            accountRepository
                    .findById(account.getId())
                    .ifPresent(account -> assertEquals(
                            BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_DOWN), account.getBalance()));
        }

        @Test
        @Order(2)
        void getHistoryTransactionSucceeded() throws Exception {
            this.mockMvc
                    .perform(get(String.format("/api/transaction?account_id=%s", account.getId())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Map<String, Object> responseMap = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertEquals(10, responseMap.get("size"));
                        assertEquals(1, responseMap.get("total_pages"));
                        List<TransactionResponseDto> list = objectMapper.readValue(
                                objectMapper.writeValueAsString(responseMap.get("content")), new TypeReference<>() {});
                        assertEquals(10, list.size());
                    });
        }

        @Test
        @Order(3)
        void getHistoryTransactionReportSucceeded() throws Exception {
            this.mockMvc
                    .perform(get(String.format("/api/transaction/report?type=%s", TransactionType.DEBIT.name())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Map<String, String> responseMap = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertEquals(TransactionType.DEBIT.name(), responseMap.get("type"));
                        assertEquals(5, Integer.parseInt(responseMap.get("total_transaction")));
                        assertEquals(
                                BigDecimal.valueOf(10000).setScale(2, RoundingMode.HALF_DOWN),
                                new BigDecimal(responseMap.get("total_amount")).setScale(2, RoundingMode.HALF_DOWN));
                    });
            this.mockMvc
                    .perform(get(String.format("/api/transaction/report?type=%s", TransactionType.CREDIT.name())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        Map<String, String> responseMap = objectMapper.readValue(
                                result.getResponse().getContentAsString(), new TypeReference<>() {});
                        assertEquals(TransactionType.CREDIT.name(), responseMap.get("type"));
                        assertEquals(5, Integer.parseInt(responseMap.get("total_transaction")));
                        assertEquals(
                                BigDecimal.valueOf(10000).setScale(2, RoundingMode.HALF_DOWN),
                                new BigDecimal(responseMap.get("total_amount")).setScale(2, RoundingMode.HALF_DOWN));
                    });
        }
    }

    @Test
    void ErrorResponseTest() {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorResponse errorResponse2 = new ErrorResponse("", 200, List.of());
        assertNotNull(errorResponse);
        assertNotNull(errorResponse2);
    }

    private static class ErrorResponse {
        public ErrorResponse() {}

        public ErrorResponse(String message, int status, List<String> errors) {
            this.setMessage(message);
            this.setStatus(status);
            this.setErrors(errors);
        }

        private String message;
        private int status;
        private List<String> errors;

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
