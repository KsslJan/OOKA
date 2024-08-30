package ooka.kessel.starterms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChannelInterceptor.class);
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        logger.info("WS message " + (sent?"successfully ": "failed to ") + "sent: " + message);
    }
}
