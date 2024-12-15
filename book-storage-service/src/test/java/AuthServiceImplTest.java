import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.service.UserService;
import com.modsen.bookstorageservice.service.impl.AuthServiceImpl;
import com.modsen.bookstorageservice.web.dto.UserDto;
import com.modsen.bookstorageservice.web.dto.auth.JwtRequest;
import com.modsen.bookstorageservice.web.dto.auth.JwtResponse;
import com.modsen.bookstorageservice.web.mapper.UserMapper;
import com.modsen.bookstorageservice.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private AuthServiceImpl authService;

    private JwtRequest loginRequest;
    private UserDto userDto;
    //private JwtResponse jwtResponse;

    @Test
    void testLogin_Success() {
        loginRequest = new JwtRequest("testUsername", "testPassword");
        userDto = new UserDto();
        userDto.setUsername("testUsername");
        userDto.setPassword("testPassword");
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        JwtResponse jwtResponse = new JwtResponse();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userService.getUserByUsername("testUsername")).thenReturn(userDto);
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()))
                .thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()))
                .thenReturn("refreshToken");

        JwtResponse result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("testUsername", result.getUsername());
        assertEquals(1L, result.getId());
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1)).getUserByUsername("testUsername");
        verify(jwtTokenProvider, times(1)).createAccessToken(user.getId(), user.getUsername(), user.getRoles());
        verify(jwtTokenProvider, times(1)).createRefreshToken(user.getId(), user.getUsername());
    }

    @Test
    void testLogin_AuthenticationFailed() {
        loginRequest = new JwtRequest("testUsername", "testPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRefresh_Success() {
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setRefreshToken("refreshToken");
        String refreshToken = "validRefreshToken";
        when(jwtTokenProvider.refreshUserTokens(refreshToken)).thenReturn(jwtResponse);

        JwtResponse result = authService.refresh(refreshToken);

        assertNotNull(result);
        verify(jwtTokenProvider, times(1)).refreshUserTokens(refreshToken);
    }

    @Test
    void testRegister_Success() {
        userDto = new UserDto();
        userDto.setUsername("testUsername");
        when(userService.createUser(userDto)).thenReturn(userDto);

        UserDto result = authService.register(userDto);


        assertNotNull(result);
        assertEquals("testUsername", result.getUsername());
        verify(userService, times(1)).createUser(userDto);
    }

    @Test
    void testRegister_UserAlreadyExists() {
        userDto = new UserDto();
        userDto.setUsername("testUsername");
        when(userService.createUser(userDto)).thenThrow(new RuntimeException("User already exists"));

        assertThrows(RuntimeException.class, () -> authService.register(userDto));

        verify(userService, times(1)).createUser(userDto);
    }
}
