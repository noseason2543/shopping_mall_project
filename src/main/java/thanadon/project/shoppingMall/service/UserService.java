package thanadon.project.shoppingMall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import thanadon.project.shoppingMall.model.User;
import thanadon.project.shoppingMall.repository.UserRepository;
import thanadon.project.shoppingMall.utils.UserUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> queryAllUser() {
        return userRepository.getAllUser();
    }
    @Transactional
    public ResponseEntity<Map<String, String>> createUser(User user) {
        Map<String, String> responseMessage = new HashMap<>();
        if ((user.getFirstName().isBlank() || user.getFirstName().isEmpty())
                || (user.getLastName().isEmpty() || user.getLastName().isBlank())) {
            responseMessage.put("message", "payload notFound input FirstName or LastName");
            return ResponseEntity.badRequest().body(responseMessage);
        }
        if (!UserUtils.validationPhoneNumber(user.getPhoneNumber())) {
            responseMessage.put("message", "phoneNumber format not correct.");
            return ResponseEntity.badRequest().body(responseMessage);
        }
        if (!UserUtils.validationGene(user.getGene())) {
            responseMessage.put("message", "Gene not correct.");
            return ResponseEntity.badRequest().body(responseMessage);
        }
        userRepository.createUser(user);
        responseMessage.put("message", "Insert new user completed.");
        return ResponseEntity.ok(responseMessage);
    }
    @Transactional
    public ResponseEntity<Map<String, String>> deleteUserService(String id) {
        Map<String, String> responseMessage = new HashMap<>();
        User user = userRepository.findUserById(id);
        if (user == null) {
            responseMessage.put("message", String.format("this user id: %s not found", id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }
        userRepository.deleteUser(id);
        responseMessage.put("message", String.format("this user id: %s was deleted.", id));
        return ResponseEntity.ok(responseMessage);
    }
    @Transactional
    public ResponseEntity<Map<String, String>> updateUserService(User payload) {
        Map<String, String> responseMessage = new HashMap<>();
        if (payload.getId().isEmpty() || payload.getId().isBlank()) {
            responseMessage.put("message", "id not found in payload.");
            return ResponseEntity.badRequest().body(responseMessage);
        }
        User user = userRepository.findUserById(payload.getId());
        if (user == null) {
            responseMessage.put("message",String.format("this id: %s is not found.", payload.getId()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }
        return null;
    }
}
