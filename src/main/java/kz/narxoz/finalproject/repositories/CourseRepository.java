package kz.narxoz.finalproject.repositories;

import kz.narxoz.finalproject.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
