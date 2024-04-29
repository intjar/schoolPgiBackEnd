//package com.np.schoolpgi;
//
//import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.np.schoolpgi.model.Roles;
//
//public class RoleMasterServiceControllerTest extends SchoolPgiApplicationTests{
//	@Test
//	public void getRolesList() throws Exception {
//	   String uri = "/np/app/viewRole";
//	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
//	      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
//	   
//	   int status = mvcResult.getResponse().getStatus();
//	   assertEquals(true, status);
//	   String content = mvcResult.getResponse().getContentAsString();
//	   Roles[] productlist = super.mapFromJson(content, Roles[].class);
//	   assertTrue(productlist.length > 0);
//	}
//}
