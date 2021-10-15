package ru.hedgehog.multid.predictiveanalytics.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Anikushin Roman
 */
@Data
public class JobReferenceId implements Serializable {

	private long depend;

	private long job;

}
