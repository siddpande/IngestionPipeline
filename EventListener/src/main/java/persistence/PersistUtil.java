package persistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import mapperclasses.BaseEvent;
import mapperclasses.SourceType;

@Component
public class PersistUtil {

	public static final String db_path = "/Users/kopal/Desktop/Ingestion/Database";
	private final Logger logger = LoggerFactory.getLogger(PersistUtil.class);

	public static final String car_filename = "car.txt";
	public static final String fridge_filename = "fridge.txt";
	public static final String clock_filename = "clock.txt";

	ObjectMapper objectMapper = new ObjectMapper();

	// (sourceType, timestamp), identity, eventID, payload/ payloadID()in case of object storage
	// File system/ Oject Storage

	public void persistEvent(BaseEvent event) {

		// Writing to file to simulate database

		try {

			String sourceType = event.getSourcetype();
			String filename = null;

			if (sourceType.equalsIgnoreCase(SourceType.CAR.toString())) {
				filename = car_filename;
			} else if (sourceType.equalsIgnoreCase(SourceType.FRIDGE.toString())) {
				filename = fridge_filename;
			} else if (sourceType.equalsIgnoreCase(SourceType.CLOCK.toString())) {
				filename = clock_filename;
			}

			String path = db_path + "/" + filename;

			FileWriter fw = new FileWriter(path, true);
			BufferedWriter br = new BufferedWriter(fw);
			PrintWriter pr = new PrintWriter(br);
			pr.println(objectMapper.writeValueAsString(event));

			pr.close();
			br.close();
			fw.close();

		}

		catch (Exception e) {
			// TO DO: Gracefull handling + Error Poller at system level
			logger.error("Error while persisting event to DB" + e);
		}

	}
}
