package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final String ADDUSER = "MERGE INTO USERS (id, name, login, email, birthday) values (?,?,?,?,?)";
    private final String ADDUSERFRIENDS = "MERGE INTO FRIENDS (request_user_id, response_user_id) " +
            "KEY(request_user_id, response_user_id) values (?,?)";
    private final String UPDATEUSER = "UPDATE USERS SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
    private final String GETUSER = "SELECT * FROM USERS WHERE id = ?";
    private final String GETUSERS = "SELECT * FROM USERS";
    private final String GETFRIENDS = "SELECT response_user_id FROM FRIENDS WHERE request_user_id = ?";
    private final String DELETEFRIEND = "DELETE FROM FRIENDS WHERE request_user_id = ? AND response_user_id = ?";
    private JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        jdbcTemplate.update(ADDUSER, user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        for (Long id : user.getFriends()) {
            jdbcTemplate.update(ADDUSERFRIENDS, user.getId(), id);
        }
        log.debug(user.toString());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = jdbcTemplate.query(GETUSERS, ((rs, rowNum) -> {
            return makeUser(rs);
        }));
        log.debug(users.toString());
        return users;
    }

    @Override
    public User getUser(Long id) {
        log.debug(id.toString());
        return jdbcTemplate.queryForObject(GETUSER, (rs, rowNum) -> {
            return makeUser(rs);
        }, id);
    }

    @Override
    public Map<Long, User> getUsers() {
        Map<Long, User> users = new HashMap<>();
        for (User user : getAllUsers()) {
            users.put(user.getId(), user);
        }
        log.debug(users.toString());
        return users;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(UPDATEUSER, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        for (Long id : user.getFriends())
            jdbcTemplate.update(ADDUSERFRIENDS, user.getId(), id);
        log.debug(user.toString());
        return user;
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        jdbcTemplate.update(DELETEFRIEND, userId, friendId);
        log.debug(userId.toString(), friendId.toString());
    }

    private List<Long> getFriends(Long id) {
        return jdbcTemplate.query(GETFRIENDS, ((rs, rowNum) -> {
            return rs.getLong("response_user_id");
        }), id);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Long idUser = rs.getLong("id");
        String name = rs.getString("name");
        String login = rs.getString("login");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = new User(name, login, email, birthday);
        for (Long friendId : getFriends(idUser)) {
            user.addFriend(friendId);
        }
        user.setId(idUser);
        return user;
    }

}
