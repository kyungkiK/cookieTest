package com.example.cookietest.service;

import com.example.cookietest.auth.config.SecurityUtil;
import com.example.cookietest.auth.jwt.TokenDto;
import com.example.cookietest.auth.jwt.TokenProvider;
import com.example.cookietest.dto.Authority;
import com.example.cookietest.dto.LoginDto;
import com.example.cookietest.dto.SignInDto;
import com.example.cookietest.dto.UserDto;
import com.example.cookietest.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder BCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;


    public String getLoginId() {
        return SecurityUtil.getCurrentMemberId();
    }

    /**
     * 회원가입
     * @param signInDto
     */
    @Transactional
    public void save(SignInDto signInDto) {
        UserDto userDto = UserDto.builder()
                .userId(signInDto.getUserId())
                .password(BCryptPasswordEncoder.encode(signInDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();

        userRepository.save(userDto);
    }

    /**
     * 로그인
     * @param loginDto
     * @return
     */
    public TokenDto login(LoginDto loginDto){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authenticate);

        return tokenDto;
    }

    public TokenDto refresh(String refreshToken) {
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        return tokenDto;
    }
}