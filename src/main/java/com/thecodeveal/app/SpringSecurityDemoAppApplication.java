package com.thecodeveal.app;



import org.jboss.jandex.VoidType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thecodeveal.app.model.Authority;
import com.thecodeveal.app.model.Department;
import com.thecodeveal.app.model.User;
import com.thecodeveal.app.repo.AuthorityDetailsRepository;
import com.thecodeveal.app.repo.DepartmentRepository;
import com.thecodeveal.app.repo.UserDetailsRepository;
import com.thecodeveal.app.service.AuthorityService;

import java.util.*;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringSecurityDemoAppApplication {

	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthorityDetailsRepository authorityDetailsRepository;
	
	@Autowired
	AuthorityService authorityService;
	
	
	@Autowired
	DepartmentRepository departmentRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoAppApplication.class, args);
	}
	
	@PostConstruct
	protected void init()
	{

		
		
		if(userDetailsRepository.findByUsername("saicharanpoleboina2@gmail.com")==null)
		{
			
			System.out.println("ok");
			
			Authority authority =new Authority((long)2, "USER");
			
			authorityService.saveAuthority(authority);
			
			Authority authority2 =new Authority((long)3, "EMP");
			
			authorityService.saveAuthority(authority2);
			
			
			Authority authority3 =new Authority((long)5, "ADMIN");
			
			authorityService.saveAuthority(authority3);
			
			
			List<Authority>authorityList=new ArrayList<>();
			
			authorityList.addAll(authorityService.findAuthority((long) 1));
			
			User user =new User();
		
			user.setUsername("saicharanpoleboina2@gmail.com");
			
			user.setPassword(passwordEncoder.encode("CHara!4545"));
			
			user.setAuthorites(authorityList);
			
			user.setProfilepic("https://northmemorial.com/wp-content/uploads/2016/10/PersonPlaceholder.png");
			
			
			//user.setDepartment(departmentRepository.findBydeptCode("IT"));
			
			
			userDetailsRepository.save(user);
			
			System.out.println(user.getUsername() +" "+user.getPassword());
			
			
			
			
			Department department = new Department("INTERN", "intern");
			
			departmentRepository.save(department);
			
			department = new Department("IT", "information Technology");
			
			departmentRepository.save(department);
			
			department = new Department("HR", "Human Resources");
			
			departmentRepository.save(department);
			
			
			department = new Department("SAL", "Sales");
			
			departmentRepository.save(department);
			
			
			
			department = new Department("MRKT", "Marketing");
			
			departmentRepository.save(department);
			
			department = new Department("MNGMT", "Management");
			
			departmentRepository.save(department);
			
			department = new Department("ADMIN", "admin");
			
			departmentRepository.save(department);
			
			
			
			
		}
		
		
		
		
		if(userDetailsRepository.findByUsername("admin@virtusa.com")==null)
		{
			List<Authority>authorityList=new ArrayList<>();
			
			authorityList.addAll(authorityService.findAuthority((long) 3));
			
			User user =new User();
		
			user.setUsername("admin@virtusa.com");
			
			user.setPassword(passwordEncoder.encode("SPrin!3972"));
			
			user.setAuthorites(authorityList);
			
			user.setProfilepic("https://northmemorial.com/wp-content/uploads/2016/10/PersonPlaceholder.png");
			
			
			user.setDepartment(departmentRepository.findBydeptCode("ADMIN"));
			
			
					userDetailsRepository.save(user);
			
				System.out.println(user.getUsername() +" "+user.getPassword());
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

    private Authority createAuthority(String roleCode,String roleDescription)
    {
    	Authority authority=new Authority();
    	authority.setRoleCode(roleCode);

    	return authority;
    	
    	
    }
	
	
	
	
	
}
