package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //PUT /users/{id}/friends/{friendId} - добавления друга по его id
    public void addFriend(Integer id, Integer friendId) {
        User userSend = userStorage.getUserById(id);
        User userReceive = userStorage.getUserById(friendId);
        if (!userSend.getFriends().isEmpty() && !userReceive.getFriends().isEmpty()) {
            if (userSend.getFriends().contains(friendId)) {
                log.error(String.format("Пользователь с id - %d, уже есть в вашем списке друзей", friendId));
                throw new DuplicatedDataException(String.format("Пользователь с id - %d, уже есть в вашем списке друзей", friendId));
            }

        }
        log.info("Пользователь с id {} добавиль пользователя с id - {} в друзья", id, friendId);
        userSend.getFriends().add(friendId);
        userReceive.getFriends().add(id);
    }

    //Для DELETE /users/{id}/friends/{friendId} - удалить друга по его id
    public void deleteFriend(Integer id, Integer friendId) {
        User userSend = userStorage.getUserById(id);
        User userReceive = userStorage.getUserById(friendId);

        if (userSend.getFriends().contains(friendId) && userReceive.getFriends().contains(id)) {
            log.info("Пользователь с id {} удалил пользователя с id - {} из  друзья", id, friendId);
            userSend.getFriends().remove(friendId);
            userReceive.getFriends().remove(id);
        }
    }

    //GET /users/{id}/friends - возврашает список всех друзей пользователя по его id
    public List<User> findAllFriends(Integer id) {
        Set<Integer> friendsId = userStorage.getUserById(id).getFriends();
        ArrayList<User> userFriends = new ArrayList<>();
        for (Integer i : friendsId) {
            userFriends.add(userStorage.getUserById(i));
        }
        log.info("Пользователь с id = {} получил список друзей", id);
        return userFriends;
    }

    //GET /users/{id}/friends/common/{otherId} - возвращает список общих друзей с другим пользователем
    public List<User> findCommonFriends(Integer id, Integer otherId) {
        User thisUser = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);

        Set<Integer> commonFriendsId = new HashSet<>(thisUser.getFriends());
        commonFriendsId.retainAll(otherUser.getFriends());
        /*Метод retainAll() используется для пересечения множеств. Он модифицирует множество, на котором вызван, так,
        чтобы оно содержало только те элементы, которые также присутствуют в указанном наборе.*/
        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : commonFriendsId) {
            //User friend = users.get(friendId);
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                commonFriends.add(friend);
            }
        }
        log.info("Пользователь с id = {} получил список общих друзей с пользователем {}", id, otherId);
        return commonFriends;
    }
}
