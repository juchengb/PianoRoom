package mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypractice")
public class MyPracticeController {
	
	@GetMapping("")
	public String page(Model model) {
		return "frontend/mypractice";
	}
}
