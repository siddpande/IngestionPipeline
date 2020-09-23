package ingestion;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import mapperclasses.BaseEvent;
import mapperclasses.EventMapper;
import persistence.PersistUtil;

@Service
public class EventConsumer {
	
	@Autowired
	EventProducer eventProducer;
	
	@Autowired
	EventMapper eventMapper;
	
	@Autowired
	PersistUtil persistUtil; 

    private final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = "EventSink", groupId = "eventConsumer")
    public void consume(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        
        BaseEvent event=eventMapper.mapEvent(message);
        
        //Push raw data to DB
        persistUtil.persistEvent(event);
        
        //Produce to outbound analytics topic
        eventProducer.sendMessage(message);
    }
}
