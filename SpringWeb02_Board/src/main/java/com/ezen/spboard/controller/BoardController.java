package com.ezen.spboard.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezen.spboard.dto.ReplyVO;
import com.ezen.spboard.dto.SpBoard;

@Controller
public class BoardController {
	
	@Autowired
	BoardService bs;
	// Controller에서 Service의 메서드 호출 -> Service의 메서드에서 Dao의 메서드 호출
	// Dao의 메서드에서 Service의 메서드로 리턴 -> Service 메서드에서 Controller 메서드로 리턴
	// 리턴 받은 내용을 model에 싣고, ~.jsp로 이동
	@RequestMapping("/main")
	public String main(Model model, HttpServletRequest request) {
		ArrayList<SpBoard> list = bs.selectBoard();
		model.addAttribute("boardList", list);
		//게시물 조회 후 main.jsp로 이동
		return "main";
	}
	@RequestMapping("/boardView")
	public String boardView(Model model, HttpServletRequest request) {
		
		String num = request.getParameter("num");
		SpBoard sb = bs.boardView(num);
		model.addAttribute("board", sb);
		
		ArrayList<ReplyVO> list = bs.selectReply(num);
		model.addAttribute("replyList", list);
		
		return "board/boardView";
	}
	@RequestMapping("/addReply")
	public String add_Reply(Model model, HttpServletRequest request) {
		String boardnum = request.getParameter("boardnum");
		
		ReplyVO rvo = new ReplyVO();
		
		rvo.setUserid(request.getParameter("userid"));
		rvo.setContent(request.getParameter("reply"));
		rvo.setBoardnum(Integer.parseInt(boardnum));
		
		bs.addReply(rvo);
		
		return "redirect:/boardViewWithoutcount?num="+boardnum;
	}
	@RequestMapping("/boardViewWithoutcount")
	public String boardViewWithoutcount(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");
		SpBoard sb = bs.getBoard(num);
		model.addAttribute("board", sb);
		
		ArrayList<ReplyVO> list = bs.selectReply(num);
		model.addAttribute("replyList", list);
		
		return "board/boardView";
	}
	
}
