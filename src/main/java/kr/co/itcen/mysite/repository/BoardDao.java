package kr.co.itcen.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.itcen.mysite.vo.BoardVo;

@Repository
public class BoardDao {

	@Autowired
	private SqlSession sqlSession;

	public int countAll() {
		int cnt = sqlSession.selectOne("board.countAll");
		return cnt;
	}

	public int countAll(String kwd) {
		String keyword = "%" + kwd + "%";
		int cnt = sqlSession.selectOne("board.countKeywordAll", keyword);
		return cnt;
	}

	public List<BoardVo> getList(int page, int showCont, String kwd) {

		Map<String, Object> map = new HashMap<String, Object>();

		String keyword = "%" + kwd + "%";
		map.put("page", (page - 1) * showCont);
		map.put("showCont", showCont);
		map.put("keyword", keyword);

		List<BoardVo> result = sqlSession.selectList("board.getList", map);

		return result;

	}

	public void insert(BoardVo vo) {

		if (!vo.isFlag()) {
			sqlSession.insert("board.insertFirst", vo);
		} else {
			sqlSession.update("board.replyUpdate", vo);
			sqlSession.insert("board.insertOther", vo);
		}

	}

	public void delete(BoardVo vo) {

		sqlSession.update("board.delete", vo);
		sqlSession.update("board.statusCheck", vo);
	}

	public void hitUpdate(BoardVo vo) {

		sqlSession.update("board.hitUpdate", vo);

	}

	/* view.jsp를 위한 getList overload */
	public BoardVo getList(Long no) {
		return sqlSession.selectOne("board.view", no);
	}

	public void modify(BoardVo vo) {

		sqlSession.update("board.modify", vo);
		System.out.println(vo);
	}

	public void replyUpdate(BoardVo vo) {

		sqlSession.update("board.replyUpdate", vo);

	}
}
