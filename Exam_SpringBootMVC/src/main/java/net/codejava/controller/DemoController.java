package net.codejava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class DemoController {
	@GetMapping("index")
	public String index() {
		return "demo/index";
	}
	
	@GetMapping("about")
	public String about() {
		return "demo/about";
	}
	
	@GetMapping("contact")
	public String contact() {
		return "demo/contact";
	}
	
	@GetMapping("service")
	public String service() {
		return "demo/service";
	}
	
	@GetMapping("industries")
	public String industries() {
		return "demo/industries";
	}
	
	@GetMapping("blog")
	public String blog() {
		return "demo/blog/blog";
	}
	
	@GetMapping("blog-1")
	public String blog1() {
		return "demo/blog/blog-1";
	}
	
	@GetMapping("career")
	public String career() {
		return "demo/career/career";
	}
	
	@GetMapping("career-1")
	public String career1() {
		return "demo/career/career-1";
	}
} 	
