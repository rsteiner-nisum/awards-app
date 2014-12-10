package com.awards.repositories;

import com.awards.Application;
import com.awards.domain.Vote;
import com.awards.repository.VoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import javax.inject.Inject;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class VoteRepositoryTest {

    private final String nomineeId001 = "nomineeId001";
    private final String nomineeId002 = "nomineeId002";
    private final Vote vote0 = new Vote("userId001", "catId001", nomineeId001);
    private final Vote vote1 = new Vote("userId002", "catId001", nomineeId001);
    private final Vote vote2 = new Vote("userId002", "catId001", nomineeId002);

    @Inject
    private VoteRepository voteRepository;

    @Before
    public void setup() {
    }

    public VoteRepositoryTest() {
        ReflectionTestUtils.setField(vote0, "id", "vote000");
        ReflectionTestUtils.setField(vote1, "id", "vote001");
        ReflectionTestUtils.setField(vote2, "id", "vote002");
    }

    /**
     * makes sure that two test votes are in the DB
     */
    public void insertSamples() {
        voteRepository.save(vote0);
        voteRepository.save(vote1);
        voteRepository.save(vote2);
    }

    @Test
    public void testCountVotes() {
        insertSamples();
        assert (voteRepository.countVotesByNominee(nomineeId001) == 2);
        assert (voteRepository.countVotesByNominee(nomineeId002) == 1);
    }

    @Test
    public void testListByNominee() {
        insertSamples();

        List<Vote> votes = voteRepository.getVotesByNominee(nomineeId001);
        assert (votes.size() == 2);
        assert (votes.contains(vote0));
        assert (votes.contains(vote1));
        assert (!votes.contains(vote2));
    }

    /**
     * deletes the votes for nominee 1
     */
    @Test
    public void deleteVotes() {
        insertSamples();

        long count = voteRepository.deleteVotesByNominee(nomineeId001);
        assert (count == 2);

        assert (voteRepository.countVotesByNominee(nomineeId001) == 0);
        assert (voteRepository.countVotesByNominee(nomineeId002) == 1);
    }
}
