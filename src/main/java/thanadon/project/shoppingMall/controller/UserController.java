package thanadon.project.shoppingMall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanadon.project.shoppingMall.model.User;
import thanadon.project.shoppingMall.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/allUser")
    public ResponseEntity<List<User>> requestAllUser() {
        return ResponseEntity.ok(userService.queryAllUser());
    }

    @PostMapping("/createUser")
    public ResponseEntity<Map<String, String>> requestCreateUser(@RequestBody @Valid User user) {
        return  userService.createUser(user);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<Map<String, String>> requestDeleteUser(@RequestBody @Valid String id) {
        return userService.deleteUserService(id);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<Map<String, String>> requestUpdateUser(@RequestBody @Valid User user) {
        return null;
    }
}
