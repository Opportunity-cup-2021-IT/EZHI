package ru.hedgehog.multid.predictiveanalytics.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Anikushin Roman
 * Класс является представлением связи двух работ в базе данных
 */
@Entity
@Table(name = "jobs_reference")
@IdClass(JobReferenceId.class)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class JobReference {

	@Id
	@ManyToOne
	@JoinColumn(name = "depend_id", referencedColumnName = "id")
	private Job depend;

	@Id
	@ManyToOne
	@JoinColumn(name = "job_id", referencedColumnName = "id")
	private Job job;

	@Column
	private double lag;

	public JobReference(Job depend, Job job, double lag) {
		this.depend = depend;
		this.job = job;
		this.lag = lag;
	}
}
