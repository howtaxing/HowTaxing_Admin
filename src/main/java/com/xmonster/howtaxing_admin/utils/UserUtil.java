package com.xmonster.howtaxing_admin.utils;

import com.xmonster.howtaxing_admin.CustomException;
import com.xmonster.howtaxing_admin.model.User;
import com.xmonster.howtaxing_admin.repository.user.UserRepository;
import com.xmonster.howtaxing_admin.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    public User findUserBySocialId(String socialId) {
        return userRepository.findBySocialId(socialId).orElse(null);
    }

    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public String findUserSocialIdByPhoneNumber(String phoneNumber) {
        User findUser = findUserByPhoneNumber(phoneNumber);

        if(findUser == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "가입된 회원의 휴대폰번호가 아닙니다.");
        }

        return findUser.getSocialId();
    }
}
