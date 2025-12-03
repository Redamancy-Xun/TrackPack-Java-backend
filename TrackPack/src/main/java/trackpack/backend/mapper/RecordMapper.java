package trackpack.backend.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import trackpack.backend.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Integer> {
}
