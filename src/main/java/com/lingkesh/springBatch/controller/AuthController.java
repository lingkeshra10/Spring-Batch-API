package com.lingkesh.springBatch.controller;

import com.lingkesh.springBatch.model.ResponseModel;
import com.lingkesh.springBatch.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseModel> authenticate(@RequestParam String username, @RequestParam String password) {
        ResponseModel responseModel = new ResponseModel();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            responseModel.setCode(ResponseModel.AUTH_LOGIN_SUCCESS);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.AUTH_LOGIN_SUCCESS));
            responseModel.setObject(jwtUtil.generateToken(userDetails));
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }catch (Exception ex){
            responseModel.setCode(ResponseModel.EXCEPTION_ERROR);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.EXCEPTION_ERROR)  + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
    }
}