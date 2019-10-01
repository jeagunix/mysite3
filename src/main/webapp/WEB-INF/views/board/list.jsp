<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link
	href="${pageContext.servletContext.contextPath }/assets/css/board.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form"
					action="${pageContext.servletContext.contextPath }/board"
					method="get">
					<input type="text" id="kwd" name="kwd" value=""> <input
						type="submit" value="찾기">
					<input type="hidden" name="page" value="1"/>
					<input type="hidden" name="move" value=""/>
				</form>

				<%
					pageContext.setAttribute("newline", "\n");
				%>
				<c:set var="count" value='${fn:length(list) }' />


				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<c:forEach items='${list }' var='vo' varStatus='status'>
						<tr>
							<td>${count-(status.index+1) }</td>
							<td style='padding-left:${50*vo.depth }px'><c:if
									test='${vo.depth != 0 }'>
									<img
										src='${pageContext.servletContext.contextPath }/assets/images/reply.png' />
								</c:if> <a
								href="${pageContext.servletContext.contextPath }/board/view?no=${vo.no }&kwd=${param.kwd }&page=${param.page }">${vo.title }</a>
							</td>

							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<td><c:if test="${vo.userNo==authUser.no}">
									<a
										href="${pageContext.servletContext.contextPath }/board/delete?no=${vo.no}&gNo=${vo.gNo}&status=${vo.status}"
										class="del">삭제</a>
								</c:if></td>

						</tr>
					</c:forEach>


				</table>


				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<c:if test="${param.page-5>=1 }">
							<li><a href="${pageContext.servletContext.contextPath }/board?kwd=${param.kwd}&page=${startPage-1}&move=prev">◀</a></li>
						</c:if>						
						<c:forEach begin="${startPage }" end="${lastPage }" step="1" var="i">
							<c:choose>
								<c:when test="${param.page==i }">
									<li><span style="font-size:16px">${i }</span></li>
								</c:when>	
								<c:otherwise>
									<li><a href="${pageContext.servletContext.contextPath }/board?kwd=${param.kwd}&page=${i }">${i }</a></li>
								</c:otherwise>
							</c:choose>								
						</c:forEach>
						<c:if test="${pageAll >= startPage+5 }">
							<li><a href="${pageContext.servletContext.contextPath }/board?kwd=${param.kwd}&page=${lastPage+1 }&move=next">▶</a></li>
						</c:if>
						
					</ul>
				</div>	

				<div class="bottom">
					<c:if test="${authUser!=null}">
						<a
							href="${pageContext.servletContext.contextPath }/board/write?flag=false"
							id="new-book">글쓰기</a>
					</c:if>
				</div>

			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board" />
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>