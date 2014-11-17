package com.awards.service;

import com.awards.domain.Vote;
import com.awards.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VoteService {
    private final Logger log = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    private VoteRepository voteRepository;

    public void castVote(Vote vote){
        Vote existingVote = voteRepository.findExistingVote(vote.getUserId(), vote.getCategoryId());
        if(existingVote == null){
            log.info("Casting a new vote {}", vote);
            voteRepository.save(vote);
        }else{
            log.info("Updating vote {}, new vote is {}", existingVote, vote);
            voteRepository.delete(existingVote);
            voteRepository.save(vote);
        }
    }
}
