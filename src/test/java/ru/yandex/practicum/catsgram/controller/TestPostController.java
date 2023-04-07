package ru.yandex.practicum.catsgram.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.PostService;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestPostController {
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    PostController controller;

    Post post1;
    Post post2;
    Post post3;
    Post post4;
    User user1;

    @BeforeEach
    void beforeEach() throws InterruptedException {
        user1 = new User("testmail", "nickname", LocalDate.now());
        post1 = new Post(1, "testmail", "test description", "someUrl");
        sleep(10);
        post2 = new Post(2, "testmail", "test description", "someUrl");
        sleep(10);
        post3 = new Post(3, "testmail", "test description", "someUrl");
        sleep(10);
        post4 = new Post(4, "testmail", "test description", "someUrl");
    }

    @Test
    public void createPost_AndExpect200() {
        restTemplate.postForEntity("/users", user1, User.class);
        ResponseEntity<Post> responseEntity = restTemplate.postForEntity("/post", post1, Post.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getId(), is(post1.getId()));
    }

    @Test
    public void getAllPosts_WithoutArgs_AndExpect200() throws InterruptedException {
        restTemplate.postForEntity("/users", user1, User.class);
        restTemplate.postForEntity("/post", post1, Post.class);
        restTemplate.postForEntity("/post", post2, Post.class);
        restTemplate.postForEntity("/post", post3, Post.class);
        restTemplate.postForEntity("/post", post4, Post.class);

        ResponseEntity<Post[]> responseEntity = restTemplate.getForEntity("/posts", Post[].class);

        assertThat(responseEntity.getBody()[3].getId(), is(post1.getId()));
        assertThat(responseEntity.getBody()[2].getId(), is(post2.getId()));
        assertThat(responseEntity.getBody()[1].getId(), is(post3.getId()));
        assertThat(responseEntity.getBody()[0].getId(), is(post4.getId()));

    }

    @Test
    public void getAllPosts_WithSortArgDesc_AndExpect200() {
        restTemplate.postForEntity("/users", user1, User.class);
        restTemplate.postForEntity("/post", post1, Post.class);
        restTemplate.postForEntity("/post", post2, Post.class);
        restTemplate.postForEntity("/post", post3, Post.class);
        restTemplate.postForEntity("/post", post4, Post.class);

        ResponseEntity<Post[]> responseEntity = restTemplate.getForEntity("/posts?sort=desc", Post[].class);

        assertThat(responseEntity.getBody()[0].getId(), is(post1.getId()));
        assertThat(responseEntity.getBody()[1].getId(), is(post2.getId()));
        assertThat(responseEntity.getBody()[2].getId(), is(post3.getId()));
        assertThat(responseEntity.getBody()[3].getId(), is(post4.getId()));
    }

    @Test
    public void getAllPosts_WithSortArgDescAndSize_AndExpect200() {
        restTemplate.postForEntity("/users", user1, User.class);
        restTemplate.postForEntity("/post", post1, Post.class);
        restTemplate.postForEntity("/post", post2, Post.class);
        restTemplate.postForEntity("/post", post3, Post.class);
        restTemplate.postForEntity("/post", post4, Post.class);

        ResponseEntity<Post[]> responseEntity = restTemplate.getForEntity("/posts?sort=desc&size=2", Post[].class);

        assertThat(responseEntity.getBody().length, is(2));
        assertThat(responseEntity.getBody()[0].getId(), is(post1.getId()));
        assertThat(responseEntity.getBody()[1].getId(), is(post2.getId()));
    }
}
