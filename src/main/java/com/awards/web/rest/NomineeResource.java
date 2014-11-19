package com.awards.web.rest;


import com.awards.domain.Nominee;
import com.awards.repository.NomineeRepository;
import com.awards.security.AuthoritiesConstants;
import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.*;
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
public class NomineeResource {
    private final Logger log = LoggerFactory.getLogger(NomineeResource.class);

    @Inject
    private NomineeRepository nomineeRepository;

    @RequestMapping(value = "/rest/nominees/create",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> saveCategories(@ApiParam(name = "List of nominees", value = "List of nominees to be saved", required = true)
                                            @RequestBody(required = true) List<Nominee> nominees) {
        log.info("Saving category {}", nominees.toString());
        nomineeRepository.save(nominees);
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/nominees/search",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.USER)
    public ResponseEntity<List<Nominee>> getNomineesByCategory(@ApiParam(name="category-id", value="The Id of the category-id", required=true)
                                                   @RequestParam("category-id") String categoryId) {
        return new ResponseEntity<List<Nominee>>(nomineeRepository.findNomineesByCategoryId(categoryId), HttpStatus.OK);

    }

    @RequestMapping(value = "/rest/nominees/delete",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> deleteNominees(@RequestBody(required = true) List<Nominee> nominees){
        log.info("deleting the following nominees", nominees.toString());
        nomineeRepository.delete(nominees);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
