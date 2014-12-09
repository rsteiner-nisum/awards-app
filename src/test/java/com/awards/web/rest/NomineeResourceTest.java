package com.awards.web.rest;

import com.awards.Application;
import com.awards.domain.Nominee;
import com.awards.repository.NomineeRepository;
import com.awards.security.AuthoritiesConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class NomineeResourceTest {

    private final Nominee nominee1 = new Nominee("myself", "i'm the best", null, null);
    @Mock
    private NomineeRepository nomineeRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc restUserMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NomineeResource nomineeResource = new NomineeResource();
        ReflectionTestUtils.setField(nomineeResource, "nomineeRepository", nomineeRepository);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(nomineeResource).build();

        ReflectionTestUtils.setField(nominee1, "id", "nomIDxx01");
        nominee1.setCategoryId("catID00001");
    }

    @Test
    @WithMockUser(roles = AuthoritiesConstants.USER)
    public void testSearch() throws Exception {
        when(nomineeRepository.findNomineesByCategoryId(anyString())).thenReturn(new LinkedList<Nominee>());

        ResultActions result = restUserMockMvc.perform(MockMvcRequestBuilders
                        .get("/app/rest/nominees/search?category-id={id}", "547f2bad7b2a7d172a2af770")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @WithMockUser(roles = AuthoritiesConstants.USER)
    public void testSearchWithResults() throws Exception {
        LinkedList<Nominee> lst = new LinkedList<Nominee>();
        lst.add(nominee1);
        when(nomineeRepository.findNomineesByCategoryId(anyString())).thenReturn(lst);

        ResultActions result = restUserMockMvc.perform(MockMvcRequestBuilders
                        .get("/app/rest/nominees/search?category-id={id}", nominee1.getCategoryId())
                        .accept(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(nominee1.getId())))
                .andExpect(jsonPath("$[0].name", is(nominee1.getName())))
                .andExpect(jsonPath("$[0].description", is(nominee1.getDescription())))
                .andExpect(jsonPath("$[0].categoryId", is(nominee1.getCategoryId())));

    }

    @Test
    @WithMockUser(roles = AuthoritiesConstants.ADMIN)
    public void testDeleteNonExisting() throws Exception {
        when(nomineeRepository.findOne(anyString())).thenReturn(null);
        ResultActions result = restUserMockMvc.perform(MockMvcRequestBuilders.delete("/app/rest/nominees/{nomineeId}", "9988")
                .accept("application/json"));
        result.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = AuthoritiesConstants.ADMIN)
    public void testDeleteExisting() throws Exception {
        when(nomineeRepository.findOne(anyString())).thenReturn(new Nominee());
        restUserMockMvc.perform(
                MockMvcRequestBuilders.delete("/app/rest/nominees/{nomineeId}", "9988")
                        .accept("application/json"))
                .andExpect(status().isNoContent());

    }
}
