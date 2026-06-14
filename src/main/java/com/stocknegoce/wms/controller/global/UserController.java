package com.stocknegoce.wms.controller.global;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.controller.GenericController;
import com.stocknegoce.wms.model.User;
import com.stocknegoce.wms.repository.global.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController extends GenericController<User, Integer> {

    @Autowired
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody User user) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
        }

        user.setId_user(id);
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }
}
