package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;
    private FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя добавить самого себя в друзья!");
        }
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя удалить самого себя из друзей!");
        }
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> findAllFriends(Integer userId) {
        List<User> friends = new ArrayList<>();
        if (userId != null) {
            friends = friendStorage.getFriends(userId);
        }
        return friends;
    }

    public List<User> findCommonFriends(Integer firstUserId, Integer secondUserId) {

        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);
        Set<User> intersection = null;

        if ((firstUser != null) && (secondUser != null)) {
            intersection = new HashSet<>(friendStorage.getFriends(firstUserId));
            intersection.retainAll(friendStorage.getFriends(secondUserId));
        }
        return new ArrayList<User>(intersection);
    }

    /*private UserStorage userStorage;
    private FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }


    public void addFriend(Integer userId, Integer friendId) {
        User userSend = userStorage.getUserById(userId);
        User userReceive = userStorage.getUserById(friendId);
        if (userId == friendId) {
            throw new ValidationException("Нельзя добавить самого себя в друзья!");
        }

        if (!userSend.getFriends().isEmpty() && !userReceive.getFriends().isEmpty()) {
            if (userSend.getFriends().contains(friendId)) {
                log.error("Пользователь с userId - {}, уже есть в вашем списке друзей", friendId);
                throw new DuplicatedDataException(String.format("Пользователь с userId - %d, уже есть в вашем списке друзей", friendId));
            }

        }
        log.info("Пользователь с userId {} добавиль пользователя с userId - {} в друзья", userId, friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User userSend = userStorage.getUserById(userId);
        User userReceive = userStorage.getUserById(friendId);

        if (userId == friendId) {
            throw new ValidationException("Нельзя удалить самого себя из друзей!");
        }

        if (userSend.getFriends().contains(friendId) && userReceive.getFriends().contains(userId)) {
            log.info("Пользователь с id {} удалил пользователя с id - {} из  друзья", userId, friendId);
            friendStorage.deleteFriend(userId, friendId);
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
        *//*Метод retainAll() используется для пересечения множеств. Он модифицирует множество, на котором вызван, так,
        чтобы оно содержало только те элементы, которые также присутствуют в указанном наборе.*//*
        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : commonFriendsId) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                commonFriends.add(friend);
            }
        }
        log.info("Пользователь с id = {} получил список общих друзей с пользователем {}", myId, otherId);
        return commonFriends;
    }*/
}
