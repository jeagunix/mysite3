package kr.co.itcen.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.itcen.mysite.repository.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;



@Repository
public class BoardService {
	
	@Autowired
	private BoardDao boardDao;
	
	public List<BoardVo> getList(int page, int showCont, String kwd) {
		
		return boardDao.getList(page, showCont, kwd);
	}
	
	public int countAll() {

		return boardDao.countAll();
	}

	public int countAll(String kwd) {

		return boardDao.countAll(kwd);
	}

	public void insert(BoardVo vo) {
		
		boardDao.insert(vo);
		
	}

	public void delete(BoardVo vo) {
		boardDao.delete(vo);
		
	}

	public void hitUpdate(BoardVo vo) {
		boardDao.hitUpdate(vo);
		
	}

	public BoardVo getList(Long no) {
		return boardDao.getList(no);
	}

	public void modify(BoardVo vo) {
		boardDao.modify(vo);
		
	}


}
