package kz.narxoz.finalproject.repositories;

import kz.narxoz.finalproject.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
