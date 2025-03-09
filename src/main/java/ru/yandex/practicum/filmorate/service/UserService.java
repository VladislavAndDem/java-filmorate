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

    public void addFriend(Integer myId, Integer friendId) {
        User userSend = userStorage.getUserById(myId);
        User userReceive = userStorage.getUserById(friendId);
        if (!userSend.getFriends().isEmpty() && !userReceive.getFriends().isEmpty()) {
            if (userSend.getFriends().contains(friendId)) {
                log.error("Пользователь с id - {}, уже есть в вашем списке друзей", friendId);
                throw new DuplicatedDataException(String.format("Пользователь с id - %d, уже есть в вашем списке друзей", friendId));
            }

        }
        log.info("Пользователь с id {} добавиль пользователя с id - {} в друзья", myId, friendId);
        userSend.getFriends().add(friendId);
        userReceive.getFriends().add(myId);
    }

    public void deleteFriend(Integer myId, Integer friendId) {
        User userSend = userStorage.getUserById(myId);
        User userReceive = userStorage.getUserById(friendId);

        if (userSend.getFriends().contains(friendId) && userReceive.getFriends().contains(myId)) {
            log.info("Пользователь с id {} удалил пользователя с id - {} из  друзья", myId, friendId);
            userSend.getFriends().remove(friendId);
            userReceive.getFriends().remove(myId);
        }
    }

    public List<User> findAllFriends(Integer id) {
        Set<Integer> friendsId = userStorage.getUserById(id).getFriends();
        ArrayList<User> userFriends = new ArrayList<>();
        for (Integer i : friendsId) {
            userFriends.add(userStorage.getUserById(i));
        }
        log.info("Пользователь с id = {} получил список друзей", id);
        return userFriends;
    }

    public List<User> findCommonFriends(Integer myId, Integer otherId) {
        User thisUser = userStorage.getUserById(myId);
        User otherUser = userStorage.getUserById(otherId);

        Set<Integer> commonFriendsId = new HashSet<>(thisUser.getFriends());
        commonFriendsId.retainAll(otherUser.getFriends());
        /*Метод retainAll() используется для пересечения множеств. Он модифицирует множество, на котором вызван, так,
        чтобы оно содержало только те элементы, которые также присутствуют в указанном наборе.*/
        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : commonFriendsId) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                commonFriends.add(friend);
            }
        }
        log.info("Пользователь с id = {} получил список общих друзей с пользователем {}", myId, otherId);
        return commonFriends;
    }
}
