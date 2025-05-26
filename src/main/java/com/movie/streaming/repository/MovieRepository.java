package com.movie.streaming.repository;

import com.movie.streaming.dto.MovieSearchCriteria;
import com.movie.streaming.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


import java.util.ArrayList;


@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long>,
        JpaSpecificationExecutor<MovieEntity> {

    // Recherches basiques
    List<MovieEntity> findByTitleContainingIgnoreCase(String title);

    // Recherche paginée
    Page<MovieEntity> findByCategory_Id(Long categoryId, Pageable pageable);

    // Recherche complexe avec plusieurs critères
    @Query("SELECT m FROM MovieEntity m " +
            "WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:categoryId IS NULL OR m.category = :categoryId) " +
            "AND (:minDuration IS NULL OR m.duration >= :minDuration) " +
            "AND (:maxDuration IS NULL OR m.duration <= :maxDuration)")
    Page<MovieEntity> searchMovies(
            @Param("title") String title,
            @Param("categoryId") Long categoryId,
            @Param("minDuration") Integer minDuration,
            @Param("maxDuration") Integer maxDuration,
            Pageable pageable
    );

    // Requête personnalisée pour les statistiques
    @Query("SELECT COUNT(m), m.category FROM MovieEntity m " +
            "GROUP BY m.category")
    List<Object[]> countMoviesByCategory();

    // Recherche par plusieurs critères avec critères dynamiques
    default Page<MovieEntity> findByFilters(MovieSearchCriteria criteria, Pageable pageable) {
        return findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (criteria.getTitle() != null) {
                predicates.add(cb.like(
                        cb.lower(root.get("title")),
                        "%" + criteria.getTitle().toLowerCase() + "%"
                ));
            }

            if (criteria.getCategory() != null) {
                predicates.add(cb.equal(
                        root.get("category"),
                        criteria.getCategory().getDisplayName()
                ));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);
    }
}

