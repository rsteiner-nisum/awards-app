package com.awards.web.rest;

import com.awards.domain.Nominee;
import com.awards.domain.Vote;
import com.awards.security.AuthoritiesConstants;
import com.awards.service.VoteService;
import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/app")
public class VoteResource {
    private final Logger log = LoggerFactory.getLogger(VoteResource.class);

    @Inject
    private VoteService voteService;

    @RequestMapping(value = "/rest/vote",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.USER)
    public ResponseEntity<String> castVote(@ApiParam(name = "Vote", value = "Vote to be casted", required = true)
                                            @RequestBody(required = true) Vote vote) {
        log.info("Casting one vote for user {}, category {}, nominee {}", vote.getUserId()
                ,vote.getCategoryId()
                , vote.getNomineeId());
        voteService.castVote(vote);
        return new ResponseEntity<String>("Vote casted", HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/vote/results",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<Nominee>> getResultsByCategory(@ApiParam(name="category-id", value="category-id", required=true)
                                                               @RequestParam("category-id") String categoryId) {
        return new ResponseEntity<List<Nominee>>( voteService.getResultsByCategory(categoryId), HttpStatus.OK);
    }




}
