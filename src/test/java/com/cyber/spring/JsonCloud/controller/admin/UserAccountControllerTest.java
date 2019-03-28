package com.cyber.spring.JsonCloud.controller.admin;


import com.cyber.spring.JsonCloud.JsonCloudApplication;
import com.cyber.spring.JsonCloud.repository.RoleRepository;
import com.cyber.spring.JsonCloud.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
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
@SpringBootTest(classes = JsonCloudApplication.class)
@TestPropertySource("/test.properties")
public class UserAccountControllerTest {

    private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
    private static final String URL_PATH = "/admin/user/";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private Principal admin;

    public Long createUser(String name) throws Exception{
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post( URL_PATH )
                .principal( admin )
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"login\":\"" + name + "\", \"password\":\"111\"}");

        MvcResult res = mockMvc.perform( req )
                .andExpect( status().isOk() )
                .andReturn();

        Long userId = Long.valueOf( JsonPath.parse(res.getResponse().getContentAsString()).read("$.id").toString() );
        return userId;
    }


    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        admin = Mockito.mock(Principal.class);
        Mockito.when(admin.getName()).thenReturn("admin");
    }

    @Test
    public void listUsers() throws Exception{

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get( URL_PATH ).principal( admin );

        mockMvc.perform( req )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$[0].login", is("admin") ) );
    }

    @Test
    public void addUser1() throws Exception{

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post( URL_PATH )
                .principal( admin )
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"login\":\"newuser1\", \"password\":\"111\"}");

        MvcResult res = mockMvc.perform( req )
                .andDo(print())
                .andExpect( status().isOk() )
                .andExpect( content().contentType(APPLICATION_JSON_UTF8) )
                .andExpect( jsonPath("$.id", greaterThan(0) ) )
                .andReturn();
    }

    @Test
    public void getUser2() throws Exception{
        Long userId = createUser("user2");

        // get user2
        MockHttpServletRequestBuilder req2 = MockMvcRequestBuilders.get( URL_PATH + "/" + userId )
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
        Long userId = createUser("user3");

        // del user3
        MockHttpServletRequestBuilder req2 = MockMvcRequestBuilders.delete( URL_PATH  + "/" + userId )
                .principal( admin );

        MvcResult res2 = mockMvc.perform( req2 )
                .andDo(print())
                .andExpect( status().isOk() )
                .andReturn();
    }

    @Test
    public void setUser4() throws Exception{
        Long userId = createUser("user4");

        // set user4
        MockHttpServletRequestBuilder req2 = MockMvcRequestBuilders.post( URL_PATH + "/" + userId )
                .principal( admin )
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"login\":\"user4edited\", \"fullname\":\"FullNameData\",\"password\":\"222\",\"status\":1,\"roles\":[\"ROLE_USER\",\"ROLE_ADMIN\"]}");

        MvcResult res2 = mockMvc.perform( req2 )
                .andDo(print())
                .andExpect( status().isOk() )
                .andExpect( content().contentType(APPLICATION_JSON_UTF8) )
                .andExpect( jsonPath("$.id", is(userId.intValue()) ) )
                .andReturn();
    }

}
