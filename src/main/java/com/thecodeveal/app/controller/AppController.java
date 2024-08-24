package com.thecodeveal.app.controller;

import java.util.*;

import javax.swing.text.Document;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties.Decryption;
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
import com.thecodeveal.app.model.Department;
import com.thecodeveal.app.model.User;
import com.thecodeveal.app.repo.AuthorityDetailsRepository;
import com.thecodeveal.app.repo.UserDetailsRepository;
import com.thecodeveal.app.service.AuthorityService;
import com.thecodeveal.app.service.CustomUserService;
import com.thecodeveal.app.service.DepartmentService;

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
	
	@Autowired
	DepartmentService departmentService;
	
	
	
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
	
	@PostMapping("/saveServiceBond")
	public void addServiceBond(@RequestBody String u) throws JSONException
	{
		
		System.out.println("fdfdkf");
		JSONObject jsonObject= new JSONObject(u);
		
		String servicebondurl=(String) jsonObject.get("filename");
		
		User  user = userDetailsRepository.findByUsername((String)jsonObject.get("username"));

		user.setServicebond(servicebondurl);
		
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
	
	
	@PostMapping("/addCandidate")
	public boolean addCandidate(@RequestBody String u) throws JSONException
	{
		JSONObject jsonObject= new JSONObject(u);
		
			String email = (String) jsonObject.get("cemail");
			String password = (String)  jsonObject.get("cpassword");
			String salary = (String) jsonObject.get("csal");
			String department = (String) jsonObject.get("cdept");
			String name = (String) jsonObject.get("cname");
			String location = (String) jsonObject.get("cloc");
			String role = (String) jsonObject.get("crole");
		
		
		 
			
			  List<Authority>authorityList=new ArrayList<>();
				
				authorityList.addAll(authorityService.findAuthority((long)1));
				
				
				if((userDetailsRepository.findByUsername(email))!=null)
				{
					return false;
				}
				
				User user =new User();
				
				Department dep = departmentService.findDepartment(department);
				
				System.out.println(dep.getDepartment());
				
				user.setDepartment(dep);
				user.setRole(role);
				
				user.setUsername(email);			
				user.setFirstname(name);
				user.setLocation(location);
				user.setPassword(passwordEncoder.encode(password));				
				user.setAuthorites(authorityList);				
				user.setProfilepic("https://northmemorial.com/wp-content/uploads/2016/10/PersonPlaceholder.png");	
				user.setSalary(Long.parseLong(salary));
				userDetailsRepository.save(user);
				return true;
			  
		
	}
	
	
	@GetMapping("/bgv/{username}")
	public boolean bgvVerified(@PathVariable("username") String email)
	{
		User user=userDetailsRepository.findByUsername(email);
		
		
		if(user==null)
		{
			return false;
		}
		
		user.setBgv(true);
		
		userDetailsRepository.save(user);
		
		
		return true;
		
	}
	
	
	@GetMapping("/removeCandidate/{username}")
	public String removeCandidate(@PathVariable("username") String email)
	{
			User user =userDetailsRepository.findByUsername(email);
			
			if(user==null)
			{
				return "invalid";
			}
			
			userDetailsRepository.delete(user);
			
			
			return "success";
			
			
		
	}
	
	
	
	
	
	@PostMapping("/generatemail/{email}/{password}")
	public String insertMail(@PathVariable("email") String email ,
			 @PathVariable("password") String password ) throws Exception
	{
		
		
	//	System.out.println((int)Math.floor(Math.random()*10));
		
		if((userDetailsRepository.findByUsername(email)==null)||userDetailsRepository.findByUsername(email).getEmailGeneration())
		{
			System.out.println("invalid");
			throw new Exception("invalid email");
		}
		
		
		User oldUser=userDetailsRepository.findByUsername(email);
		
		System.out.println(oldUser.isBgv());
		
		if(!oldUser.isBgv())
		{
			return "invalid";
		}
		
		System.out.println(" "+email+" "+password);
		
		String virtusaemail = oldUser.getFirstname();
		
		if(oldUser.getMiddlename()!=null)
		{
			virtusaemail+=oldUser.getMiddlename();
		}
		
		if(oldUser.getLastname()!=null)
		{
			virtusaemail+=oldUser.getLastname();
		}
		
		
		
		while(userDetailsRepository.findByUsername(virtusaemail+"@virtusa.com")!=null)
		{
			
			for(int i=0;i<2;i++)
			{
				
				virtusaemail=virtusaemail+(int)Math.floor(Math.random()*10);
			}
			
		}
		
		
		virtusaemail=virtusaemail+"@virtusa.com";
		
		
		System.out.println(oldUser);
		
		List<Authority>authorityList=new ArrayList<>();
		
		authorityList.addAll(authorityService.findAuthority((long)2));
		
		User user =new User();
		
		user.setAddressone(oldUser.getAddressone());
		
		user.setAddresstwo(oldUser.getAddresstwo());
		
		user.setAlternatemobilenumber(oldUser.getAlternatemobilenumber());
		
		// user.setCourses(oldUser.getCourses());
		
		user.setDateofbirth(oldUser.getDateofbirth());
		
		user.setDepartment(oldUser.getDepartment());
		
		user.setMobilenumber(oldUser.getMobilenumber());
		
		user.setFathername(oldUser.getFathername());
		
		
		user.setMothername(oldUser.getMothername());
		
		user.setFathermobilenumber(oldUser.getFathermobilenumber());
		
		user.setMothermobilenumber(oldUser.getMothermobilenumber());
		
		
		user.setPincode(oldUser.getPincode());
		
		
		user.setPersonalemail(oldUser.getUsername());
		
		user.setSalary(oldUser.getSalary());
		
		user.setRole(oldUser.getRole());
		
		user.setResume(oldUser.getResume());
		
		user.setMarksheets(oldUser.getMarksheets());
		
		
		user.setUsername(virtusaemail);
		
		user.setPassword(passwordEncoder.encode(password));
		
		user.setAuthorites(authorityList);
		
		user.setEmailGeneration(true);
		
	    user.setProfilepic(oldUser.getProfilepic());
	    
	    
	    user.setFirstname(oldUser.getFirstname());
	    
	    user.setMiddlename(oldUser.getMiddlename());
	    
	    user.setServicebond(oldUser.getServicebond());
	    
	    user.setLastname(oldUser.getLastname());
	    
	    user.setLocation(oldUser.getLocation());
		
		
		System.out.println(user);
		
		
		//user.setMailGeneration(true);
		
		userDetailsRepository.save(user);
		
		user=userDetailsRepository.findByUsername(email);
		
		user.setEmailGeneration(true);
		
		customUserService.updateDetails(user);
		
		System.out.println(user.getUsername()+" "+user.getPassword());
		
		return virtusaemail;
		
	}
	
	@PostMapping("/offerstatus/{username}")
	public boolean offerstatus(@PathVariable("username") String email)
	{
		
		System.out.println("hello world");

		User u = userDetailsRepository.findByUsername(email);
		
		if(u==null)
		{
			return false;
		}

		u.setOfferAcceptance(true);
		
		userDetailsRepository.save(u);
		
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
