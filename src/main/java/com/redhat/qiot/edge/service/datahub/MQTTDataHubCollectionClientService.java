/**
 * 
 */
package com.redhat.qiot.edge.service.datahub;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;


/**
 * It is not rare to combine in a single application imperative parts (Jax-RS,
 * regular CDI beans) and reactive parts (beans with @Incoming and @Outgoing
 * annotations). In these case, it’s often required to send messages from the
 * imperative part to the reactive part. In other words, send messages to
 * channels handled by reactive messaging and how can you retrieve messages.
 * 
 * @author Andrea
 *
 */
@ApplicationScoped
public class MQTTDataHubCollectionClientService {

    @Inject
    Logger LOGGER;

    @Inject
    @Channel("gas")
    Emitter<String> gasEmitter;
    @Inject
    @Channel("pollution")
    Emitter<String> pollutionEmitter;

    public void sendGas(String data) {
        gasEmitter.send(data);
    }

    public void sendPollution(String data) {
        pollutionEmitter.send(data);
    }
}
