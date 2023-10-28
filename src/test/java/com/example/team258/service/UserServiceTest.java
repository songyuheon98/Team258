package com.example.team258.service;

import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.common.exception.DuplicateUsernameException;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.common.service.UserService;

import com.example.team258.domain.member.dto.UserSignupRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
class UserServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private SecurityUtil securityUtil;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;
    @BeforeAll
    static void setUp() {
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }
    @AfterAll
    static void tearDown() {
        mockedSecurityUtil.close();
    }

    @Test
    void passwordCheck_비밀번호_일치() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();

        // when
        // then
        assertDoesNotThrow(() -> userService.passwordCheck(requestDto));
    }

    @Test
    void passwordCheck_비밀번호_불일치() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@1234")
                .build();

        // when
        // then
        assertThrows(IllegalArgumentException.class,() -> userService.passwordCheck(requestDto));
    }

    @Test
    void userNameCheck_중복_아닐경우() {
        // given
        String username = "bin7777";
        String username2 = "bin7778";
        User user = User.builder()
                .username(username)
                .build();

        //when
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        //then
        assertDoesNotThrow(() -> userService.userNameCheck(username2));
    }
    @Test
    void userNameCheck_중복_일_경우() {
        // given
        String username = "bin7777";
        String username2 = "bin7777";
        User user = User.builder()
                .username(username)
                .build();

        //when
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        //then
        assertThrows(DuplicateUsernameException.class,() -> userService.userNameCheck(username2));

    }

    @Test
    void getUserRoleEnum_사용자() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@1234")
                .build();

        assertThat(userService.getUserRoleEnum(requestDto)).isEqualTo(UserRoleEnum.USER);

    }
    @Test
    void getUserRoleEnum_관리자_토큰_일치() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@1234")
                .admin(true)
                .adminToken("adminadminadminadminadmin")
                .build();

        assertThat(userService.getUserRoleEnum(requestDto)).isEqualTo(UserRoleEnum.ADMIN);

    }

    @Test
    void getUserRoleEnum_관리자_토큰_불일치() {
        //given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@1234")
                .admin(true)
                .adminToken("adminadminadminadminadmn")
                .build();

        assertThrows(IllegalArgumentException.class,()->userService.getUserRoleEnum(requestDto));

    }
    @Test
    void signup() {
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();
        User user = User.builder()
                .build();

        // when
        when(userRepository.save(user)).thenReturn(user);
        MessageDto msg = userService.signup(requestDto).getBody();
        // then
        assertThat(msg.getMsg()).isEqualTo("회원가입이 완료되었습니다");

    }

    @Test
    void escape() {
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();
        User user = User.builder()
                .username("bin0222")
                .build();

        // when
        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));

        doNothing().when(userRepository).delete(user);

        MessageDto msg = userService.escape().getBody();
        // then
        assertThat(msg.getMsg()).isEqualTo("회원탈퇴가 완료되었습니다");
    }
}