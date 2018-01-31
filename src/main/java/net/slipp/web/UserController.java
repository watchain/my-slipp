package net.slipp.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import net.slipp.domain.User;
import net.slipp.domain.UserRepository;

@Controller
public class UserController {
	
	//private List<User> users = new ArrayList<User>();
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/create")	
	//public String create(String userId, String password, String name, String email) {
	public String create(User user) {
		//System.out.println("userid: " + userId + ", Password : " + password + ", Name : " + name);
		System.out.println("user : " + user);
		//users.add(user);
		userRepository.save(user);
		
		//return "index";
		return "redirect:/list";
	}
	
	@GetMapping("/list")
	public String list(Model model) {
		//model.addAttribute("users", users);
		model.addAttribute("users", userRepository.findAll());
		return "list";
	}

}
