package ru.hedgehog.multid.predictiveanalytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hedgehog.multid.predictiveanalytics.domain.Job;
import ru.hedgehog.multid.predictiveanalytics.domain.JobReference;
import ru.hedgehog.multid.predictiveanalytics.repository.JobRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Anikushin Roman
 * Сервис, отвечающий за различные операции с {@link Job}
 * Выступает промежуточным звеном между {@link ru.hedgehog.multid.predictiveanalytics.controller.JobController JobController} и {@link JobRepository}
 */
@Service
@RequiredArgsConstructor
public class JobService {

	private final JobRepository jobRepository;

	@Value("${cost.job}")
	private double costForJob;

	@Value("${cost.mile}")
	private double costForMile;

	/**
	 * Метод получения страницы, состоящих из {@link Job}, из базы данных
	 * @param page номер страницы (от 1)
	 * @param size количество элеметов на странице
	 * @param desc порядок сортировки
	 * @param sortBy критерий сортировки
	 * @return страница из работ
	 */
	public Page<Job> getJobs(int page, int size, boolean desc, String sortBy) {
		Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
		Sort sort = sortBy == null ? Sort.unsorted() : Sort.by(sortDirection, sortBy);
		return jobRepository.findAll(PageRequest.of(page - 1, size, sort));
	}

	/**
	 * Метод подсчёта затрат на перенос {@link Job} (в рублях)
	 * @param jobId идентификатор работы, которую нужно перенести
	 * @param days на сколько дней планируется перенести работу
	 * @return сколько будет стоить перенос (в рублях)
	 */
	@Transactional(readOnly = true)
	public double calculateCosts(long jobId, double days) {
		return calculateCosts(jobRepository.findById(jobId).get(), days);
	}

	// рекурсивный алгоритм вычисления стоимости переноса работы
	private double calculateCosts(Job job, double days) {
		if (days <= 0) {
			return 0;
		}
		double cost = job.isMile() ? costForMile : costForJob;
		Set<JobReference> references = job.getNextJobs();
		for (JobReference reference : references) {
			if (reference.getLag() < days) {
				cost += calculateCosts(reference.getJob(), days - reference.getLag());
			}
		}
		return cost;
	}
}
