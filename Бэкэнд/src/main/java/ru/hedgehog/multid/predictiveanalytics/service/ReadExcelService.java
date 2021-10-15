package ru.hedgehog.multid.predictiveanalytics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hedgehog.multid.predictiveanalytics.Relationship;
import ru.hedgehog.multid.predictiveanalytics.Utils;
import ru.hedgehog.multid.predictiveanalytics.domain.Job;
import ru.hedgehog.multid.predictiveanalytics.domain.JobReference;
import ru.hedgehog.multid.predictiveanalytics.repository.JobRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Anikushin Roman
 */
@Service
@RequiredArgsConstructor
@Log
public class ReadExcelService {

	private final JobRepository jobRepository;

	@PostConstruct
	private void onConstruct() {
		convert("OC2021_IT_Data_ASE.xlsx");
	}

	// конвертация данных из excel в базу данных
	@Transactional
	public void convert(String resource) {
		Map<Long, Job> map = new HashMap<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(resource)) {
			readJobs(in, map);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		jobRepository.saveAll(map.values());
		log.info("Converted jobs!");
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(resource)) {
			readReference(in, map);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		jobRepository.saveAll(map.values());
		log.info("Converted reference!");
	}

	public void readJobs(InputStream input, Map<Long, Job> jobs) throws IOException {
		Workbook workbook = WorkbookFactory.create(input);
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> iterator;

		iterator = sheet.rowIterator();
		iterator.next();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			Job job = parseJob(row);
			jobs.put(job.getId(), job);
		}
	}

	public void readReference(InputStream input, Map<Long, Job> jobs) throws IOException {
		Workbook workbook = WorkbookFactory.create(input);
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> iterator;

		iterator = sheet.rowIterator();
		iterator.next();
		int i = 0;
		while (iterator.hasNext()) {
			Row row = iterator.next();
			Long id = Long.parseLong(row.getCell(0).getStringCellValue());
			Job job = jobs.get(id);

			if (row.getCell(4) != null) {
				for (String next : row.getCell(4).getStringCellValue().split(Pattern.quote(";"))) {
					if (next.endsWith(".")) {
						continue;
					}
					Relationship rs = parseRelationship(next);
					Job nextJob = jobs.get(rs.getId());
					if (nextJob == null) {
						//jobs.put(rs.getId(), nextJob = new Job(rs.getId()));
						continue;
					}
					job.getNextJobs().add(new JobReference(job, nextJob, rs.getLag()));
				}
				i++;
			}

			if (row.getCell(5) != null) {
				for (String previous : row.getCell(5).getStringCellValue().split(Pattern.quote(";"))) {
					if (previous.endsWith(".")) {
						continue;
					}
					Relationship rs = parseRelationship(previous);
					Job previousJob = jobs.get(rs.getId());
					if (previousJob == null) {
						//jobs.put(rs.getId(), previousJob = new Job(rs.getId()));
						continue;
					}
					previousJob.getNextJobs().add(new JobReference(previousJob, job, rs.getLag()));
				}
			}
		}
	}

	private Job parseJob(Row row) {
		String dur = "0д";
		if (row.getCell(2) != null) {
			dur = row.getCell(2).getStringCellValue();
		}
		if (dur.contains(",")) {
			dur = dur.replace(",", ".");
		}
		return new Job(
				Long.parseLong(row.getCell(0).getStringCellValue()),
				row.getCell(1) == null ? null : Utils.parseDate(row.getCell(1).getStringCellValue()),
				row.getCell(3) == null ? null : Utils.parseDate(row.getCell(3).getStringCellValue())
				//row.getCell(2) == null ? 0 : Double.parseDouble(dur.substring(0, dur.length() - 1))
		);
	}

	private Relationship parseRelationship(String string) {
		if (string.endsWith("д")) {
			String[] arr;
			int m;
			if (string.contains("+")) {
				arr = string.split(Pattern.quote("+"));
				m = 1;
			} else {
				arr = string.split(Pattern.quote("-"));
				m = -1;
			}
			return new Relationship(
					Integer.parseInt(arr[0].substring(0, arr[0].length() - 2)),
					arr[0].substring(arr[0].length() - 2),
					m * Integer.parseInt(arr[1].substring(0, arr[1].length() - 1))
			);
		}
		else if (string.endsWith("Н") || string.endsWith("О")) {
			return new Relationship(
					Integer.parseInt(string.substring(0, string.length() - 2)),
					string.substring(string.length() - 2),
					0
			);
		}
		else {
			return new Relationship(
					Integer.parseInt(string),
					"ОН",
					0
			);
		}
	}
}
