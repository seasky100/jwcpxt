<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
<title>用户信息</title>
</head>
<body>
<div class="wrapper">
	<!-- 引入侧边栏 -->
	<s:action name="skipSidebar" namespace="/Skip" executeResult="true" />
	<div class="main-panel">
		<!-- 引入导航条  -->
		<s:action name="skipNavbar" namespace="/Skip" executeResult="true" />
		<div class="content">
			<div class="container-fluid">
				<!-- 主内容 -->
			</div>
		</div>
		<!-- 引入底部 -->
		<s:action name="skipFooter" namespace="/Skip" executeResult="true" />
	</div>
</div>
</body>
</html>