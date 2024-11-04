package net.codejava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomeController {
	@GetMapping("index/draft")
	public String index() {
		return "home/index_draft";
	}
	
	@GetMapping("about")
	public String about() {
		return "home/about";
	}
	
	@GetMapping("contact")
	public String contact() {
		return "home/contact";
	}
	
	@GetMapping("industries")
	public String industries() {
		return "home/industries";
	}
	
	@GetMapping("blog")
	public String blog() {
		return "home/blog/blog";
	}
	
	@GetMapping("blog-1")
	public String blog1() {
		return "home/blog/blog-1";
	}
	
	@GetMapping("career")
	public String career() {
		return "home/career/career";
	}
	
	@GetMapping("career-1")
	public String career1() {
		return "home/career/career-1";
	}
} 	
