package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.configuration.FileService;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username = principal.getName(); // gives the username
		
		User user = this.userRepository.getUserByUserName(username);
		model.addAttribute("user", user);
	}
	
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		
		model.addAttribute("title", user.getName());
		return "normal/user_dashboard";
	}
	
	//add form handler
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult,
				@RequestParam("myImage") MultipartFile myImage,
				HttpSession session, Principal principal) {
		
		try {
			
			if(bindingResult.hasErrors()) {
				return "normal/add_contact_form";
			}
			
			String userName = principal.getName();
			User user = this.userRepository.getUserByUserName(userName);
			contact.setUser(user);
			user.getContacts().add(contact);
			
			if(myImage.isEmpty()) {
				
				this.userRepository.save(user);
				session.setAttribute("message", new Message("Your contact is added without image. Add more...", "alert-warning"));
			}
			else {
				// add the file to the folder and update the name to the contact
//				contact.setcImageUrl(myImage.getOriginalFilename() + contact.getcPhone());
				
				String fileName = this.fileService.uploadImage(path, myImage);
				contact.setcImageUrl(fileName);				
				
				this.userRepository.save(user);				
				session.setAttribute("message", new Message("Your contact is added. Add more...", "alert-success"));
			}
			
			return "normal/add_contact_form";
		}
		catch (Exception e) {
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "normal/add_contact_form";
		}
		
	}
	
	/* Show contacts
	 * Per page = 5 contacts
	 * current page = 0;
	 */
	
	@GetMapping("/show-contacts/{page}")
	public String viewContactForm(@PathVariable("page") Integer page, Model model, Principal principal) {
		
		model.addAttribute("title", "Show Contacts");
		
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		Pageable pageable = PageRequest.of(page, 8);
		
		Page<Contact> contact =  this.contactRepository.findContactsPerPageByUser(user.getId(), pageable);
		if(page > contact.getTotalPages()) {
			return "normal/wrong_user";
		} else {
			model.addAttribute("contacts", contact);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", contact.getTotalPages());
			
			return "normal/show_contacts";
		}
	}
	
	
//	show particular contact
	@GetMapping("/{cId}/contact")
	public String showContactDetails(@PathVariable("cId") Integer cId, Model model, Principal principal) {
	
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		if(contactOptional.isEmpty()) {
			return "normal/wrong_user";
		}
		Contact contact = contactOptional.get();
		
		String username = principal.getName();
		User user =	this.userRepository.getUserByUserName(username);
		
		if(user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getcName());
		}
		else {
			return "normal/wrong_user";
		}
		return "normal/contact_detail";
	}
	
	@GetMapping("/delete/{cId}")
	public String deleteUser(@PathVariable("cId") Integer cId, Principal principal, HttpSession session) throws IOException { 
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		if(contactOptional.isEmpty()) {
			return "normal/wrong_user";
		}
		Contact contact = contactOptional.get();
		String username = principal.getName();
		User user =	this.userRepository.getUserByUserName(username);
		
		//perform a check
		if(user.getId() == contact.getUser().getId()) {
			//Also delete contact image if contact is to be deleted
			if(contact.getcImageUrl() != null) {
				//delete old photo
				
				File deletefile = new ClassPathResource("static/images").getFile();
				File file = new File(deletefile, contact.getcImageUrl());
				file.delete();
			}
			contact.setUser(null);
			this.contactRepository.delete(contact);
			session.setAttribute("message", new Message("Contact has been deleted successfully", "alert-danger"));
		}
		else {
			return "normal/wrong_user";
		}
		
		return "redirect:/user/show-contacts/0";
	}
	//update contact handler
	@PostMapping("/update/{cId}") 
	public String updateHandler(@PathVariable("cId") Integer cId, Model model) {
		model.addAttribute("title", "Update Contact");
		
		if(!this.contactRepository.findById(cId).isEmpty()) {
			model.addAttribute("contact", this.contactRepository.findById(cId).get());
		}
		return "normal/update_form";
	}
	
	//process update of contact
	@PostMapping("/process-update")
	public String processUpdateHandler(@Valid @ModelAttribute Contact contact, BindingResult bindingResult,
		 @RequestParam("cId") Integer cId, Principal principal,
		 @RequestParam("myImage") MultipartFile myImage,
		 Model model, HttpSession session) {
		
		try {
				Contact oldContact = this.contactRepository.findById(contact.getcId()).get();
			
				User user = this.userRepository.getUserByUserName(principal.getName());
				contact.setUser(user);
				if(bindingResult.hasErrors()) {
					return "normal/update_form";
				}
				
				if(myImage.isEmpty()) {
					contact.setcImageUrl(oldContact.getcImageUrl());
				}
				else {
					//delete old photo
					
					File deletefile = new ClassPathResource("static/images").getFile();
					File file = new File(deletefile, oldContact.getcImageUrl());
					file.delete();
					
					//update new photo
					  
					File saveImage = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(saveImage.getAbsolutePath()+File.separator+myImage.getOriginalFilename());
					Files.copy(myImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					contact.setcImageUrl(myImage.getOriginalFilename());
				}
				
				this.contactRepository.save(contact);
				session.setAttribute("message", new Message("Your contact is added.", "alert-success"));
		}catch(Exception e) {
			session.setAttribute("message", new Message("Some error occurred", "alert-danger"));
		}	
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	@GetMapping("/profile") 
	public String profileView(Model model) {
		model.addAttribute("title","Your profile");
		return "normal/profile";
	}
}
