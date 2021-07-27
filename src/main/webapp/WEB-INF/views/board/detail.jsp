<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../layout/header.jsp"%>
<div class="container">
	<button class="btn btn-secondary" onclick="history.back()">돌아가기</button>
	<c:if test="${board.user.id == principal.user.id}">
		<a href="/board/${board.id}/updateForm" class="btn btn-warning">수정</a>
		<button id="btn-delete" class="btn btn-danger">삭제</button>
	</c:if>
	<br/><br/>
	<div>
		글번호 : <span id="id"><i>${board.id}</i></span>
		작성자 : <span><i>${board.user.username}</i></span>
	</div>
	<br/>
	<div>
		<h3>${board.title}</h3>
	</div>
	<hr>
	<div>
		<div>${board.content}</div>
	</div>
	
	<div class="card">
		<form>
			<input type="hidden" id="userId" value="${principal.user.id}" />
			<input type="hidden" id="boardId" value="${board.id}" />
			<div class="card-body">
				<textarea id="reply-content" rows="1" class="form-control"></textarea>
			</div>
			<div class="card-footer">
				<button type="button" id="btn-reply-save" class="btn btn-primary">등록</button>
			</div>
		</form>
	</div>
	
	<div class="card">
		<div class="card-header">댓글 리스트</div>
		<ui id="reply-box" class="list-group">
			<c:forEach var="reply" items="${board.replys}">
				<li id="reply-${reply.id} }" class="list-group-item d-flex justify-content-between">
					<div>${reply.content}</div>
					<div class="d-flex">
						<div class="font-italic">작성자 : ${reply.user.username} &nbsp; </div>
						<c:if test="${principal.user.id eq reply.user.id}">
							<button onclick="index.replyDelete(${board.id}, ${reply.id})" class="badge">삭제</button>
						</c:if>
					</div>
				</li>
			</c:forEach>
		</ui>
	</div>
	<hr>
	<button id="btn-save" class="btn btn-primary">글쓰기 완료</button>
</div>
<script src="/js/board.js"></script>
<%@include file="../layout/footer.jsp"%>