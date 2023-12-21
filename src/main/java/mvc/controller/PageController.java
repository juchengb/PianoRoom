package mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {
	@GetMapping("/")
	public String index(Model model) {
		return "frontend/home";
	}
	
	@GetMapping("/reservation")
	public String reservation(Model model) {
		return "frontend/reservation";
	}
	
	@GetMapping("/myreservation")
	public String myReservation(Model model) {
		return "frontend/myreservation";
	}
	
	@GetMapping("/mypractice")
	public String myPractice(Model model) {
		return "frontend/mypractice";
	}
	
}
