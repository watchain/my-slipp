package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.User;
import net.slipp.domain.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	
	//private List<User> users = new ArrayList<User>();
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session) {
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			System.out.println("Login Failed");
			return "redirect:/users/loginForm";
		}
		
		if (!password.equals(user.getPassword())) {
			System.out.println("Login Failed");
			return "redirect:/users/loginForm"; 
		}
		
		System.out.println("Login Success");
		session.setAttribute("sessionedUser", user);
		
		return "redirect:/";

	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("sessionedUser");
		return "redirect:/";
	}
	
	
	@PostMapping("")	
	//public String create(String userId, String password, String name, String email) {
	public String create(User user) {
		//System.out.println("userId: " + userId + ", Password : " + password + ", Name : " + name);
		System.out.println("user : " + user);
		//users.add(user);
		userRepository.save(user);
		
		//return "index";
		return "redirect:/users";
	}
	
	@GetMapping("/form")
	public String form() {
		return "/user/form";
	}

	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		Object tempUser = session.getAttribute("sessionedUser");
		if (tempUser == null) {
			return "redirect:/users/form";
		}
		User sessionedUser = (User)tempUser;
		if (!id.equals(sessionedUser.getId())) {
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
				
		User user = userRepository.findOne(sessionedUser.getId());
		model.addAttribute("user", user);
		return "/user/updateForm";
	}
	
	@GetMapping("")
	public String list(Model model) {
		//model.addAttribute("users", users);
		model.addAttribute("users", userRepository.findAll());
		return "/user/list";
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, User updateUser, HttpSession session) {
		Object tempUser = session.getAttribute("sessionedUser");
		if (tempUser == null) {
			return "redirect:/users/loginForm";
		}
		User sessionedUser = (User)tempUser;
		if (!id.equals(sessionedUser.getId())) {
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
			
		User user = userRepository.findOne(id);
		user.update(updateUser);
		userRepository.save(user);
		return "redirect:/users";
	}
	

}
