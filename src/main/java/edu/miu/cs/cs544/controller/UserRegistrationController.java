package edu.miu.cs.cs544.controller;


import edu.miu.cs.cs544.domain.*;
import edu.miu.cs.cs544.domain.dto.CustomerDTO;
import edu.miu.cs.cs544.domain.dto.PasswordDTO;
import edu.miu.cs.cs544.domain.dto.UserDTO;
import edu.miu.cs.cs544.domain.dto.UserUpdateDTO;
import edu.miu.cs.cs544.event.RegistrationCompleteEvent;
import edu.miu.cs.cs544.repository.CustomerRepository;
import edu.miu.cs.cs544.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController

public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private CustomerRepository customerRepository;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CustomerDTO customerDTO, HttpServletRequest request) {

        try {
            User user = userService.registerUser(customerDTO);
            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        }
        catch (CustomError e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Successfully Registered, check your email to verify your account", HttpStatus.OK);
    }
    @PostMapping("/api/register")
    public ResponseEntity<?> registerAdmin(@RequestBody UserDTO userDTO, HttpServletRequest request, Authentication authentication){

        try {
            User user = userService.registerAdmin(userDTO,getEmailFromAuthentication(authentication));
            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        }
        catch (CustomError e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Successfully Registered, check your email to verify your account", HttpStatus.OK);
    }
    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            return  new ResponseEntity<>("User verified successfully", HttpStatus.OK);
        }
        return  new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/resendRegistrationToken")
    public ResponseEntity<?> resendRegistrationToken(@RequestParam("token") String existingToken, HttpServletRequest request) {
        VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        User user = newToken.getUser();
        resendVerificationToken(user, newToken, applicationUrl(request));
        return  new ResponseEntity<>("Token Re-sent Successfully", HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
public String resetPassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordDTO.getEmail());
        String url = applicationUrl(request);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, token, applicationUrl(request));
        }
        return url;
    }
    @GetMapping("/")
    public ResponseEntity<?> hello(Authentication authentication) throws CustomError{
       return new ResponseEntity<>(getEmailFromAuthentication(authentication), HttpStatus.OK);
    }
    private String getEmailFromAuthentication(Authentication authentication) throws CustomError {
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return (String) jwtAuthenticationToken.getTokenAttributes().get("email");
        } else if (authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            return usernamePasswordAuthenticationToken.getName();
        }
        else if (authentication != null && authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal oauth2User) {
            String email = oauth2User.getAttribute("email");
            Customer user = customerRepository.findByEmail(email);
            if(user== null){
                throw new CustomError("User not found", HttpStatus.BAD_REQUEST);
            }
            return user.getEmail();
        }
        else {
            throw new IllegalArgumentException("Authentication method not supported");
        }
    }
    private String passwordResetTokenMail(User user, String token, String appUrl) {
        String url = appUrl + "/savePassword?id=" + user.getId() + "&token=" + token;
        //send password reset Email
        log.info("Click the link to reset your password: " + url);
        return url;
    }




    @PostMapping("/api/savePassword")
    public ResponseEntity<?> savePassword(@RequestParam("token") String token, @RequestBody PasswordDTO passwordDTO) {
        String result = userService.ValidatePasswordResetToken(token);

        if(!result.equals("valid")){
            return  new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userService.getUserByPasswordToken(token);
        if(user.isPresent()){
            userService.changeUserPassword(user.get(), passwordDTO.getNewPassword());
            return  new ResponseEntity<>("Password Changed Successfully", HttpStatus.OK);
        }
        else {
            return  new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/api/changePassword")
    public String ChangePassword(@RequestBody PasswordDTO passwordDTO) {
        User user = userService.findUserByEmail(passwordDTO.getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordDTO.getOldPassword())) {
            return "Invalid Old Password";
        }
        //Save new password
     userService.changeUserPassword(user, passwordDTO.getNewPassword());
        return "password changed successfully";
    }
    private void resendVerificationToken(User user, VerificationToken newToken, String appUrl) {
        String url = appUrl + "/registrationConfirm?token=" + newToken.getToken();
        //send verification Email
        log.info("Click the link to verify your account: " + url);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @DeleteMapping("/api/deleteUser")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        try {
            User user = userService.findUserByEmail(authentication.getName());
            if (user != null) {
                userService.deleteUser(user.getId());
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (CustomError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, Authentication authentication) {
        try {
            User user = userService.findUserByEmail(authentication.getName());
            if (user != null) {
                // Update user details based on the fields available in userUpdateDTO
                if (userUpdateDTO.getName() != null) {
                    user.setUserName(userUpdateDTO.getName());
                }
                if (userUpdateDTO.getEmail() != null) {
                    user.setEmail(userUpdateDTO.getEmail());
                }
                userService.updateUserDetails(user);
                return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (CustomError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
