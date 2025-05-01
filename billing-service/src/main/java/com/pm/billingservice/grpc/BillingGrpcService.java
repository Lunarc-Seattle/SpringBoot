package com.pm.billingservice.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 3:53:54
@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest, StreamObserver<billing.BillingResponse> responseObserver) {
        log.info("Create billing account request received {}", billingRequest.toString());

        // business logic - like save too database perform calculate..

        BillingResponse response = BillingResponse.newBuilder().setAccountId("12345").setStatus("active").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        // we can return as many as requests we want
    }
}
