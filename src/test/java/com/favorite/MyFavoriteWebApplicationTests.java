package com.favorite;

import com.favorite.domain.User;
import com.favorite.domain.result.ResponseData;
import com.favorite.web.UserController;
import com.sun.deploy.net.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyFavoriteWebApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void contextLoads() throws Exception {
		String url = "/user/login";
		User user = new User();
		user.setUserName("4444");
		user.setPassWord("1qaz2wsx");
		Object  result = testRestTemplate.postForObject(url,user, ResponseData.class);
		System.out.println("========" + result);
	}

}
