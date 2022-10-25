package com.thecodeveal.app.controller;

import java.util.*;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thecodeveal.app.model.Authority;
import com.thecodeveal.app.model.User;
import com.thecodeveal.app.repo.AuthorityDetailsRepository;
import com.thecodeveal.app.repo.UserDetailsRepository;
import com.thecodeveal.app.service.AuthorityService;
import com.thecodeveal.app.service.CustomUserService;

@RestController
@RequestMapping("/")
@CrossOrigin
public class AppController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Autowired
	CustomUserService customUserService;

	@Autowired
	AuthorityService authorityService;
	
	
	
	
	@PostMapping("/saveDocuments")
	public void addResume(@RequestBody String u) throws JSONException
	{
		
		JSONObject jsonObject= new JSONObject(u);
		
		String resumeurl=(String) jsonObject.get("resume");
		
		String marksheetsurl=(String) jsonObject.get("marksheets");
		
		User  user = userDetailsRepository.findByUsername((String)jsonObject.get("username"));

		
		user.setResume(resumeurl);
		
		user.setMarksheets(marksheetsurl);
		
		userDetailsRepository.save(user);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/changepassword")
	public void changePassword(@RequestBody String u) throws JSONException,Exception
	{
		
		JSONObject jsonObject= new JSONObject(u);
		
		String username = (String) jsonObject.get("username");
		String oldpassword = (String) jsonObject.get("oldpassword");
		String newpassword = (String) jsonObject.get("newpassword");
		
			
			User user=userDetailsRepository.findByUsername(username);
			
			if(user==null)
			{
				throw new Exception("invalid email");
			}
		
		
	      String password =user.getPassword();
	      
	       boolean result = (passwordEncoder.matches(oldpassword,password));
		
	      	if(!result)
	      	{
	      		throw new Exception("invalid email");
	      	}
	      	
	      	
	      	user.setPassword(passwordEncoder.encode(newpassword));
	      	
	      	userDetailsRepository.save(user);
	      	
	      	System.out.println(result);
		
			System.out.println(username+" "+oldpassword+" "+newpassword);
	}
	
	
	@PostMapping("/addCandidate/{email}/{password}")
	public boolean addCandidate(@PathVariable("email") String email ,  @PathVariable("password") String password)
	{
		 
			
			  List<Authority>authorityList=new ArrayList<>();
				
				authorityList.addAll(authorityService.findAuthority((long)1));
				
				
				if((userDetailsRepository.findByUsername(email))!=null)
				{
					return false;
				}
				
				User user =new User();
				user.setUsername(email);				
				user.setPassword(passwordEncoder.encode(password));				
				user.setAuthorites(authorityList);				
				user.setProfilepic("https://northmemorial.com/wp-content/uploads/2016/10/PersonPlaceholder.png");				
				userDetailsRepository.save(user);
				return true;
			  
		
	}
	
	
	
	
	
	
	
	
	@PostMapping("/generatemail/{email}/{virtusaemail}/{password}")
	public boolean insertMail(@PathVariable("email") String email ,
			@PathVariable("virtusaemail") String virtusaemail , @PathVariable("password") String password ) throws Exception
	{
		
		
		
		if((userDetailsRepository.findByUsername(email)==null)||userDetailsRepository.findByUsername(email).getEmailGeneration())
		{
			System.out.println("invalid");
			throw new Exception("invalid email");
		}
		
		
		
		
		List<Authority>authorityList=new ArrayList<>();
		
		authorityList.addAll(authorityService.findAuthority((long)2));
		
		User user =new User();
		
		
		
		user.setUsername(virtusaemail);
		
		user.setPassword(passwordEncoder.encode(password));
		
		user.setAuthorites(authorityList);
		
		user.setEmailGeneration(true);
		
		user.setProfilepic("https://northmemorial.com/wp-content/uploads/2016/10/PersonPlaceholder.png");
		
		
		
		//user.setMailGeneration(true);
		
		userDetailsRepository.save(user);
		
		user=userDetailsRepository.findByUsername(email);
		
		user.setEmailGeneration(true);
		
		customUserService.updateDetails(user);
		
		System.out.println(user.getUsername()+" "+user.getPassword());
		
		return true;
		
	}
	
	
	
	@GetMapping("/userdetails/{username}")
	@ResponseBody
	public String getDetails(@PathVariable("username") String email) throws JsonProcessingException{
	  ObjectMapper mapper = new ObjectMapper();
	  User u = customUserService.getDetails(email);
	  return mapper.writeValueAsString(u);
	}
	
	
	
	
	@PostMapping("/details/{username}")
	public boolean updateDetails(@RequestBody User user , @PathVariable("username") String email )
	{
		
		
		System.out.println(user.getAddressone()+" "+user.getAddresstwo()+" "+user.getDateofbirth());
		
		
		User u = userDetailsRepository.findByUsername(email);
		
		u.setFirstname(user.getFirstname());
		u.setMiddlename(user.getMiddlename());
		u.setLastname(user.getLastname());
		u.setFathername(user.getFathername());
		u.setMothername(user.getMothername());
		u.setMobilenumber(user.getMobilenumber());
		u.setAlternatemobilenumber(user.getAlternatemobilenumber());
		u.setFathermobilenumber(user.getFathermobilenumber());
		u.setMothermobilenumber(user.getMothermobilenumber());
		u.setPincode(user.getPincode());
		u.setDateofbirth(user.getDateofbirth());
		u.setPersonalemail(user.getPersonalemail());
     	u.setAddressone(user.getAddressone());
		u.setAddresstwo(user.getAddresstwo());
		u.setProfilepic(user.getProfilepic());
		
		customUserService.updateDetails(u);
		
		
		return true;
	}
	
	
	
	
}
