package kr.co.itcen.mysite.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import kr.co.itcen.mysite.service.BoardService;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.UserVo;


@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	private static final int SHOW_PAGE = 5;
	private static final int SHOW_CNT = 5;

	/* board list */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String list(@RequestParam(name = "kwd", required = true, defaultValue = "") String kwd,
			@RequestParam(name = "page", required = true, defaultValue = "") String page,
			@RequestParam(name = "move", required = true, defaultValue = "") String move, Model model) {

		int currentPage = 1;

		if ("".equals(page) || page == null) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(page);
		}

		int countAll = boardService.countAll();

		if (!"".equals(kwd)) {
			countAll = boardService.countAll(kwd);

		}

		String pageMove = move;

		int pageAll = countAll % SHOW_CNT == 0 ? countAll / SHOW_CNT : countAll / SHOW_CNT + 1;
		int startPage = (currentPage % SHOW_PAGE) == 0 ? ((currentPage / SHOW_PAGE) - 1) * SHOW_PAGE + 1
				: ((currentPage / SHOW_PAGE) * SHOW_PAGE) + 1;
		int lastPage = startPage + SHOW_PAGE - 1;

		if ("next".equals(pageMove)) {
			startPage = currentPage;
			lastPage = startPage + (SHOW_PAGE - 1);
		} else if ("prev".equals(pageMove)) {
			startPage = currentPage - (SHOW_PAGE - 1);
			lastPage = currentPage;
		}

		if (pageAll < lastPage) {
			lastPage = pageAll;
		}

		model.addAttribute("countAll", countAll - (currentPage - 1) * SHOW_CNT);
		model.addAttribute("pageAll", pageAll);
		model.addAttribute("startPage", startPage);
		model.addAttribute("lastPage", lastPage);

		List<BoardVo> list = boardService.getList(currentPage, SHOW_CNT, kwd);
		model.addAttribute("list", list);

		return "board/list";
	}

	/* board write */
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(@RequestParam(name = "flag", required = true, defaultValue = "0") Boolean flag,
			@RequestParam(name = "oNo", required = true, defaultValue = "1") int oNo,
			@RequestParam(name = "gNo", required = true, defaultValue = "0") int gNo,
			@RequestParam(name = "depth", required = true, defaultValue = "0") int depth, Model model) {

		if (!flag) {
			model.addAttribute("flag", flag);
			return "board/write";
		} else {

			model.addAttribute("flag", flag);
			model.addAttribute("oNo", oNo);
			model.addAttribute("gNo", gNo);
			model.addAttribute("depth", depth);
			return "board/write";
		}

	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(@ModelAttribute BoardVo vo, HttpSession session) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		Long userNo = authUser.getNo();
		vo.setUserNo(userNo);
		
		boardService.insert(vo);

		return "redirect:/board";
	}

	/* delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam(name = "no", required = true, defaultValue = "0") Long no,
			@RequestParam(name = "gNo", required = true, defaultValue = "0") int gNo,
			@RequestParam(name = "status", required = true, defaultValue = "0") int status,
			@ModelAttribute BoardVo vo) {
		vo.setNo(no);
		vo.setgNo(gNo);
		vo.setStatus(status);

		boardService.delete(vo);

		return "redirect:/board";

	}

	/* view */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(@RequestParam(name = "no", required = true, defaultValue = "0") Long no,
			@RequestParam(name = "kwd", required = true, defaultValue = "0") String kwd,
			@RequestParam(name = "page", required = true, defaultValue = "0") int page, @ModelAttribute BoardVo vo,
			Model model) {

		vo.setNo(no);
		boardService.hitUpdate(vo);

		BoardVo list = boardService.getList(no);
		model.addAttribute("list", list);

		return "board/view";
	}

	@RequestMapping(value = "/view", method = RequestMethod.POST)
	public String view(@ModelAttribute BoardVo vo, HttpSession session) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		Long userNo = authUser.getNo();
		vo.setUserNo(userNo);

		boardService.insert(vo);

		return "redirect:/board";
	}

	/* modify */
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modify(@RequestParam(name = "no", required = true, defaultValue = "0") Long no,
			@RequestParam(name = "title", required = true, defaultValue = "") String title,
			@RequestParam(name = "content", required = true, defaultValue = "") String content, @ModelAttribute BoardVo vo,
			Model model) {

	
		vo.setTitle(title);
		vo.setContents(content);
		
		model.addAttribute("list", vo);

		return "board/modify";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute BoardVo vo, HttpSession session) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		Long userNo = authUser.getNo();
		vo.setUserNo(userNo);

		boardService.modify(vo);

		return "redirect:/board";
	}
}
