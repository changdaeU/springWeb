package com.ezen.spboard.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezen.spboard.dto.Paging;
import com.ezen.spboard.dto.ReplyVO;
import com.ezen.spboard.dto.SpBoard;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class BoardController {
	
	@Autowired
	BoardService bs;
	// Controller에서 Service의 메서드 호출 -> Service의 메서드에서 Dao의 메서드 호출
	// Dao의 메서드에서 Service의 메서드로 리턴 -> Service 메서드에서 Controller 메서드로 리턴
	// 리턴 받은 내용을 model에 싣고, ~.jsp로 이동
	@Autowired
	ServletContext context; // session을 통하지 않고, 스프링 컨테이너에 있는 빈으로 가져다 씁니다.
	
	@RequestMapping("/main")
	public String main(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("loginUser")==null)
			return "loginform";
		else{
			int page = 1;
			
			if(request.getParameter("page")!=null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			}else if(session.getAttribute("page")!=null){
				page = (int) session.getAttribute("page");
			}else {
				page=1;
				session.removeAttribute("page");
			}
			
			Paging paging = new Paging();
			paging.setPage(page);
			
			int count = bs.getAllcount();
			paging.setTotalCount(count);
			
			ArrayList<SpBoard> list = bs.selectBoard(paging);
			model.addAttribute("paging", paging);
			model.addAttribute("boardList", list);
		}		
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
	@RequestMapping("/deleteReply")
	public String deleteReply(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");
		String boardnum = request.getParameter("boardnum");
		bs.deleteReply(num);
		
		return "redirect:/boardViewWithoutcount?num="+boardnum;
	}
	@RequestMapping("/boardWriteForm")
	public String boardWriteForm(Model model, HttpServletRequest request) {
		String url = "board/boardWriteForm";
		HttpSession session = request.getSession();
		if(session.getAttribute("loginUser")==null	) url="loginform";
		return url;
	}
	@RequestMapping("/boardWrite")
	public String board_Write(Model model, HttpServletRequest request) {
		
		//HttpSession session = request.getSession();
		//ServletContext context = session.getServletContext();
		// session을 거치지않고 스프링 컨테이너에 있는 빈 형태의 ServletContext를 가져다 씁니다
		String path=context.getRealPath("resources/upload");
		try {
			MultipartRequest multi = new MultipartRequest(
					request, path, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy());
			SpBoard sb = new SpBoard();
			sb.setPass(multi.getParameter("pw"));
			sb.setUserid(multi.getParameter("userid"));
			sb.setEmail(multi.getParameter("email"));
			sb.setTitle(multi.getParameter("title"));
			sb.setContent(multi.getParameter("content"));
			sb.setImagename(multi.getFilesystemName("imagename"));
			
			bs.insertBoard(sb);
		} catch (IOException e) {e.printStackTrace();
		}
		
		return "redirect:/main";
	}
	
	@RequestMapping("/boardEditForm")
	public String board_Edit_Form(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");
		model.addAttribute("num", num);
		return "board/boardCheckPassForm";
	}
	@RequestMapping("/boardEdit")
	public String board_Edit(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");
		String pass = request.getParameter("pass");
		
		SpBoard sb = bs.getBoard(num);
		model.addAttribute("num", num);
		if(pass.equals(sb.getPass())) {
			return "board/boardCheckPass";
		}else {
			model.addAttribute("message", "비밀번호가 맞지 않습니다. 확인해 주세요");
			return "board/boardCheckPassForm";
		}
		
	}
	
	@RequestMapping("/boardUpdateForm")
	public String board_Update_Form(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");
		SpBoard sb = bs.getBoard(num);
		model.addAttribute("num", num);
		model.addAttribute("board", sb);
		return "board/boardEditForm";
	}
	@RequestMapping("/boardUpdate")
	public String board_Update(Model model, HttpServletRequest request) {
		String savePath = context.getRealPath("resources/upload");
		SpBoard sb = new SpBoard();
		int num = 0;
		try {
			MultipartRequest multi = new MultipartRequest(
					request, savePath, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy());
			num = Integer.parseInt(multi.getParameter("num"));
			sb.setNum(num);
			sb.setPass(multi.getParameter("pw"));
			sb.setUserid(multi.getParameter("userid"));
			sb.setEmail(multi.getParameter("email"));
			sb.setTitle(multi.getParameter("title"));
			sb.setContent(multi.getParameter("content"));
			String filename = multi.getFilesystemName("imagename"); // 수정하고자 하는 파일이름
			
			if(filename == null) filename = multi.getParameter("oldfilename");
			sb.setImagename(filename);
			bs.boardUpdate(sb);
		} catch (IOException e) {e.printStackTrace();
		}
		
		return "redirect:/boardViewWithoutcount?num="+ num;
	}
	@RequestMapping("/boardDeleteForm")
	public String board_delete_form(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");
		SpBoard sb = bs.getBoard(num);
		model.addAttribute("num", num);
		model.addAttribute("board", sb);
		return "board/boardCheckPassForm";
	}
	@RequestMapping("/boardDelete")
	public String board_delete(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");
		bs.boardDelete(num);
		return "redirect:/main";
	}
}
