package com.example.team258.service;

import com.example.team258.dto.UserSignupRequestDto;
import com.example.team258.entity.MessageDto;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.exception.DuplicateUsernameException;
import com.example.team258.jwt.SecurityUtil;
import com.example.team258.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "adminadminadminadminadmin";

    public static void passwordCheck(UserSignupRequestDto requestDto) {
        if(!requestDto.getPassword1().equals(requestDto.getPassword2())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void userNameCheck(String username) {
        // username 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if(checkUsername.isPresent()){
            throw new DuplicateUsernameException("중복된 username 입니다.");
        }
    }

    public UserRoleEnum getUserRoleEnum(UserSignupRequestDto requestDto) {
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isAdmin()){
            if(!ADMIN_TOKEN.equals(requestDto.getAdminToken())){
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능 합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        return role;
    }

    public ResponseEntity<MessageDto> signup(UserSignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword1());

        passwordCheck(requestDto);

        userNameCheck(username);

        UserRoleEnum role = getUserRoleEnum(requestDto);

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);

        return new ResponseEntity<>(new MessageDto("회원가입이 완료되었습니다"), null, HttpStatus.OK);

    }

    public ResponseEntity<MessageDto> escape() {
        String username = SecurityUtil.getPrincipal().get().getUsername();
        userRepository.delete(userRepository.findByUsername(username).orElse(null));
        return new ResponseEntity<>(new MessageDto("회원탈퇴가 완료되었습니다"), null, HttpStatus.OK);
    }
}
