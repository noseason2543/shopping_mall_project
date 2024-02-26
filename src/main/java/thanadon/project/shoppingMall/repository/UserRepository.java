package thanadon.project.shoppingMall.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import thanadon.project.shoppingMall.model.User;
import thanadon.project.shoppingMall.utils.UserUtils;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private DataSource dataSource;

    public List<User> getAllUser() {
        String query = """
                SELECT * FROM user_service.users
                """;
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            User user = new User().setId(rs.getString("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setEmail(rs.getString("email"))
                    .setDateOfBirth(rs.getDate("date_of_birth"))
                    .setLastName(rs.getString("last_name"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setGene(rs.getString("gene"))
                    .setLastLoginTime(rs.getDate("last_login_time"));
            if (rs.getString("role") != null) {
                user.setRole(UserUtils.userRole.valueOf(rs.getString("role")));
            }
            return user;
        });
    }

    public User findUserById(String id) {
        String query = """
                SELECT id FROM user_service.users WHERE id = ?
                """;
        List<User> users = jdbcTemplate.query(query, new Object[]{id}, (rs, rowNum) -> {
            User user = new User().setId(rs.getString("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setEmail(rs.getString("email"))
                    .setDateOfBirth(rs.getDate("date_of_birth"))
                    .setLastName(rs.getString("last_name"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setGene(rs.getString("gene"))
                    .setLastLoginTime(rs.getDate("last_login_time"));
            if (rs.getString("role") != null) {
                user.setRole(UserUtils.userRole.valueOf(rs.getString("role")));
            }
            return user;
        });
        return users.isEmpty() ? null : users.get(0);
    }
    //namedParameterJdbcTemplate is better
    public void createUser(User user) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        String query = """
                INSERT INTO user_service.users(id, user_name, password, first_name, last_name, email, gene, date_of_birth, role, phone_number)
                VALUES(uuid_generate_v4(), :userName, :password, :firstName, :lastName, :email, :gene, :dateOfBirth, :role, :phoneNumber)
                """;
        mapSqlParameterSource.addValue("userName", user.getUserName());
        mapSqlParameterSource.addValue("password", user.getPassword());
        mapSqlParameterSource.addValue("firstName", user.getFirstName());
        mapSqlParameterSource.addValue("lastName", user.getLastName());
        mapSqlParameterSource.addValue("email", user.getEmail());
        mapSqlParameterSource.addValue("gene", user.getGene());
        mapSqlParameterSource.addValue("dateOfBirth", user.getDateOfBirth());
        mapSqlParameterSource.addValue("role", user.getRole().toString());
        mapSqlParameterSource.addValue("phoneNumber", user.getPhoneNumber());
// cannot use because role is enum it need to convert to String first.
//        namedParameterJdbcTemplate.update(query, new BeanPropertySqlParameterSource(user));
        namedParameterJdbcTemplate.update(query, mapSqlParameterSource);
    }

    public void deleteUser(String id) {
        String query = """
                DELETE FROM user_service.users WHERE id = ?
                """;
        jdbcTemplate.update(query, id);
    }

    public void updateUser(User user) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        List<Object> params = new ArrayList<>();
        StringBuilder query = new StringBuilder("""
                UPDATE user_service.users SET 
                """);
        StringBuilder setUpdate = new StringBuilder();
        if (user.getFirstName() != null) {
            setUpdate.append("first_name = :first_name, ");
            mapSqlParameterSource.addValue("first_name", user.getFirstName());
        }
        if (user.getLastName() != null) {
            setUpdate.append("last_name = :last_name, ");
            mapSqlParameterSource.addValue("last_name", user.getLastName());
        }
        if (user.getEmail() != null) {
            setUpdate.append("email = :email, ");
            mapSqlParameterSource.addValue("email", user.getEmail());
        }
        if (user.getPhoneNumber() != null) {
            setUpdate.append("phone_number = :phone_number, ");
            mapSqlParameterSource.addValue("phone_number", user.getPhoneNumber());
        }
        if (user.getRole() != null) {
            setUpdate.append("role = :role, ");
            mapSqlParameterSource.addValue("role", user.getRole());
        }
        if (setUpdate.length() > 0) {
            String whereCause = " WHERE id = :id";
            mapSqlParameterSource.addValue("id", user.getId());
            query.append(setUpdate.substring(0, setUpdate.length() - 2)).append(whereCause);
        }
        namedParameterJdbcTemplate.update(query.toString(), mapSqlParameterSource);
    }

//    public void createUsers(List<User> users) {
//        String query = """
//            INSERT INTO user_service.users(id, user_name, password, first_name, last_name, email, gene, date_of_birth, role, phone_number)
//            VALUES(uuid_generate_v4(), :user_name, :password, :first_name, :last_name, :email, :gene, :date_of_birth, :role, :phone_number)
//            """;
//
//        List<MapSqlParameterSource> batchArgs = new ArrayList<>();
//        for (User user : users) {
//            MapSqlParameterSource params = new MapSqlParameterSource();
//            params.addValue("user_name", user.getUserName());
//            params.addValue("password", user.getPassword());
//            params.addValue("first_name", user.getFirstName());
//            params.addValue("last_name", user.getLastName());
//            params.addValue("email", user.getEmail());
//            params.addValue("gene", user.getGene());
//            params.addValue("date_of_birth", user.getDateOfBirth());
//            params.addValue("role", user.getRole());
//            params.addValue("phone_number", user.getPhoneNumber());
//            batchArgs.add(params);
//        }
//
//        namedParameterJdbcTemplate.batchUpdate(query, batchArgs.toArray(new MapSqlParameterSource[0]));
//    }

}
