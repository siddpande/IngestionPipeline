package mapperclasses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ingestion.EventConsumer;

@Component
public class EventMapper {

	private final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
	ObjectMapper objectMapper = new ObjectMapper();

	public BaseEvent mapEvent(String event) {
		BaseEvent ev = null;
		try {
			ev = objectMapper.readValue(event, BaseEvent.class);
		} catch (Exception e) {
			logger.error("Enable to parse received event: " + event + "Exception" + e);
			// TO DO
			// We can also publish these to an error queue

		}
		return ev;
	}

}
