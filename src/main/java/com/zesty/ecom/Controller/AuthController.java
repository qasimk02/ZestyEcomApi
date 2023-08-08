package com.zesty.ecom.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zesty.ecom.Payload.Dto.UserDto;
import com.zesty.ecom.Payload.Request.JwtRequest;
import com.zesty.ecom.Payload.Response.JwtResponse;
import com.zesty.ecom.Security.JwtHelper;
import com.zesty.ecom.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;
    
    @Autowired
    private UserService userService;


    @PostMapping("/signup")
	public ResponseEntity<JwtResponse> createUser(@Valid @RequestBody UserDto userDto){
    	
    	//saving the user
		UserDto savedUser= userService.createUser(userDto);
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = this.helper.generateToken(userDetails);
        
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .message("success")
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
    
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
    	
    	//authentication
        this.doAuthenticate(request.getEmail(), request.getPassword());
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .message("success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Email or Password  !!");
		}

	}
}
