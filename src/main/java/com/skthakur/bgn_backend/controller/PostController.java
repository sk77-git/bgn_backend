package com.skthakur.bgn_backend.controller;

import com.skthakur.bgn_backend.dto.PostRequestDto;
import com.skthakur.bgn_backend.enums.ResourceType;
import com.skthakur.bgn_backend.model.Post;
import com.skthakur.bgn_backend.service.PostService;
import com.skthakur.bgn_backend.util.ResponseWrapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    private <T> ResponseEntity<ResponseWrapper<T>> buildResponse(HttpStatus status, String message, T data, Map<String, String> errors) {
        return ResponseEntity.status(status).body(new ResponseWrapper<>(status, message, data, errors));
    }

    // Create a post
    @PostMapping("/admin/post")
    public ResponseEntity<ResponseWrapper<Post>> createRecord(@Valid @ModelAttribute PostRequestDto postRequestDto) {
        try {
            Post post = postService.createPost(
                    postRequestDto.getFile(),
                    postRequestDto.getTitle(),
                    postRequestDto.getDescription(),
                    postRequestDto.getAuthor(),
                    postRequestDto.getAuthorInfo(),
                    postRequestDto.getResourceType(),
                    postRequestDto.getSemester()
            );

            return buildResponse(HttpStatus.CREATED, "Post created successfully.", post, null);
        } catch (IOException e) {
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed.", null, Map.of("file", e.getMessage()));
        }
    }

    // Update a Post
    @PutMapping("/admin/post/{id}")
    public ResponseEntity<ResponseWrapper<Post>> updatePost(@PathVariable Long id, @Valid @ModelAttribute PostRequestDto postRequestDto) {
        try {
            Optional<Post> updatedPost = postService.updatePost(
                    id,
                    postRequestDto.getFile(),
                    postRequestDto.getTitle(),
                    postRequestDto.getDescription(),
                    postRequestDto.getAuthor(),
                    postRequestDto.getAuthorInfo(),
                    postRequestDto.getResourceType(),
                    postRequestDto.getSemester()
            );

            return updatedPost
                    .map(post -> buildResponse(HttpStatus.OK, "Post updated successfully.", post, null))
                    .orElseGet(() -> buildResponse(HttpStatus.NOT_FOUND, "Post not found.", null, null));
        } catch (IOException e) {
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed.", null, Map.of("file", e.getMessage()));
        }
    }

    // List all Posts
    @GetMapping("/posts")
    public ResponseEntity<ResponseWrapper<List<Post>>> getPosts(
            @RequestParam("resource_type") ResourceType resourceType,
            @RequestParam("semester") int semester
    ) {
        List<Post> posts = postService.getPostList(resourceType, semester);
        return buildResponse(HttpStatus.OK, "Posts fetched successfully.", posts, null);
    }

    // Get a single post by ID
    @GetMapping("/posts/{id}")
    public ResponseEntity<ResponseWrapper<Post>> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(post -> buildResponse(HttpStatus.OK, "Post fetched successfully.", post, null))
                .orElseGet(() -> buildResponse(HttpStatus.NOT_FOUND, "Post not found.", null, null));
    }

    // Delete a post by ID
    @DeleteMapping("/admin/posts/{id}")
    public ResponseEntity<ResponseWrapper<String>> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);
        return deleted
                ? buildResponse(HttpStatus.OK, "Post deleted successfully.", null, null)
                : buildResponse(HttpStatus.NOT_FOUND, "Post not found.", null, null);
    }
}
