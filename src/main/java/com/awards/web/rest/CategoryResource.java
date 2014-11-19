package com.awards.web.rest;

import com.awards.domain.Category;
import com.awards.repository.CategoryRepository;
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
public class CategoryResource {
    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    @Inject
    private CategoryRepository categoryRepository;

    @RequestMapping(value = "/rest/categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<Category>> getCategories() {
        return new ResponseEntity<List<Category>>(categoryRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/category/delete/{category-id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> deleteCategory(@ApiParam(name="category-id", value="The Id of the product to be deleted", required=true)
                                            @PathVariable("category-id") String categoryId) {
        log.info("deleting category with id: {}", categoryId);
        categoryRepository.delete(categoryId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/category/create",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> saveCategories(@ApiParam(name = "List of categories", value = "List of categories to be saved", required = true)
                                                @RequestBody(required = true) List<Category> categories) {
        log.info("Saving category {}", categories.toString());
        categoryRepository.save(categories);
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/category/update",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> updateCategories(@ApiParam(name = "List of categories", value = "List of categories to be updated", required = true)
                                              @RequestBody(required = true) List<Category> categories) {
        log.info("Updating category {}", categories.toString());
        categoryRepository.save(categories);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/category/{category-id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Category> getCategoryById(@ApiParam(name="category-id", value="The Id of the product to be retrieved", required=true)
                                             @PathVariable("category-id") String categoryId) {
        return new ResponseEntity<Category>(categoryRepository.findOne(categoryId), HttpStatus.OK);
    }
}
