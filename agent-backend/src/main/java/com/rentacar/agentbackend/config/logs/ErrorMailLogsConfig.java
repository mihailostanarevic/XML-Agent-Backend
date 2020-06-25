package com.rentacar.agentbackend.config.logs;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.spi.FilterReply;
import com.rentacar.agentbackend.config.EmailContext;
import com.rentacar.agentbackend.service.IAuthService;
import com.rentacar.agentbackend.service.impl.AuthService;
import com.rentacar.agentbackend.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.List;

public class ErrorMailLogsConfig extends ch.qos.logback.core.filter.AbstractMatcherFilter{

    @Autowired
    private EmailService _emailService;

    @Override
    public FilterReply decide(Object event)
    {
        if (!isStarted())
        {
            return FilterReply.NEUTRAL;
        }

        LoggingEvent loggingEvent = (LoggingEvent) event;
        List<Level> eventsToKeep = Arrays.asList(Level.ERROR);
        if (eventsToKeep.contains(loggingEvent.getLevel()))
        {
            return FilterReply.NEUTRAL;
        }
        else
        {
            return FilterReply.DENY;
        }
    }
}
