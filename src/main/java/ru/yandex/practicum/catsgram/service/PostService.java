package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final List<Post> posts = new ArrayList<>();
    final UserService userService;

    @Autowired
    PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll() {
        return posts;
    }

    public Optional<Post> findPostById(int postId) {
        return posts.stream()
                .filter(x -> x.getId() == postId)
                .findFirst();
    }

    public Post create(Post post) {
        User author = userService.findUserByEmail(post.getAuthor());

        if (author == null) {
            throw new UserNotFoundException("Юзер не найден!");
        }

        posts.add(post);
        return post;
    }
}
