///**
// * 
// */
//package com.redhat.qiot.edge.service.datahub;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;
//
//import org.eclipse.microprofile.reactive.messaging.Incoming;
//import org.eclipse.microprofile.reactive.messaging.Outgoing;
//import org.slf4j.Logger;
//
//import com.redhat.qiot.edge.util.decorator.MeasurementDecorator;
//
///**
// * It is not rare to combine in a single application imperative parts (Jax-RS,
// * regular CDI beans) and reactive parts (beans with @Incoming and @Outgoing
// * annotations). In these case, it’s often required to send messages from the
// * imperative part to the reactive part. In other words, send messages to
// * channels handled by reactive messaging and how can you retrieve messages.
// * 
// * @author Andrea
// *
// */
//@ApplicationScoped
//public class IntegrationService {
//
//    @Inject
//    Logger LOGGER;
//
//    @Inject
//    MeasurementDecorator measurementDecorator;
//
//
//    @Incoming("sensor-gas")
//    @Outgoing("datahub-gas")
//    public String sendGas(String data) {
//        LOGGER.info("Sending out GAS measurement");
//        return measurementDecorator.decorate(data);
//    }
//
//    @Incoming("sensor-pollution")
//    @Outgoing("datahub-pollution")
//    public String sendPollution(String data) {
//        LOGGER.info("Sending out PARTICULATES measurement");
//        return measurementDecorator.decorate(data);
//    }
//}
