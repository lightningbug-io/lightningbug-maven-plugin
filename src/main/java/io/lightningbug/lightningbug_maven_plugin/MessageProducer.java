/**
 * 
 */
package io.lightningbug.lightningbug_maven_plugin;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * @author UKROLMI
 *
 */
public class MessageProducer {
	private String kafkaServer;
	private String kafkaPort;
	private String topicName;

	public MessageProducer(String kafkaServer, String kafkaPort, String topicName) {
		if ((kafkaServer != null && !kafkaServer.isEmpty()) && (kafkaPort != null && !kafkaPort.isEmpty())
				&& (topicName != null && !topicName.isEmpty())) {
			this.kafkaServer = kafkaServer;
			this.kafkaPort = kafkaPort;
			this.topicName = topicName;
		} else {
			throw new IllegalArgumentException("Kafka params cannot be null or empty");
		}
	}

	public void sendMessage(String msg) {
		Properties producerProperties = new Properties();
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer + ":" + kafkaPort);
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProperties.put(ProducerConfig.ACKS_CONFIG, "all");

		Producer<Long, String> producer = new KafkaProducer<>(producerProperties);

		try {
			Long counter = 1L;
			Headers headers = new RecordHeaders();
			headers.add(new RecordHeader("header-1", "header-value-1".getBytes()));
			headers.add(new RecordHeader("header-2", "header-value-2".getBytes()));
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(topicName, null, counter, msg,
					headers);

			producer.send(record);
			counter++;
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}
	}
}
