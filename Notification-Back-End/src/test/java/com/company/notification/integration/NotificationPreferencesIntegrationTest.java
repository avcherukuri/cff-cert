// package com.company.notification.integration;

// import java.util.List;
// import java.util.Map;

// import static org.hamcrest.Matchers.is;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.springframework.test.web.servlet.MockMvc;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;

// import com.company.notification.security.TestUserPrincipal;
// import com.fasterxml.jackson.databind.ObjectMapper;

// @Testcontainers
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// class NotificationPreferencesIntegrationTest {

//     private static final String BASE_PATH = "/api/v1/notification-preferences";
//     private static final long USER_A = 1L;
//     private static final long USER_B = 2L;

//     @Container
//     static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
//             .withDatabaseName("notification_test")
//             .withUsername("test")
//             .withPassword("test");

//     @DynamicPropertySource
//     static void datasourceProperties(DynamicPropertyRegistry registry) {
//         registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
//         registry.add("spring.datasource.username", POSTGRES::getUsername);
//         registry.add("spring.datasource.password", POSTGRES::getPassword);
//         // Test-only migration location adds a stub `users` table for the FK to reference,
//         // since the real users table is owned by another service and out of scope here.
//         registry.add("spring.flyway.locations", () -> "classpath:db/migration,classpath:db/testmigration");
//     }

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private JdbcTemplate jdbcTemplate;

//     private final ObjectMapper objectMapper = new ObjectMapper();

//     @BeforeEach
//     void seedUsers() {
//         jdbcTemplate.update("DELETE FROM user_notification_preferences");
//         jdbcTemplate.update("DELETE FROM users");
//         jdbcTemplate.update("INSERT INTO users (id, email) VALUES (?, ?)", USER_A, "user-a@example.com");
//         jdbcTemplate.update("INSERT INTO users (id, email) VALUES (?, ?)", USER_B, "user-b@example.com");
//     }

//     private Authentication authFor(long userId) {
//         return new UsernamePasswordAuthenticationToken(new TestUserPrincipal(userId), null, List.of());
//     }

// //     @Test
// //     void get_returnsDefaults_whenNoRowExistsYet() throws Exception {
// //         mockMvc.perform(get(BASE_PATH).with(authentication(authFor(USER_A))))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.emailEnabled", is(true)))
// //                 .andExpect(jsonPath("$.smsEnabled", is(false)))
// //                 .andExpect(jsonPath("$.pushEnabled", is(true)))
// //                 .andExpect(jsonPath("$.updatedAt", nullValue()));
// //     }

// //     @Test
// //     void put_thenGet_roundTripsThroughRealDatabase() throws Exception {
// //         Map<String, Boolean> body = Map.of("emailEnabled", false, "smsEnabled", true, "pushEnabled", false);

// //         mockMvc.perform(put(BASE_PATH)
// //                         .with(authentication(authFor(USER_A)))
// //                         .with(csrf())
// //                         .contentType(MediaType.APPLICATION_JSON)
// //                         .content(objectMapper.writeValueAsString(body)))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.emailEnabled", is(false)))
// //                 .andExpect(jsonPath("$.smsEnabled", is(true)))
// //                 .andExpect(jsonPath("$.pushEnabled", is(false)));

// //         mockMvc.perform(get(BASE_PATH).with(authentication(authFor(USER_A))))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.smsEnabled", is(true)));
// //     }

// //     @Test
// //     void patch_updatesOnlyTheProvidedField() throws Exception {
// //         mockMvc.perform(put(BASE_PATH)
// //                         .with(authentication(authFor(USER_A)))
// //                         .with(csrf())
// //                         .contentType(MediaType.APPLICATION_JSON)
// //                         .content(objectMapper.writeValueAsString(Map.of("emailEnabled", true, "smsEnabled", false, "pushEnabled", true))))
// //                 .andExpect(status().isOk());

// //         mockMvc.perform(patch(BASE_PATH)
// //                         .with(authentication(authFor(USER_A)))
// //                         .with(csrf())
// //                         .contentType(MediaType.APPLICATION_JSON)
// //                         .content(objectMapper.writeValueAsString(Map.of("smsEnabled", true))))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.emailEnabled", is(true)))
// //                 .andExpect(jsonPath("$.smsEnabled", is(true)))
// //                 .andExpect(jsonPath("$.pushEnabled", is(true)));
// //     }

// //     @Test
// //     void put_rejectsIncompleteBody() throws Exception {
// //         mockMvc.perform(put(BASE_PATH)
// //                         .with(authentication(authFor(USER_A)))
// //                         .with(csrf())
// //                         .contentType(MediaType.APPLICATION_JSON)
// //                         .content(objectMapper.writeValueAsString(Map.of("emailEnabled", true))))
// //                 .andExpect(status().isBadRequest())
// //                 .andExpect(jsonPath("$.error", is("VALIDATION_ERROR")));
// //     }

// //     @Test
// //     void patch_rejectsEmptyBody() throws Exception {
// //         mockMvc.perform(patch(BASE_PATH)
// //                         .with(authentication(authFor(USER_A)))
// //                         .with(csrf())
// //                         .contentType(MediaType.APPLICATION_JSON)
// //                         .content(objectMapper.writeValueAsString(Map.of())))
// //                 .andExpect(status().isBadRequest());
// //     }

// //     @Test
// //     void unauthenticatedRequest_returns401() throws Exception {
// //         mockMvc.perform(get(BASE_PATH))
// //                 .andExpect(status().isUnauthorized());
// //     }

// //     @Test
// //     void crossUserIsolation_userACannotSeeOrAffectUserBsPreferences() throws Exception {
// //         mockMvc.perform(put(BASE_PATH)
// //                         .with(authentication(authFor(USER_A)))
// //                         .with(csrf())
// //                         .contentType(MediaType.APPLICATION_JSON)
// //                         .content(objectMapper.writeValueAsString(Map.of("emailEnabled", false, "smsEnabled", true, "pushEnabled", false))))
// //                 .andExpect(status().isOk());

// //         // User B has never saved preferences - must still see column defaults, not user A's row.
// //         mockMvc.perform(get(BASE_PATH).with(authentication(authFor(USER_B))))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.emailEnabled", is(true)))
// //                 .andExpect(jsonPath("$.smsEnabled", is(false)))
// //                 .andExpect(jsonPath("$.pushEnabled", is(true)))
// //                 .andExpect(jsonPath("$.updatedAt", nullValue()));

// //         mockMvc.perform(put(BASE_PATH)
// //                         .with(authentication(authFor(USER_B)))
// //                         .with(csrf())
// //                         .contentType(MediaType.APPLICATION_JSON)
// //                         .content(objectMapper.writeValueAsString(Map.of("emailEnabled", true, "smsEnabled", true, "pushEnabled", true))))
// //                 .andExpect(status().isOk());

// //         // User A's row must be unaffected by user B's write.
// //         mockMvc.perform(get(BASE_PATH).with(authentication(authFor(USER_A))))
// //                 .andExpect(status().isOk())
// //                 .andExpect(jsonPath("$.emailEnabled", is(false)))
// //                 .andExpect(jsonPath("$.smsEnabled", is(true)))
// //                 .andExpect(jsonPath("$.pushEnabled", is(false)));

// //         long distinctRows = jdbcTemplate.queryForObject(
// //                 "SELECT COUNT(*) FROM user_notification_preferences", Long.class);
// //         org.junit.jupiter.api.Assertions.assertEquals(2, distinctRows);
// //     }
// }
