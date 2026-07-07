package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaProducerExample {
    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String topic = "java-learning-topic";

        // Create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        try {
            // Create a producer record
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>(topic, "hello_key", "Hello from Java Code!");

            // Send data - asynchronous
            System.out.println("Sending message to Kafka topic: " + topic);
            Future<RecordMetadata> sendFuture = producer.send(producerRecord, (recordMetadata, e) -> {
                if (e == null) {
                    System.out.println("Successfully sent message!");
                    System.out.println("Topic: " + recordMetadata.topic());
                    System.out.println("Partition: " + recordMetadata.partition());
                    System.out.println("Offset: " + recordMetadata.offset());
                    System.out.println("Timestamp: " + recordMetadata.timestamp());
                } else {
                    System.err.println("Error while producing: " + e.getMessage());
                }
            });

            // Block to wait for send confirmation (optional, for demo)
            sendFuture.get();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Flush and close producer
            producer.flush();
            producer.close();
            System.out.println("Producer closed.");
        }
    }
}
