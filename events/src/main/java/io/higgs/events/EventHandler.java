package io.higgs.events;

import io.higgs.core.InvokableMethod;
import io.higgs.core.reflect.dependency.DependencyProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class EventHandler extends SimpleChannelInboundHandler<EventMessage> {
    private final Queue<InvokableMethod> methods;
    private Logger log = LoggerFactory.getLogger(getClass());

    public EventHandler(Queue<InvokableMethod> methods) {
        this.methods = methods;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, EventMessage msg) throws Exception {
        int matches = 0;
        for (InvokableMethod method : methods) {
            if (method.matches(msg.name(), ctx, msg)) {
                Object response = method.invoke(ctx, msg.name(), msg, msg.params(), DependencyProvider.from());
                if (response instanceof TypeMismatch) {
                    continue;
                }
                matches++;
            }
        }
        if (matches == 0) {
            log.debug(String.format("Event received but no subscribers found (%s)", msg));
        }
    }
}
