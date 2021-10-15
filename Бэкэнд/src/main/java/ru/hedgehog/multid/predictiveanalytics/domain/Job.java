package ru.hedgehog.multid.predictiveanalytics.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Anikushin Roman
 * Класс является представлением работы в базе данных
 */
@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
public class Job {

	/**
	 * Идентификатор работы
	 */
	@Id
	private long id;

	/**
	 * Дата начала работы
	 */
	@Column
	private Date start;

	/**
	 * Дата окончания работы
	 */
	@Column
	private Date end;

	/**
	 * Список работ-последователей
	 */
	@OneToMany(mappedBy = "depend", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<JobReference> nextJobs = new HashSet<>();

	/**
	 * Список работ-предшественников
	 */
	@OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<JobReference> previousJobs = new HashSet<>();

	/**
	 * Конструктор работы
	 * @param id идентификатор работы
	 * @param start дата начала работы
	 * @param end дата окончания работы
	 */
	public Job(Long id, Date start, Date end) {
		this.id = id;
		this.start = start;
		this.end = end;
	}

	/**
	 * Конструктор работы
	 * @param id идентификатор работы
	 */
	public Job(Long id) {
		this(id, null, null);
	}

	/**
	 * @return true, если работа является вехой
	 */
	public boolean isMile() {
		return start.equals(end);
	}
}
