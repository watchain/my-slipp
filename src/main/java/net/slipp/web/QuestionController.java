package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.Question;
import net.slipp.domain.QuestionRepository;
import net.slipp.domain.Result;
import net.slipp.domain.User;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("/form")
	public String form(HttpSession session) {
		if (!HttpSessionUnits.isLoginUser(session)) {
			return "/users/loginForm";
		}
		
		return "/qna/form";
	}
	
	@PostMapping("")
	public String create(String title, String contents, HttpSession session) {
		if (!HttpSessionUnits.isLoginUser(session)) {
			return "/users/loginForm";
		}
		User sessionUser = HttpSessionUnits.getUserFromSession(session);
		Question newQuestion = new Question(sessionUser, title, contents);
		questionRepository.save(newQuestion);
		
		return "redirect:/";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable Long id, Model model) {
		Question question = questionRepository.findOne(id); 
		model.addAttribute("question", question);
		//model.addAttribute("question", questionRepository.findOne(id));
		return "/qna/show";
	}
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		Question question = questionRepository.findOne(id);
		Result result = valid(session, question);
		if (!result.isValid()) {
			model.addAttribute("errorMessage", result.getErrorMessage());
			return "/user/login";
		}
		model.addAttribute("question", question);
		return "/qna/updateForm";	
		
		/*
		if (!HttpSessionUnits.isLoginUser(session)) {
			return "/users/loginForm";
		}

		User loginUser = HttpSessionUnits.getUserFromSession(session);
		System.out.println(loginUser);
		Question question = questionRepository.findOne(id);
		
		System.out.println(question.isSameWriter(loginUser));
		
		if (!question.isSameWriter(loginUser)) {
			return "/users/loginForm";
		}
		//Question question = questionRepository.findOne(id);
		model.addAttribute("question", question);
		return "/qna/updateForm";*/
	}
	
	private Result valid(HttpSession session, Question question) {
		
		if (!HttpSessionUnits.isLoginUser(session)) {
			return Result.fail("Need to login");
		}
			
		User loginUser = HttpSessionUnits.getUserFromSession(session);
		if (!question.isSameWriter(loginUser)) {
			return Result.fail("You can modify or delete by your article.");
		}		
		return Result.ok();
	}
	
	/*private boolean hasPermission(HttpSession session, Question question) {
		if (!HttpSessionUnits.isLoginUser(session)) {
			throw new IllegalStateException("Need to login");
		}
		
		User loginUser = HttpSessionUnits.getUserFromSession(session);
		if (question.isSameWriter(loginUser)) {
			throw new IllegalStateException("You can modify or delete by your article.");
		}
		
		return true;
	}*/
	
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, String title, String contents, Model model, HttpSession session) {
		Question question = questionRepository.findOne(id);
		Result result = valid(session, question);
		if (!result.isValid()) {
			model.addAttribute("errorMessage", result.getErrorMessage());
			return "/user/login";
		}
		question.update(title, contents);
		questionRepository.save(question);
		return String.format("redirect:/questions/%d", id);
		
				
		
/*		if (!HttpSessionUnits.isLoginUser(session)) {
			return "/users/loginForm";
		}

		User loginUser = HttpSessionUnits.getUserFromSession(session);
		Question question = questionRepository.findOne(id);
		if (!question.isSameWriter(loginUser)) {
			return "/users/LoginForm";
		}*/
		
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, Model model, HttpSession session) {
		Question question = questionRepository.findOne(id);
		Result result = valid(session, question);

		if (!result.isValid()) {
			model.addAttribute("errorMessage", result.getErrorMessage());
			return "/user/login";
		}
		
		questionRepository.delete(id);
		return "redirect:/";
				
/*		if (!HttpSessionUnits.isLoginUser(session)) {
			return "/users/loginForm";
		}

		User loginUser = HttpSessionUnits.getUserFromSession(session);
		Question question = questionRepository.findOne(id);
		if (!question.isSameWriter(loginUser)) {
			return "/users/LoginForm";
		}*/

	}

}
