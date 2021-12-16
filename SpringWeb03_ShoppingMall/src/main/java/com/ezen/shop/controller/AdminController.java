package com.ezen.shop.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.shop.dto.Paging;
import com.ezen.shop.dto.ProductVO;
import com.ezen.shop.service.AdminService;
import com.ezen.shop.service.ProductService;
import com.ezen.shop.service.QnaService;

@Controller
public class AdminController {
	
	@Autowired
	ProductService ps; // getProduct() 사용
	
	@Autowired
	QnaService qs; //getQna() 사용
	
	@Autowired
	ServletContext context; // 파일 업로드를 위해 준비
	
	@Autowired
	AdminService as;
	
	@RequestMapping("/")
	public String admin() {
		return "admin/adminLoginForm";
	}
	
	@RequestMapping("adminLogin")
	public ModelAndView adminLogin(HttpServletRequest request, @RequestParam("workId")String workId,
			@RequestParam("workPwd")String workPwd) {
		ModelAndView mav = new ModelAndView();
		
		int result = as.workerCheck(workId, workPwd);
		// result 값이 1 이면 정상로그인, 0이면 비밀번호 오류, -1이면 아이디 없음
		
		if(result == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("workId", workId);
			mav.setViewName("redirect:/productList");
		}else if(result ==0) {
			mav.addObject("message", "비밀번호가 맞지 않습니다.");
			mav.setViewName("admin/adminLoginForm");
		}else if(result == -1) {
			mav.addObject("message", "아이디를 확인하세요");
			mav.setViewName("admin/adminLoginForm");
		}else {
			mav.addObject("message", "원인미상의 오류로 로그인 실패");
			mav.setViewName("admin/adminLoginForm");
		}
		return mav;
	}
	
	@RequestMapping("productList")
	public ModelAndView product_List(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("workId");
		if(id == null) {
			mav.setViewName("redirect:/admin");
		}else {
			int page = 1;
			if(request.getParameter("page")!=null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			}else if (session.getAttribute("page")!=null) {
				page =(Integer)session.getAttribute("page");
			}else {
				page=1;
				session.removeAttribute("page");
			}
			
			String key = "";
			if(request.getParameter("key")!=null) {
				key = request.getParameter("key");
				session.setAttribute("key", key);
			}else if (session.getAttribute("key")!=null) {
				key =(String)session.getAttribute("key");
			}else {
				session.removeAttribute("key");
				key = "";
			}
			
			
			Paging paging = new Paging();
			paging.setPage(page);
			
			int count = as.getAllCount("product", "name", key);
			paging.setTotalCount(count);
			
			List<ProductVO> productList = as.listProduct(paging, key);
			
			request.setAttribute("paging",paging);
			mav.addObject("productList",productList);
			request.setAttribute("key",key);
			mav.setViewName("admin/product/productList");
		}
		return mav;
	}
	
	@RequestMapping("adminProductDetail")
	public ModelAndView Product_Detail(HttpServletRequest request, @RequestParam("pseq") String pseq) {
		ModelAndView mav = new ModelAndView();
		
		ProductVO pvo = ps.getProduct(pseq);
		
		String kindList[] = {"0", "Heels", "Boots", "Sandals", "Slipers", "Shcakers", "Sale"};
		int index = Integer.parseInt(pvo.getKind());
		
		mav.addObject("productVO", pvo);
		mav.addObject("kind", kindList[index]);
		mav.setViewName("admin/product/productDetail");
		
		return mav;
	}
	
}
