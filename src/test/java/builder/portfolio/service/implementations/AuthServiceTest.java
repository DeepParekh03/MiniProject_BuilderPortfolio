package builder.portfolio.service.implementations;

import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.AuthRepository;
import builder.portfolio.util.ValidatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private AuthRepository mockAuthRepository;

    @BeforeEach
    void setUp() {
        mockAuthRepository = mock(AuthRepository.class);
        authService = new AuthService();
        try {
            var field = AuthService.class.getDeclaredField("authRepository");
            field.setAccessible(true);
            field.set(authService, mockAuthRepository);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock repository into AuthService", e);
        }
    }

    @Test
    void testLogin_Successful() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        when(mockAuthRepository.login("test@example.com", "password123")).thenReturn(mockUser);

        User result = authService.login("test@example.com", "password123");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(mockAuthRepository, times(1)).login("test@example.com", "password123");
    }

    @Test
    void testLogin_InvalidEmail() {
        User result = authService.login("invalid-email", "password123");
        assertNull(result);
        verify(mockAuthRepository, never()).login(any(), any());
    }

    @Test
    void testLogin_InvalidPassword() {
        User result = authService.login("test@example.com", "123");
        assertNull(result);
        verify(mockAuthRepository, never()).login(any(), any());
    }

    @Test
    void testRegister_Successful() {
        User mockUser = new User();
        mockUser.setUserName("John");
        mockUser.setEmail("john@example.com");
        mockUser.setPassword("password123");
        mockUser.setRole(UserRole.CLIENT);

        when(mockAuthRepository.register(any(User.class))).thenReturn(mockUser);

        User result = authService.register("John", "john@example.com", "password123", UserRole.CLIENT);

        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        verify(mockAuthRepository, times(1)).register(any(User.class));
    }


    @Test
    void testRegister_InvalidEmail() {
        User result = authService.register("John", "invalid-email", "password123", UserRole.CLIENT);
        assertNull(result);
        verify(mockAuthRepository, never()).register(any());
    }

    @Test
    void testRegister_InvalidPassword() {
        User result = authService.register("John", "john@example.com", "123", UserRole.CLIENT);
        assertNull(result);
        verify(mockAuthRepository, never()).register(any());
    }

}
