package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.slipp.domain.Answer;
import net.slipp.domain.AnswerRepository;
import net.slipp.domain.Question;
import net.slipp.domain.QuestionRepository;
import net.slipp.domain.Result;
import net.slipp.domain.User;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class APIAnswerContoller {
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@PostMapping("")
	public Answer create(@PathVariable Long questionId, String contents, HttpSession session) {
		if (!HttpSessionUnits.isLoginUser(session)) {
			return null;
		}
		
		User loginUser = HttpSessionUnits.getUserFromSession(session);
		Question question = questionRepository.findOne(questionId);
		Answer answer = new Answer(loginUser,question, contents);
		
		question.addAnswer();
		return answerRepository.save(answer);
 
	}
	
	@DeleteMapping("/{id}")
	public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
		if (!HttpSessionUnits.isLoginUser(session)) {
			return Result.fail("Need to login");
		}
		
		Answer answer = answerRepository.findOne(id);
		User loginUser = HttpSessionUnits.getUserFromSession(session);
		if (!answer.isSameWriter(loginUser)) {
			return Result.fail("you can delete only your articles.");
		}
		
		answerRepository.delete(id);
		Question question = questionRepository.findOne(questionId);
		question.deleteAnswer(); 
		questionRepository.save(question);
		return Result.ok();
	}
	

/*	public String create(@PathVariable Long questionId, String contents, HttpSession session) {
		if (!HttpSessionUnits.isLoginUser(session)) {
			return "/users/loginForm";
		}
		
		User loginUser = HttpSessionUnits.getUserFromSession(session);
		Question question = questionRepository.findOne(questionId);
		Answer answer = new Answer(loginUser,question, contents);
		answerRepository.save(answer);
		return String.format("redirect:/questions/%d", questionId);
 
	}
*/
	
}
