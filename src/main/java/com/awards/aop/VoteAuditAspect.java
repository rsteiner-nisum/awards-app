package com.awards.aop;


import com.awards.domain.PersistentAuditEvent;
import com.awards.domain.Vote;
import com.awards.repository.PersistenceAuditEventRepository;
import com.awards.repository.VoteRepository;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.LocalDateTime;

import javax.inject.Inject;
import java.util.Map;

@Aspect
public class VoteAuditAspect {
    @Inject
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    @Inject
    private VoteRepository voteRepository;

    @After("execution(* com.awards.repository.VoteRepository.save(..))")
    public void audit(JoinPoint joinPoint){
        Vote vote = (Vote) joinPoint.getArgs()[0];
        Map<String, String> payload = Maps.newHashMap();
        payload.put(vote.getNomineeId(), vote.toString());

        PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
        persistentAuditEvent.setId(voteRepository.count() + 1L);
        persistentAuditEvent.setAuditEventDate(new LocalDateTime());
        persistentAuditEvent.setAuditEventType(String.format("Vote for nominee %s in category %s"
            , vote.getNomineeId(), vote.getCategoryId()));
        persistentAuditEvent.setData(payload);
        persistentAuditEvent.setPrincipal(vote.getUserId());

        persistenceAuditEventRepository.save(persistentAuditEvent);
    }
}
