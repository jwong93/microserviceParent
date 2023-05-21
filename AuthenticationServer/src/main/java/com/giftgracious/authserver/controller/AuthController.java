package com.giftgracious.authserver.controller;

import com.giftgracious.authserver.dto.SignInDTO;
import com.giftgracious.authserver.dto.SignUpDTO;
import com.giftgracious.authserver.model.User;
import com.giftgracious.authserver.security.TokenGenerator;
import com.giftgracious.authserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    TokenGenerator tokenGenerator;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody SignUpDTO signUpDTO){
        if (userService.userExists(signUpDTO.getUserName())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        log.info("Registering user");
        User user = userService.registerUser(signUpDTO);
        log.info("User {} registered", user.getUsername());
        return ResponseEntity.ok(user.getId());
    }


    @PostMapping("/login")
    public ResponseEntity login (@RequestBody SignInDTO dto){
        if (userService.isAccountCorrect(dto)){
            User user = userService.findUser(dto.getUsername());
            if (user!=null) {
                Authentication auth = UsernamePasswordAuthenticationToken.authenticated(user, dto.getPassword(), Collections.EMPTY_LIST);
                return ResponseEntity.ok(tokenGenerator.createToken(auth));
            }
        }
        return ResponseEntity.notFound().build();
    }
}
