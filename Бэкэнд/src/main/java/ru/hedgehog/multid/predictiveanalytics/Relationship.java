package ru.hedgehog.multid.predictiveanalytics;

import lombok.Data;

import java.util.regex.Pattern;

/**
 * @author Anikushin Roman
 */
@Data
public class Relationship {

	private final long id;

	private final String type;

	private final int lag;

}
