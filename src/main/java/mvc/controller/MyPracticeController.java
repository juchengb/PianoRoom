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

/**
 * MyPracticeController 處理我的練習頁面相關功能。
 */
@Controller
@RequestMapping("/mypractice")
public class MyPracticeController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ReservationDao reservationDao;
	
	/**
	 * GET 請求，顯示我的練習頁面，包含使用者的月練習數據、月數據和排行榜。
	 * 
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return mypractice 頁面
	 */
	@GetMapping("")
	public String mypracticePage(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		// ---------------------------------------------------------------------------------------------------
		// 使用者月數據資訊
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
		// 排行榜資訊
		List<RankingUser> rankingUsers = userDao.findAllUsersMonthlyDatas();
		model.addAttribute("rankingUsers", rankingUsers);
		return "frontend/mypractice";
	}
	
	/**
	 * GET 請求，以 JSON 格式回傳月練習數據以繪製圖表，包含日期和每日練習分鐘數。
	 * 
	 * @param session HttpSession
	 * @return 練習數據的 Map<String, List<Object>>
	 */
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
