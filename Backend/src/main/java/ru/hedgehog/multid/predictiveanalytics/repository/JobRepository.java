package ru.hedgehog.multid.predictiveanalytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hedgehog.multid.predictiveanalytics.domain.Job;

/**
 * @author Anikushin Roman
 * Репозиторий для получения {@link Job} из базы данных
 */
public interface JobRepository extends JpaRepository<Job, Long> {

}
