package com.skthakur.bgn_backend.repository;


import com.skthakur.bgn_backend.enums.ResourceType;
import com.skthakur.bgn_backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByResourceTypeAndSemester(ResourceType resourceType, int semester);

}

