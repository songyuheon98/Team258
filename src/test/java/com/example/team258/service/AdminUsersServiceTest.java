//package com.example.team258.service;
//
//import com.example.team258.dto.AdminResponseDto;
//import com.example.team258.dto.MessageDto;
//import com.example.team258.entity.User;
//import com.example.team258.entity.UserRoleEnum;
//import com.example.team258.repository.UserRepository;
//import com.example.team258.security.UserDetailsImpl;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class AdminServiceTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private AdminService adminService;
//
//    @Nested
//    @DisplayName("AdminService - READ - 단순 로직 작동 여부 체크")
//    class BasicReadTest {
//        @Test
//        @DisplayName("회원 전체 조회 성공")
//        void Admin_getAllUsersSuccess() {
//            // given
//            User user1 = new User();
//            User user2 = new User();
//            User user3 = new User();
//
//            List<User> userList = Arrays.asList(user1, user2, user3);
//
//            when(userRepository.findAll()).thenReturn(userList);
//
//            // when
//            List<AdminResponseDto> result = adminService.getAllUsers();
//
//            // then
//            assertThat(result).hasSize(3);
//        }
//
//        @Test
//        @DisplayName("회원 전체 조회 실패 - 등록된 회원이 없는 경우")
//        void Admin_getAllUsersFail() {
//            // given
//            List<User> emptyUserList = Collections.emptyList();
//
//            when(userRepository.findAll()).thenReturn(emptyUserList);
//
//            // when
//            List<AdminResponseDto> result = adminService.getAllUsers();
//
//            // then
//            assertThat(result).isEmpty();
//        }
//    }
//
//    @Nested
//    @DisplayName("AdminService - DELETE - 단순 로직 작동 여부 체크")
//    class BasicDeleteTest {
//
//        @Test
//        @DisplayName("유저 삭제 성공")
//        void deleteUserSuccess() {
//            // given
//            User user = User.builder()
//                    .userId(1L)
//                    .username("user1")
//                    .password("pass1")
//                    .role(UserRoleEnum.USER)
//                    .build();
//
//            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//            doNothing().when(userRepository).delete(user);
//
//            // when
//            MessageDto result = adminService.deleteUser(1L, user);
//
//            // then
//            assertThat(result.getMsg()).isEqualTo("삭제가 완료되었습니다");
//            verify(userRepository, times(1)).delete(user);
//        }
//
//        @Test
//        @DisplayName("유저 삭제 실패 - 존재하지 않는 유저 삭제 시도")
//        void deleteNonExistingUserFail() {
//            // given
//            when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//            // when & then
//            assertThrows(IllegalArgumentException.class,
//                    () -> adminService.deleteUser(1L, any(User.class)),
//                    "회원을 찾을 수 없습니다.");
//        }
//    }
//
//    @Nested
//    @DisplayName("AdminService - READ - 로그인된 회원의 ROLE에 대한 테스트")
//    class LoginnedTest {
//        @Test
//        @DisplayName("회원 전체 조회 성공 - ADMIN으로 로그인한 경우에 성공")
//        void Admin_getAllUsersSuccessWithAdminRoleSuccess() {
//            // given
//            User adminUser = User.builder()
//                    .userId(1L)
//                    .username("admin")
//                    .password("adminPass")
//                    .role(UserRoleEnum.valueOf("ADMIN"))
//                    .build();
//
//            List<User> userList = Arrays.asList(new User(), new User(), new User());
//
//            when(userRepository.findAll()).thenReturn(userList);
//
//            // 가상의 관리자로 로그인 상태를 설정
//            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//            Authentication authentication = Mockito.mock(Authentication.class);
//
//            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
//
//            SecurityContextHolder.setContext(securityContext);
//
//            when(securityContext.getAuthentication()).thenReturn(authentication);
//            when(authentication.getPrincipal()).thenReturn(adminUserDetails);
//
//            // when
//            List<AdminResponseDto> result = adminService.getAllUsers();
//
//            // then
//            assertThat(result).hasSize(3);
//        }
//
//        @Test
//        @DisplayName("회원 전체 조회 실패 - USER로 로그인한 경우에 조회 실패")
//        void Admin_getAllUsersFailWithUserRole() {
//            // given
//            User user = User.builder()
//                    .userId(2L)
//                    .username("user")
//                    .password("userPass")
//                    .role(UserRoleEnum.valueOf("USER"))
//                    .build();
//
//            List<User> emptyUserList = Collections.emptyList();
//
//            when(userRepository.findAll()).thenReturn(emptyUserList);
//
//            // 가상의 USER로 로그인 상태를 설정
//            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//            Authentication authentication = Mockito.mock(Authentication.class);
//
//            UserDetailsImpl userDetails = new UserDetailsImpl(user);
//
//            SecurityContextHolder.setContext(securityContext);
//
//            when(securityContext.getAuthentication()).thenReturn(authentication);
//            when(authentication.getPrincipal()).thenReturn(userDetails);
//
//            // when
//            List<AdminResponseDto> result = adminService.getAllUsers();
//
//            // then
//            assertThat(result).isEmpty();
//        }
//    }
//
//    @Nested
//    @DisplayName("AdminService - DELETE - 로그인된 회원의 ROLE에 대한 테스트")
//    class LoginnedDeleteTest {
//
//        @Test
//        @DisplayName("유저 삭제 성공 - 관리자로 로그인한 경우")
//        void deleteUserSuccessWithAdminRole() {
//            // given
//            User adminUser = User.builder()
//                    .userId(1L)
//                    .username("admin")
//                    .password("adminPass")
//                    .role(UserRoleEnum.ADMIN)
//                    .build();
//
//            User userToDelete = User.builder()
//                    .userId(3L)
//                    .username("userToDelete")
//                    .password("password")
//                    .role(UserRoleEnum.USER)
//                    .build();
//
//            when(userRepository.findById(3L)).thenReturn(Optional.of(userToDelete));
//
//            // 가상의 ADMIN으로 로그인 상태를 설정
//            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//            Authentication authentication = Mockito.mock(Authentication.class);
//
//            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
//
//            SecurityContextHolder.setContext(securityContext);
//
//            when(securityContext.getAuthentication()).thenReturn(authentication);
//            when(authentication.getPrincipal()).thenReturn(adminUserDetails);
//
//            // when
//            MessageDto msg = adminService.deleteUser(3L, userToDelete);
//
//            // then
//            assertThat(msg.getMsg()).isEqualTo("삭제가 완료되었습니다");
//        }
//
//
//        @Test
//        @DisplayName("유저 삭제 실패 - 존재하지 않는 유저 삭제 시도")
//        void deleteNonExistingUserFail() {
//            // given
//            User adminUser = User.builder()
//                    .userId(1L)
//                    .username("admin")
//                    .password("adminPass")
//                    .role(UserRoleEnum.ADMIN)
//                    .build();
//
//            when(userRepository.findById(3L)).thenReturn(Optional.empty());
//
//            // 가상의 관리자로 로그인 상태를 설정
//            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//            Authentication authentication = Mockito.mock(Authentication.class);
//
//            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
//
//            SecurityContextHolder.setContext(securityContext);
//
//            when(securityContext.getAuthentication()).thenReturn(authentication);
//            when(authentication.getPrincipal()).thenReturn(adminUserDetails);
//
//            // when & then
//            assertThrows(IllegalArgumentException.class,
//                    () -> adminService.deleteUser(3L, any(User.class)),
//                    "회원을 찾을 수 없습니다.");
//        }
//    }
//}
//
