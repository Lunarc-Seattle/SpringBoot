package com.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;
//InvalidProtocolBufferException：当你解码Protobuf数据失败时会抛出这个异常。
//
//）。Logger：用来打印日志（比如log.info()和log.error()）。
//
//KafkaListener：这是Spring的注解，表示这个方法要监听Kafka消息。
//
//PatientEvent：这是用Protobuf定义的Java类，代表顾客的事件数据。

@Service
public class KafkaConsumer {
    //@Service: Spring 标记，这样这个类会自动被 Spring 管理（可以自动注入）。
    //
    //类名是KafkaConsumer，意思是它专门负责接收Kafka消息。

    private static final Logger log = LoggerFactory.getLogger(
            KafkaConsumer.class);
    // 创建一个日志对象，方便在代码里打印日志。

    @KafkaListener(topics="patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        // 见： KafkaProducer.java里的try catch  就是这么传进来的
        // transfer into event

        //@KafkaListener：
        //
        //topics="patient"→ 这个方法监听名为patientKafka 主题。
        //
        //groupId = "analytics-service"→ 这是消费者组ID，表示该消费者属于** analytics-service**组。
        //
        //方法consumeEvent()：
        //
        //参数是一个byte[] event，馄饨从Kafka接收到二进制字节负载（因为Protobuf消息就是字节流）。
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            //在呢这里可以加 其他的business logic
            //这里用PatientEvent.parseFrom(event)把字节存储转回一个收到的PatientEvent对象。
            //
            //Protobuf的数据传输就是用字节流，所以必须先解析才能用。
            //把接收的字节吞吐量event（来自Kafka的一段二进制数据）
            //解析（decode）成一个PatientEvent对象。
            //这意味着：event只是一堆字节乱码）（看起来像乱码）。
            //parseFrom()是Protobuf提供的方法，把这堆字节还原成我们定义的`成我们定义好的PatientEvent这个Java对象。
            //
            //还原之后，你就可以像正常的Java对象那样使用它了，比如：patientEvent.getPatientId()，patientEvent.getName()
            //patientEvent.getEmail()

            //perform any business related to analytics here
            //👉这个意思：
            //在这里，你可以编写与“分析业务”相关的逻辑。
            //现在它只是一个注释，作者提醒你：
            //“这里可以放一些实际做的事情，比如保存数据、计算统计、调用其他服务等等。”
            //举个例子：
            //你以后可以在这里写：
            //analyticsService.recordPatientVisit(patientEvent.getPatientId());
            //或者：
            //statisticsService.updatePatientStats(patientEvent);
            //现在还没写，是留空的，但可以扩展。

            //总结：它先把二进制消息变回客户的接收对象（PatientEvent），
            //然后可以在这里做任何你想要的分析业务，比如记录、统计、发送数据。

            log.info("Received Patient Event: [PatientId={},PatientName={},PatientEmail={}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail());

            //一旦解析成功，这里会打印出患者的ID、姓名和邮箱。
            //
            //这些值是从patientEvent对象里拿出来的。

        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}", e.getMessage());
            //如果Protobuf解析失败（比如字节数据损坏了），会捕获异常。
            //
            //并打印错误日志。
        }
    }
}