package com.skthakur.bgn_backend.service;

import com.skthakur.bgn_backend.enums.ResourceType;
import com.skthakur.bgn_backend.model.Post;
import com.skthakur.bgn_backend.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {
    private final PostRepository repository;
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public PostService(PostRepository repository) {
        this.repository = repository;
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory.", e);
        }
    }

    // Create a new post with file upload
    public Post createPost(MultipartFile file, String title, String description, String author, String authorInfo, ResourceType resourceType, int semester) throws IOException {
        // Generate a unique file name using UUID
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

        Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        Post post = new Post(LocalDateTime.now(), title, description, author, authorInfo, originalFileName, targetLocation.toString(), resourceType, semester);
        return repository.save(post);
    }

    public Optional<Post> updatePost(Long id, MultipartFile file, String title, String description, String author, String authorInfo, ResourceType resourceType, int semester) throws IOException {
        Optional<Post> existingPostOpt = repository.findById(id);

        if (existingPostOpt.isPresent()) {
            Post existingPost = existingPostOpt.get();

            // Delete the old file if a new file is provided
            if (file != null && !file.isEmpty()) {
                Path oldFilePath = Paths.get(existingPost.getFilePath());
                Files.deleteIfExists(oldFilePath);

                // Generate a unique file name for the new file
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

                Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                existingPost.setFileName(originalFileName);  // Keep original filename
                existingPost.setFilePath(targetLocation.toString());  // Store unique file path
            }

            // Update other fields
            existingPost.setTitle(title);
            existingPost.setDescription(description);
            existingPost.setAuthor(author);
            existingPost.setAuthorInfo(authorInfo);
            existingPost.setResourceType(resourceType);
            existingPost.setSemester(semester);
            existingPost.setUpdatedAt(LocalDateTime.now());
            return Optional.of(repository.save(existingPost));
        }

        return existingPostOpt;


    }


    // Retrieve a list of posts by resource type and semester
    public List<Post> getPostList(ResourceType resourceType, int semester) {
        return repository.findByResourceTypeAndSemester(resourceType, semester);
    }

    // Retrieve a post by its ID
    public Optional<Post> getPostById(Long id) {
        return repository.findById(id);
    }


    // Delete a post by its ID
    public boolean deletePost(Long id) {
        Optional<Post> existingPostOpt = repository.findById(id);

        if (existingPostOpt.isPresent()) {
            Post existingPost = existingPostOpt.get();

            // Delete the file associated with the post
            Path filePath = Paths.get(existingPost.getFilePath());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not delete file: " + existingPost.getFilePath(), e);
            }

            repository.deleteById(id);
            return true;
        }
        return false;
    }

}
