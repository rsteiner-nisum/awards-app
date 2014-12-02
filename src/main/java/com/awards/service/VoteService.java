package com.awards.service;

import com.awards.domain.Nominee;
import com.awards.domain.Vote;
import com.awards.repository.NomineeRepository;
import com.awards.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;


@Service
public class VoteService {
    private final Logger log = LoggerFactory.getLogger(VoteService.class);

    @Inject
    private VoteRepository voteRepository;

    @Inject
    private NomineeRepository nomineeRepository;

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

    public List<Nominee> getResultsByCategory(String categoryId) {
        List<Nominee> nominees = nomineeRepository.findNomineesByCategoryId(categoryId);
        for(Nominee nominee : nominees){
            nominee.setTotalVotes(voteRepository.getVotesByCategoryAndNominee(categoryId, nominee.getId()));
        }
        return nominees;
    }
}
