package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;

    @NonNull
    @Email(message = "Электронный адресс не соответствует формату")
    private String email;

    @NotBlank(message = "логин не может быть пустым и состоять из пробелов")
    private String login;

    private String name;

    @Past(message = "дата рождения не может быть в будущем")
    LocalDate birthday;
}
