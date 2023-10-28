package com.example.team258.common.service;


import com.example.team258.domain.member.dto.UserSignupRequestDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.common.exception.DuplicateUsernameException;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.common.service.UserService;
import com.example.team258.domain.member.dto.UserUpdateRequestDto;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

/**
 * 사용자 서비스 관련 단위 테스트 클래스로 변환
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS) 어노테이션을 통해
 * 테스트 클래스의 인스턴스 수명 주기를 설정하는 데 사용
 * TestInstance.Lifecycle.PER_CLASS 설정을 사용하면 테스트 클래스의 단일 인스턴스만 생성
 * 이 인스턴스는 모든 테스트 메서드에 대해 재사용된다.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    /**
     * UserRepository에 대한 모의 객체 생성
     */
    @Mock
    private UserRepository userRepository;

    /**
     * UserService에 대한 모의 객체 생성
     */
    private UserService userService;

    /**
     * SecurityUtil의 static 메서드를 mock하기 위한 객체
     */
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;

    /**
     * PasswordEncoder 모의 객체 생성
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * SecurityUtil 모의 객체 생성
     */
    @Mock
    private SecurityUtil securityUtil;

    /**
     * 모든 테스트 시작하기 전에 실행하는 설정 메서드 이다.
     */
    @BeforeAll
    void setUp() {
        /**
         * Mockito 어노테이션을 활성화하여 Mock 객체 초기화
         */
        MockitoAnnotations.openMocks(this);

        /**
         * UserService 객체 생성
         */
        userService = new UserService(userRepository, passwordEncoder);

        /**
         * SecurityUtil의 static 메서드를 mock하기 위한 객체 생성
         */
        mockedSecurityUtil = mockStatic(SecurityUtil.class);

        //when
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
    }

    /**
     * 모든 테스트가 끝난 후에 실행하는 메서드
     */
    @AfterAll
    void tearDown() {
        /**
         * SecurityUtil의 static 메서드를 mock하기 위한 객체 닫기
         */
        mockedSecurityUtil.close();
    }

    @Test
    @DisplayName("비밀번호 일치 테스트")
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
    @DisplayName("비밀번호 불일치 테스트")
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

//    @Test
//    void userNameCheck_중복_아닐경우() {
//        // given
//        String username = "bin7780";
//        String username2 = "bin7788";
//        User user = User.builder()
//                .username(username)
//                .build();
//
//        //then
//        assertDoesNotThrow(() -> userService.userNameCheck(username2));
//    }
    @Test
    @DisplayName("유저 이름 중복 테스트")
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
    @DisplayName("유저 권한 테스트")
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
    @DisplayName("관리자 토큰 일치 테스트")
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
    @DisplayName("관리자 토큰 불일치 테스트")
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
    @DisplayName("CREATE 회원가입 테스트")
    void signup() {
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin022552")
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();
        User user = User.builder()
                .build();

        // when
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        MessageDto msg = userService.signup(requestDto).getBody();
        // then
        assertThat(msg.getMsg()).isEqualTo("회원가입이 완료되었습니다");

    }

    @Test
    @DisplayName("DELETE 회원탈퇴 테스트")
    void escape() {
        // given
        /**
         * HttpServletRequest, HttpServletResponse 모의 객체 생성
         */
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        /**
         * 현재 쓰레드 요청 속성에 모의 요청 및 응답 설정
         */
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));

        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .username("bin0222")
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();
        User user = User.builder()
                .username("bin0222")
                .build();

        // when
        /**
         * 모의 객체 동작 설정 : SecurityUtil.getPrincipal() 메서드가 Optional.ofNullable(user)를 반환하도록 설정
         */
        when(securityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        /**
         * 모의 객체 동작 설정 : userRepository.delete(user) 메서드가 아무것도 반환하지 않도록 설정
         */
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(user));


        doNothing().when(userRepository).delete(user);

        MessageDto msg = userService.escape().getBody();
        // then
        assertThat(msg.getMsg()).isEqualTo("회원탈퇴가 완료되었습니다");
    }

    @Test
    @DisplayName("READ 유저 이름과 권한으로 FIND 테스트")
    void findUsersByUsernameAndRoleV1(){
        when(userRepository.findAll(any(BooleanBuilder.class),any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(
                        User.builder()
                                .username("bin0222")
                                .userId(1L)
                                .role(UserRoleEnum.USER)
                                .build()
                )));

        //when
        Page<User> results = userService.findUsersByUsernameAndRoleV1("bin0222","USER",PageRequest.of(0,10));

        //then
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent().get(0).getUsername()).isEqualTo("bin0222");
        assertThat(results.getContent().get(0).getUserId()).isEqualTo(1L);
        assertThat(results.getContent().get(0).getRole()).isEqualTo(UserRoleEnum.USER);
    }

    @Test
    @DisplayName("UPDATE 비밀번호 수정 테스트")
    void update(){
//given
        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();

        User user = User.builder()
                .username("bin0222")
                .build();

        when(securityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.encode(any(String.class))).thenReturn("Bin@12345");

        //when
        MessageDto result = userService.update(requestDto);
        // then
        assertThat(result.getMsg()).isEqualTo("회원 정보 수정이 완료되었습니다");
    }

    @Test
    @DisplayName("UPDATE 비밀번호 수정 블일치 테스트")
    void update_비밀번호_불일치(){
        //given
        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                .password1("Bin@12345")
                .password2("Bin@1235")
                .build();

        User user = User.builder()
                .username("bin0222")
                .build();

        when(securityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.encode(any(String.class))).thenReturn("Bin@12345");

        //when
        // then
        assertThrows(IllegalArgumentException.class,()->userService.update(requestDto));
    }

    @Test
    @DisplayName("UPDATE 사용자 없을때 테스트")
    void update_사용자가_없을때(){
        //given
        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                .password1("Bin@12345")
                .password2("Bin@12345")
                .build();

        User user = User.builder()
                .username("bin0222")
                .build();

        when(securityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("Bin@12345");

        //when
        // then
        assertThrows(IllegalArgumentException.class,()->userService.update(requestDto));
    }

}
