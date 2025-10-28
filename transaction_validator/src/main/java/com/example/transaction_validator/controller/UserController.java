package com.example.transaction_validator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction_validator.entities.User;
import com.example.transaction_validator.entities.enums.UserRole;
import com.example.transaction_validator.service.AccountService;
import com.example.transaction_validator.service.SignerService;
import com.example.transaction_validator.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final SignerService signerService;

    @Autowired
    public UserController(UserService userService, AccountService accountService, PasswordEncoder passwordEncoder, SignerService signerService) {
        this.userService = userService;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.signerService = signerService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(User user){
        if (user.getUserRole().equals(UserRole.ADMIN)){
            return ResponseEntity.ok(userService.getAllUsers());
        }
        else{
            return new ResponseEntity<>("Access Denied: You do not have the required permissions.", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/newuser")
    public ResponseEntity<?> createNewUser(@RequestParam String name,
                                           @RequestParam String email,
                                           @RequestParam String password){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PatchMapping("/updatename")
    public ResponseEntity<?> updateUserName(@RequestParam String name){
        if (name != null){
            User user = userService.getUser();
            user.setName(name);
            return ResponseEntity.ok(userService.updateUser(user));
        }
        else {
            return ResponseEntity
                .badRequest() // Sets the status to 400
                .body("Illegal name change");
        }
    }

    @PatchMapping("/updateemail")
    public ResponseEntity<?> updateUserEmail(@RequestParam String email){
        if (email != null){
            User user = userService.getUser();
            user.setEmail(email);
            return ResponseEntity.ok(userService.updateUser(user));
        }
        else {
            return ResponseEntity
                .badRequest() // Sets the status to 400
                .body("Illegal email change");
        }
    }

    @PatchMapping("/updatepassword")
    public ResponseEntity<?> updateUserPassword(@RequestParam String password){
        if (password != null){
            User user = userService.getUser();
            String hashed_password = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashed_password);
            return ResponseEntity.ok(userService.updateUser(user));
        }
        else {
            return ResponseEntity
                .badRequest() // Sets the status to 400
                .body("Illegal password change");
        }
    }

    @PatchMapping("/updaterole")
    public ResponseEntity<?> updateRole(@RequestParam int role){
        User user = userService.getUser();
        
        if (role == 0){
            user.setUserRole(UserRole.ADMIN);
        }
        else {
            user.setUserRole(UserRole.USER);
        }

        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/deleteuser")
    public ResponseEntity<?> deleteUser(){
        return ResponseEntity.ok(userService.deleteUser());
    }

    @GetMapping("/getaccounts")
    public ResponseEntity<?> getUserAccounts(){
        return ResponseEntity.ok(accountService.getAccountsByUser());
    }

    @GetMapping("/getenvelopes")
    public ResponseEntity<?> getUserEnvelopes(){
        User user = userService.getUser();
        return ResponseEntity.ok(signerService.getSignersForUser(user));
    }
}
