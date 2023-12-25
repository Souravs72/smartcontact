package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		model.addAttribute("title", "User Dashboard");
		model.addAttribute("user", user);
		return "admin/admin_dashboard";
	}
}
