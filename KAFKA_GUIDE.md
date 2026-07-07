# Apache Kafka Java Integration Guide

Yes, you can use the Kafka broker we just set up in **any Java project**! The broker is running locally on your system and is listening on **`localhost:9092`**.

This guide explains how to configure your Java application to communicate with Kafka, detailing:
1. How to add the Kafka dependency to your project.
2. Complete Java code examples for a **Producer** and a **Consumer**.

---

## 1. Adding the Kafka Dependency

To use Kafka in Java, you need the official `kafka-clients` library. Depending on how your project is configured, choose one of the options below:

### Option A: Plain IntelliJ IDEA Project (Your Current Setup)
Since your project is currently a plain Java project (without Maven/Gradle files), you can add the library directly through IntelliJ:
1. Open your project in IntelliJ IDEA.
2. Go to **File** -> **Project Structure...** (or press `Cmd + ;`).
3. Select **Libraries** under the *Project Settings* sidebar on the left.
4. Click the **`+`** (plus) icon at the top of the libraries pane and choose **From Maven...**.
5. Search for `org.apache.kafka:kafka-clients:3.7.0` (or the latest stable version) and click **OK**.
6. Check the box to download it to your project, then click **Apply** and **OK**.

### Option B: Maven Project
If you decide to convert this project to Maven in the future, add the following to your `pom.xml`:
```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.7.0</version>
</dependency>
```

### Option C: Gradle Project
For a Gradle build, add this line to the `dependencies` block in your `build.gradle`:
```groovy
implementation 'org.apache.kafka:kafka-clients:3.7.0'
```

---

## 2. Java Code Examples

We have created complete code templates under `src/kafka/` for you:

### 1. Producer Example
File: [KafkaProducerExample.java](file:///Users/mohit/Code/JAVA_LEARINING/src/kafka/KafkaProducerExample.java)
This class sends test records containing string data to Kafka.

### 2. Consumer Example
File: [KafkaConsumerExample.java](file:///Users/mohit/Code/JAVA_LEARINING/src/kafka/KafkaConsumerExample.java)
This class subscribes to a topic, polls for new messages, and prints them to the console.

---

## 3. How to Run the Examples

### Step 1: Start Kafka
If Kafka is not already running, start it using the helper script we created:
```bash
/Users/mohit/.gemini/antigravity/scratch/kafka-setup/start.sh
```

### Step 2: Create the Topic
Create the topic that the Java examples use (`java-learning-topic`):
```bash
/Users/mohit/.gemini/antigravity/scratch/kafka-setup/create-topic.sh java-learning-topic
```

### Step 3: Run the Java Files in IntelliJ
1. **Start the Consumer**: Run the main method in [KafkaConsumerExample.java](file:///Users/mohit/Code/JAVA_LEARINING/src/kafka/KafkaConsumerExample.java) first. It will stay active, waiting for incoming messages.
2. **Start the Producer**: Run the main method in [KafkaProducerExample.java](file:///Users/mohit/Code/JAVA_LEARINING/src/kafka/KafkaProducerExample.java). It will send a message and then exit.
3. Observe the console of `KafkaConsumerExample` — you should see the message received in real-time!
