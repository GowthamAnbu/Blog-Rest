package com.gowtham.controller;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.exception.ServiceException;
import com.gowtham.model.Article;
import com.gowtham.model.Category;
import com.gowtham.model.CommentDetail;
import com.gowtham.model.User;
import com.gowtham.service.ArticleService;
import com.gowtham.service.CategoryService;
import com.gowtham.service.CommentDetailService;
import com.gowtham.service.UserService;
import com.gowtham.util.MailUtil;

@RestController
@RequestMapping("/showList")
public class ShowListController {

	@GetMapping("/{userId}")	
	public List<Article> showList(@PathVariable Integer userId) {
		ArticleService articleService = new ArticleService();
		User user =  new User();
		user.setId(userId);
		List<Article> articleList = null;
			try {
				articleList = articleService.viewAllArticle(user);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			return articleList;
	}

	@PutMapping("/update")
	public ResponseEntity<Object> update(@RequestBody Article article) {
		ArticleService articleService = new ArticleService();
		article.getId();
		article.getName();
		article.getContent();
		try {
			articleService.updateArticle(article);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Object>("Redirect to showList",HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/delete/{articleId}")
	public ResponseEntity<Object> delete(@PathVariable Integer articleId) {
		ArticleService articleService = new ArticleService();
		articleService.delete(articleId);
		return new ResponseEntity<Object>("Redirect to showList",HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/viewAll/{userId}")
	public ResponseEntity<Object> viewAll(@PathVariable Integer userId) {
		ArticleService articleService = new ArticleService();
		try {
			CategoryService categoryService = new CategoryService();
			articleService.viewAllArticle();
			categoryService.getCategory(userId);
		} catch (ServiceException e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("Redirect to viewALl.jsp",HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Redirect to viewAll.jsp",HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/viewByCategory")
	public ResponseEntity<Object> viewByCategory(@RequestBody Category category) {
			CategoryService categoryService = new CategoryService();
			categoryService.getCategory(category.getUser().getId());
			categoryService.listByCategory(category.getName());
		return new ResponseEntity<Object>("Redirect to viewAll.jsp",HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/change/{userId}/{roleId}")
	public ResponseEntity<Object> change(@PathVariable Integer userId,@PathVariable Integer roleId){
		UserService userService = new UserService();
		userService.change(userId,roleId);
		userService.forAdmin();
		return new ResponseEntity<Object>("Redirect to admin.jsp",HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/comment/{userName}")
	public ResponseEntity<Object> comment(@PathVariable String userName,@RequestBody CommentDetail commentDetail){
		final CommentDetailService commentDetailService = new CommentDetailService();
		final UserService userService = new UserService();
		final User author= userService.getUser(userName);
		final User user=userService.getUser(commentDetail.getUser().getUserName());
		try {
			commentDetailService.save(commentDetail);
			try {
				MailUtil.sendSimpleMail(user,author);
			} catch (EmailException e) {
				return new ResponseEntity<Object>("EMAIL_ERROR",HttpStatus.OK);
			}
		} catch (ServiceException e) {
			return new ResponseEntity<Object>("COMMENT-SERVICE_ERROR",HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Redirect to showList/viewAll",HttpStatus.ACCEPTED);
	}
	
	
	@GetMapping("/viewComments/{articleId}")
	public List<CommentDetail> viewComments(@PathVariable Integer articleId){
		final CommentDetailService commentDetailService = new CommentDetailService();
		return commentDetailService.getComments(articleId);
		/*return"../viewComments.jsp";*/
	}
}
