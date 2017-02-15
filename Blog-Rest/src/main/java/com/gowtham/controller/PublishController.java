package com.gowtham.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.exception.ServiceException;
import com.gowtham.model.Article;
import com.gowtham.model.Category;
import com.gowtham.model.CategoryDetail;
import com.gowtham.model.User;
import com.gowtham.service.ArticleService;
import com.gowtham.service.CategoryDetailService;
import com.gowtham.service.CategoryService;

@RestController
@RequestMapping("/publish")
public class PublishController {

	@PostMapping
	public ResponseEntity<Object> publish(@RequestBody CategoryDetail categoryDetail) {
		final CategoryDetailService categoryDetailService = new CategoryDetailService();
		final ArticleService articleService = new ArticleService();
		final CategoryService categoryService = new CategoryService();
		final Article article = new Article();
		final User user = new User();
		final Category category = new Category();
		user.setId(categoryDetail.getArticle().getUser().getId());
		article.setName(categoryDetail.getArticle().getName());
		article.setContent(categoryDetail.getArticle().getContent());
		article.setUser(user);
		category.setName(categoryDetail.getCategory().getName());
		category.setUser(user);
		try {
			articleService.publishSave(article);
			categoryService.addCategory(category);
			categoryDetailService.save(categoryDetail);
		} catch (ServiceException e) {
			return  new ResponseEntity<Object>( e.getMessage(),HttpStatus.OK);
		}
		return new ResponseEntity<Object>("redirect to show list",HttpStatus.ACCEPTED);
	}
}
