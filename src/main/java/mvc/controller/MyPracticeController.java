package mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mvc.dao.ReservationDao;
import mvc.dao.UserDao;
import mvc.model.dto.RankingUser;
import mvc.model.po.User;
import mvc.model.vo.DailyMinutes;
import mvc.model.vo.UserMonthlyDatas;

@Controller
@RequestMapping("/mypractice")
public class MyPracticeController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ReservationDao reservationDao;
	
	@GetMapping("")
	public String mypracticePage(HttpSession session, Model model) {
		// find user
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		// ---------------------------------------------------------------------------------------------------
		// dashboard
		Optional<UserMonthlyDatas> monthlyOpt = userDao.getUserMonthlyDatasByUserId(user.getId());
		
		if (monthlyOpt.isPresent()) {
			UserMonthlyDatas monthly = monthlyOpt.get();
			model.addAttribute("monthly", monthly);
		} else {
			model.addAttribute("monthly.counts", 0);
			model.addAttribute("monthly.minutes", 0);
			model.addAttribute("monthly.ranking", 0);
		}
		// ---------------------------------------------------------------------------------------------------
		// ranking
		List<RankingUser> rankingUsers = userDao.findAllUsersMonthlyDatas();
		model.addAttribute("rankingUsers", rankingUsers);

		
		return "frontend/mypractice";
	}
	
	@GetMapping("/chartdatas")
	@ResponseBody
    public Map<String, List<Object>> getChartedDatas(HttpSession session) {
		User user = (User)session.getAttribute("user");
		List<DailyMinutes> dailyMinutesList = reservationDao.getDailyMinutesByUserId(user.getId());

        List<Object> labels = new ArrayList<>();
        List<Object> datas = new ArrayList<>();


        for (DailyMinutes dailyMinutes : dailyMinutesList) {
        	labels.add(dailyMinutes.getDay());
            datas.add(dailyMinutes.getMinutes());
        }
        
        Map<String, List<Object>> result = new HashMap<>();
        result.put("labels", labels);
        result.put("datas", datas);

        return result;
    }

	
}
