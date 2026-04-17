package com.swaggerhub.clone.repository;

import com.swaggerhub.clone.model.ApiDefinition;
import com.swaggerhub.clone.model.ApiStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDefinitionRepository extends JpaRepository<ApiDefinition, Long> {

    Page<ApiDefinition> findByStatus(ApiStatus status, Pageable pageable);

    Page<ApiDefinition> findByStatusNot(ApiStatus status, Pageable pageable);

    Page<ApiDefinition> findByStatusNotAndNameContainingIgnoreCase(ApiStatus status, String name, Pageable pageable);

    @Query("""
        SELECT a FROM ApiDefinition a
        WHERE a.status != :status
        AND (:search IS NULL OR :search = ''
             OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:category IS NULL OR :category = '' OR LOWER(a.category) = LOWER(:category))
        """)
    Page<ApiDefinition> findByStatusNotAndNameContainingIgnoreCaseAndCategory(
        @Param("status") ApiStatus status,
        @Param("search") String search,
        @Param("category") String category,
        Pageable pageable
    );

    @Query("""
        SELECT a FROM ApiDefinition a
        WHERE a.status = :status
        AND (:search IS NULL OR :search = ''
             OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:category IS NULL OR :category = '' OR LOWER(a.category) = LOWER(:category))
        """)
    Page<ApiDefinition> findByStatusAndSearchAndCategory(
        @Param("status") ApiStatus status,
        @Param("search") String search,
        @Param("category") String category,
        Pageable pageable
    );

    long countByStatus(ApiStatus status);
}
