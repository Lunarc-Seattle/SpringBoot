package com.pm.patientservice.grpc;

import billing.BillingServiceGrpc;
import org.springframework.stereotype.Service;
import billing.BillingRequest;
import billing.BillingResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;



@Service
//marks the class as a service component, making it eligible for Spring’s dependency injection and lifecycle management.

public class BillingServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(
            BillingServiceGrpcClient.class);
    //A Logger instance is created to log information,
    // which is useful for debugging and tracking activity in this class.
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;
    //BillingServiceGrpc.BillingServiceBlockingStub is a GRPC blocking stub. A stub is used to interact with the GRPC server.
    // "Blocking" means that each method call will wait until a response is received.

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort)
    //his constructor is used to initialize the client.
    // It receives the server address and port for the Billing Service via the Spring @Value annotation,
    // which allows configuration values to be injected from a properties file or environment variables.
    //If no values are provided for billing.service.address and billing.service.grpc.port, default values (localhost and 9001, respectively) are used.
    {

        log.info("Connecting to Billing Service GRPC service at {}:{}",
                serverAddress, serverPort);
        // This logs that the application is attempting to connect to the Billing Service GRPC service.

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,
                serverPort).usePlaintext().build();
        //ManagedChannelBuilder.forAddress(serverAddress, serverPort): This creates a channel to communicate with the server.
        // It specifies the address and port where the GRPC service is hosted.

//        .usePlaintext(): This disables encryption, which is typically used for local or development environments.
        //channel.build(): This builds the actual communication channel.
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);

        //blockingStub = BillingServiceGrpc.newBlockingStub(channel): This creates a blocking stub, which will be used to call the GRPC service synchronously (waiting for the response).
    }


    //This method is responsible for creating a billing account by sending a request to the GRPC service.
    // It takes the patientId, name, and email as input parameters.
    public BillingResponse createBillingAccount(String patientId, String name,
                                                String email) {

        BillingRequest request = BillingRequest.newBuilder().setPatientId(patientId)
                .setName(name).setEmail(email).build();
        //BillingRequest.newBuilder(): This creates a builder for the BillingRequest.
        //
        //.setPatientId(patientId), .setName(name), .setEmail(email): These methods set the values for the patient's ID, name, and email in the request.
        //
        //.build(): This creates the final BillingRequest object.

        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Received response from billing service via GRPC: {}", response);
        return response;
        //blockingStub.createBillingAccount(request): This sends the BillingRequest to the Billing Service via GRPC and waits for a BillingResponse.
        //
        //log.info(...): This logs the received response from the billing service for debugging purposes.
        //
        //return response: The method returns the BillingResponse received from the Billing Service.
    }
}
