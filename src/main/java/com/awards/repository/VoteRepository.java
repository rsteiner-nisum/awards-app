package com.awards.repository;


import com.awards.domain.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VoteRepository extends MongoRepository<Vote, String > {

    @Query( value = "{ 'nomineeId' : ?0 }", count = true)
    int countVotesByNominee(String nomineeId);

    @Query( value = "{ 'nomineeId' : ?0 }")
    List<Vote> getVotesByNominee(String nomineeId);

    @Query( value = "{ 'nomineeId' : ?0 }", delete = true)
    long deleteVotesByNominee(String nomineeId);

    // --- repo function relaying on category ID

    @Deprecated
    @Query( value = "{ $and: [ { 'categoryId' : ?0 }, { 'nomineeId' : ?1} ]}", count = true)
    int getVotesByCategoryAndNominee(String categoryId, String nomineeId);

    @Deprecated
    @Query("{ $and: [ { 'userId' : ?0 }, { 'categoryId' : ?1} ]}")
    Vote findExistingVote(String userId, String categoryId);

}
