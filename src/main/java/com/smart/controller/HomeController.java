package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Contact manager - Home");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "Contact manager - About");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("register", "Contact manager - Sign Up");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	
	@PostMapping(value = "/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
		@RequestParam(value="agreement", defaultValue="false") boolean agreement, Model model,
		HttpSession session) {
			
		try {
			if(bindingResult.hasErrors()) {
				return "signup";
			}
			
			model.addAttribute("user", new User());
			
			if(!agreement) {
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Not agreed to agreement", "alert-warning"));
				return "signup";
			}
			else {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				this.userRepository.save(user);
				session.setAttribute("message", new Message("Registered Succesfully", "alert-success"));
				return "signup";
			}
		} 
		catch (Exception e) {
			
			model.addAttribute("user", user);
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
	
	@GetMapping("/login")
	public String customLogin(Model model) {
		model.addAttribute("title", "Login Page");
		return "login";
	}
}
