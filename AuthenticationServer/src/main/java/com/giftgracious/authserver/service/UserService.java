package com.giftgracious.authserver.service;

import com.giftgracious.authserver.dto.SignInDTO;
import com.giftgracious.authserver.dto.SignUpDTO;
import com.giftgracious.authserver.model.User;
import com.giftgracious.authserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsManager {

    @Autowired
    UserRepository userRepository;


    @Override
    public void createUser(UserDetails user) {
        userRepository.save((User)user);
    }

    @Override
    public void updateUser(UserDetails user) {


    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public User registerUser (SignUpDTO dto){
        if (userExists(dto.getUserName())){
            throw new RuntimeException("USer Already Exists");
        }
        User user = User.builder().username(dto.getUserName())
                .password(encodePasswordEncoder(dto.getPassword()))
                .email(dto.getEmail())
                .country(dto.getCountry())
                .address(dto.getAddress())
                .mobileNumber(dto.getMobileNumber())
                .build();
        createUser(user);
        return user;
    }

    public User findUser (String username){
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            return user.get();
        }
        return null;
    }

    public boolean isAccountCorrect (SignInDTO dto){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<User> user = userRepository.findByUsername(dto.getUsername());
        User user1 = new User();
        if (user.isPresent()){
            user1 = user.get();
        }
        log.info(user1.getUsername());
        log.info(String.valueOf(encoder.matches(dto.getPassword(),user1.getPassword())));
        if (encoder.matches(dto.getPassword(),user1.getPassword()) && dto.getUsername().equals(user1.getUsername()))
            return true;
        return false;
    }

    public String encodePasswordEncoder (String password){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPw = encoder.encode(password);
        log.info(encodedPw);
        return encodedPw;
    }
}
