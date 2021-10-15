package ru.hedgehog.multid.predictiveanalytics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.hedgehog.multid.predictiveanalytics.domain.Job;
import ru.hedgehog.multid.predictiveanalytics.repository.JobRepository;
import ru.hedgehog.multid.predictiveanalytics.service.JobService;

import java.util.Collection;

/**
 * @author Anikushin Roman
 * REST Контроллер для {@link Job}
 */
@RestController
@RequestMapping("job")
@RequiredArgsConstructor
public class JobController {

	private final JobService service;

	/**
	 * GET метод получаения страниц, состоящих из {@link Job}
	 * @param page номер страницы (от 1)
	 * @param size количество элеметов на странице
	 * @param desc порядок сортировки
	 * @param sort критерий сортировки
	 * @return страница из работ
	 */
	@GetMapping("list")
	public Page<Job> list(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "desc", defaultValue = "0") boolean desc,
			@RequestParam(value = "sort", required = false) String sort
			) {
		return service.getJobs(page, size, desc, sort);
	}

	/**
	 * GET метод подсчёта затрат на перенос работы
	 * @param id идентификатор {@link Job}
	 * @param days на сколько дней нужно перенести работу
	 * @return сколько рублей будет стоить перенос
	 */
	@GetMapping("{id}/calc")
	public double calculate(
			@PathVariable("id") long id,
			@RequestParam("days") double days
	) {
		return service.calculateCosts(id, days);
	}
}