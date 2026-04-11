package com.swaggerhub.clone.repository;

import com.swaggerhub.clone.model.entity.ApiTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApiTagRepository extends JpaRepository<ApiTag, UUID> {

    List<ApiTag> findBySpecId(UUID specId);

    void deleteBySpecId(UUID specId);
}
