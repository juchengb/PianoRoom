package mvc.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mvc.bean.RankingUser;
import mvc.bean.UserMonthlyDatas;
import mvc.dao.UserDao;
import mvc.entity.User;

@Controller
@RequestMapping("/mypractice")
public class MyPracticeController {
	
	@Autowired
	private UserDao userDao;
	
	@GetMapping("")
	public String mypracticePage(HttpSession session, Model model) {
		// find user
		User user = (User)session.getAttribute("user");
		
		// ---------------------------------------------------------------------------------------------------
		// dashboard
		Optional<UserMonthlyDatas> monthlyOpt = userDao.getUserMonthlyDatasByUserId(user.getId());
		
		if (monthlyOpt.isPresent()) {
			UserMonthlyDatas monthly = monthlyOpt.get();
			model.addAttribute("monthly", monthly);
		} else {
			model.addAttribute("monthly.counts", 0);
			model.addAttribute("monthly.hours", 0);
			model.addAttribute("monthly.ranking", 0);
		}
		// ---------------------------------------------------------------------------------------------------
		// ranking
		List<RankingUser> rankingUsers = userDao.findAllUsersMonthlyDatas();
		model.addAttribute("rankingUsers", rankingUsers);
		
		return "frontend/mypractice";
	}

	
}
