package com.cyber.spring.JsonCloud.controller.admin;


import com.cyber.spring.JsonCloud.JsonCloudApplication;
import com.cyber.spring.JsonCloud.config.SecurityConfiguration;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { JsonCloudApplication.class, SecurityConfiguration.class})
@WebAppConfiguration
@TestPropertySource("/test.properties")
public class UserAccountControllerTest {

    private String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private Principal admin;


    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        admin = Mockito.mock(Principal.class);
        Mockito.when(admin.getName()).thenReturn("admin");
    }

    @Test
    public void listUsers() throws Exception{
        String url = "/admin/user/";

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get( url ).principal( admin );

        mockMvc.perform( req )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$[0].login", is("admin") ) );
    }

    @Test
    public void addUser1() throws Exception{
        String url = "/admin/user/";

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post( url )
                .principal( admin )
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"login\":\"user1\", \"password\":\"111\"}");

        MvcResult res = mockMvc.perform( req )
                .andDo(print())
                .andExpect( status().isOk() )
                .andExpect( content().contentType(APPLICATION_JSON_UTF8) )
                .andExpect( jsonPath("$.id", greaterThan(0) ) )
                .andReturn();
    }

    @Test
    public void getUser2() throws Exception{
        String url = "/admin/user/";

        // add user2
        MockHttpServletRequestBuilder req1 = MockMvcRequestBuilders.post( url )
                .principal( admin )
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"login\":\"user2\", \"password\":\"222\"}");

        MvcResult res1 = mockMvc.perform( req1 )
                .andDo(print())
                .andExpect( status().isOk() )
                .andExpect( content().contentType(APPLICATION_JSON_UTF8) )
                .andExpect( jsonPath("$.id", greaterThan(0) ) )
                .andReturn();

        Long userId = Long.valueOf( JsonPath.parse(res1.getResponse().getContentAsString()).read("$.id").toString() );

        // get user2
        MockHttpServletRequestBuilder req2 = MockMvcRequestBuilders.get( url + "/" + userId )
                .principal( admin );

        MvcResult res = mockMvc.perform( req2 )
                .andDo(print())
                .andExpect( status().isOk() )
                .andExpect( content().contentType(APPLICATION_JSON_UTF8) )
                .andExpect( jsonPath("$.login", is("user2") ) )
                .andExpect( jsonPath("$.id", is(userId.intValue()) ) )
                .andReturn();
    }


    @Test
    public void delUser3() throws Exception{
        String url = "/admin/user/";

        // Add user3
        MockHttpServletRequestBuilder req1 = MockMvcRequestBuilders.post( url )
                .principal( admin )
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"login\":\"user3\", \"password\":\"333\"}");

        MvcResult res1 = mockMvc.perform( req1 ).andReturn();

        Long userId = Long.valueOf( JsonPath.parse(res1.getResponse().getContentAsString()).read("$.id").toString() );

        // del user3
        MockHttpServletRequestBuilder req2 = MockMvcRequestBuilders.delete( url  + "/" + userId )
                .principal( admin );

        MvcResult res2 = mockMvc.perform( req2 )
                .andDo(print())
                .andExpect( status().isOk() )
                .andReturn();
    }

    @Test
    public void setUser4() throws Exception{
        String url = "/admin/user/";

        // add user2
        MockHttpServletRequestBuilder req1 = MockMvcRequestBuilders.post( url )
                .principal( admin )
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"login\":\"user4\", \"password\":\"444\"}");

        MvcResult res1 = mockMvc.perform( req1 )
                .andDo(print())
                .andExpect( status().isOk() )
                .andExpect( content().contentType(APPLICATION_JSON_UTF8) )
                .andExpect( jsonPath("$.id", greaterThan(0) ) )
                .andReturn();

        Long userId = Long.valueOf( JsonPath.parse(res1.getResponse().getContentAsString()).read("$.id").toString() );

        // set user4
        MockHttpServletRequestBuilder req2 = MockMvcRequestBuilders.post( url + "/" + userId )
                .principal( admin )
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"login\":\"user4\", \"fullname\":\"FullNameData\"}");

        MvcResult res2 = mockMvc.perform( req2 )
                .andDo(print())
                .andExpect( status().isOk() )
                .andExpect( content().contentType(APPLICATION_JSON_UTF8) )
                .andExpect( jsonPath("$.id", is(userId.intValue()) ) )
                .andReturn();
    }

}
