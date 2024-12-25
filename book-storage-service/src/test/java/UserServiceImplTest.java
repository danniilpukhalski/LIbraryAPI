import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.domain.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.domain.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.repository.UserRepository;
import com.modsen.bookstorageservice.service.impl.UserServiceImpl;
import com.modsen.bookstorageservice.web.dto.UserDto;
import com.modsen.bookstorageservice.web.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private static User user;
    private static UserDto userDto;

    public static void setUpBeforeAll() {
        // Это выполняется один раз для всего класса
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testUser");
        userDto.setPassword("password");
    }

    @Test
    void testGetUserById_Success() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(userDto.getId());

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userDto.getId()));
        verify(userRepository, times(1)).findById(user.getId());
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    void testGetUserByUsername_Success() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserByUsername(userDto.getUsername());

        assertNotNull(result);
        assertEquals(userDto.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testGetUserByUsername_NotFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername(userDto.getUsername()));
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        UserDto result = userService.updateUser(userDto);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testUpdateUser_NotFound(){
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("existingUser");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userDto));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser_UsernameAlreadyExists() {
            // Arrange
            UserDto userDto = new UserDto();
            userDto.setId(1L);
            userDto.setUsername("existingUser");

            User existingUser = new User();
            existingUser.setId(2L);
            existingUser.setUsername("existingUser");

            when(userRepository.findById(1L)).thenReturn(Optional.of(new User())); // user is found
            when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

            // Act & Assert
            DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> userService.updateUser(userDto));
            assertEquals("Username already exists", exception.getMessage());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, times(2)).findByUsername("existingUser");
        }



    @Test
    void testCreateUser_Success() {
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testCreateUser_PasswordsDoNotMatch() {
        assertThrows(IllegalStateException.class, () -> userService.createUser(userDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> userService.createUser(userDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(user.getId()));

        verify(userRepository, never()).delete(any(User.class));
    }
    @Test
    void testGetAllUsers_WhenUsersExist() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(new UserDto());
        userDtos.add(new UserDto());

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(users)).thenReturn(userDtos);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(users);
    }
}

