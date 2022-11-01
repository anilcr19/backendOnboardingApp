package com.thecodeveal.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.thecodeveal.app.model.Policies;
import com.thecodeveal.app.model.User;
import com.thecodeveal.app.service.PoliciesService;


@RestController
@CrossOrigin(origins = "*")
public class PoliciesController {

	@Autowired
	PoliciesService policiesService;
	
//	 	@PostMapping("/policies")
//	    public Policies savePolicies(@RequestBody Policies res){
//	        System.out.println("RES"+res);
//	        return policiesService.savePolicies(res);
//	    }
//		@GetMapping("/policies")
//		public List<Policies> getBankDetail() {
//			return policiesService.getPolicies();
//		}
	    @GetMapping("/policies/{username}")
	    public User getBankDetails(@PathVariable("username") String username){
	        return policiesService.getPolicies(username);
	    }
//	    @DeleteMapping("/policies/{username}")
//	    public List<Policies> delResById(@PathVariable("username") String username){
//	        return policiesService.deletePolicies(username);
//	    }
	   @PutMapping("/policies/{username}")
	    public String updateRes(@PathVariable("username") String username,@RequestBody User resupdate){
	        return policiesService.updatePolicies(username,resupdate);
	    }
}