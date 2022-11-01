package com.thecodeveal.app.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecodeveal.app.model.Policies;
import com.thecodeveal.app.model.User;
import com.thecodeveal.app.repo.UserDetailsRepository;

@Service
public class PoliciesService {

	@Autowired
	UserDetailsRepository userDetailsRepository;
	
//	public User savePolicies(Policies res) {
//		return userDetailsRepository.save(res);
//	}
//	
//	public List<Policies> getPolicies(){
//		return userDetailsRepository.findAll();
//	}
	
	public User getPolicies(String username) {
		return userDetailsRepository.findByUsername(username);
	}
	
//	@Transactional
//	public List<Policies> deletePolicies(String username) {
//		return userDetailsRepository.deleteByUsername(username);
//	}
	
	public String updatePolicies(String username, User policies) {
		User existingPolicies = userDetailsRepository.findByUsername(username);
		if(existingPolicies==null){
            return "Data Not Found";
        }
		System.out.println(policies);
		if(policies.isHrPolicy())existingPolicies.setHrPolicy(policies.isHrPolicy());
		if(policies.isItPolicy())existingPolicies.setItPolicy(policies.isItPolicy());
		if(policies.isNdaPolicy())existingPolicies.setNdaPolicy(policies.isNdaPolicy());
		
		userDetailsRepository.save(existingPolicies);
		return "Policies updated successfully";
	}
}