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
<style type="text/css">
[v-cloak] {
	display: none;
}

i {
	cursor: pointer;
}

th {
	text-align: center !important;
}
</style>
<title>警务测评</title>
</head>
<body>
	<div class="wrapper">
		<!-- 引入侧边栏 -->
		<s:action name="skipSidebar" namespace="/Skip" executeResult="true" />
		<div class="main-panel">
			<!-- 引入导航条  -->
			<s:action name="skipNavbar" namespace="/Skip" executeResult="true" />
			<div class="content" id="content">
				<div class="container-fluid">
					<!-- 主内容 -->
					<div class="row">
						<div class="col-md-12">
							<div class="card" style="padding: 10px;">
								<div class="header">
									<h4 class="title">问卷管理</h4>
								</div>
								<div class="content table-responsive table-full-width">
									<select v-if="isUnit" style="width:120px; float: left;"
										class="selectpicker" name="clientInfoVO.screenUser"
										@change="queryClient" title="请选择单位。。">
										<option v-for="Appraisal in allAppraisal" vlaue="Appraisal.jwcpxt_user_id">{{Appraisal.user_name}}</option>
									</select>
									<div style="width: 500px; float: left; margin-left: 10px;">
										<div class="form-group" style="margin: auto;">
											<input name="clientInfoVO.startTime" @change="queryClient"
												class="mydate form-control" id="beginTime"
												placeholder="起始时间" style="display: inline; width: 150px;">到<input
												name="clientInfoVO.endTime" @change="queryClient"
												id="endTime" placeholder="结束时间" class="mydate form-control"
												style="display: inline; width: 150px;">
										</div>
									</div>
									<input @change="queryClient" placeholder="搜索内容(姓名、电话号码、单位名称)"
										name="clientInfoVO.search" class="form-control"
										style="float: right; width: 250px;">
									<table class="table" style="text-align: center; width: 100%;">
										<thead>
											<tr style="border-bottom: 2px solid #ddd">
												<th style="width: 60px;">姓名</th>
												<th style="width: 60px;">性别</th>
												<th style="width: 60px;">电话</th>
												<th style="width: 60px;"><select class="form-control"
													@change="queryClient" name="clientInfoVO.screenVisit">
														<option value="">全部</option>
														<option value="1">成功</option>
														<option value="2">未回访</option>
														<option value="3">空号</option>
														<option value="4">无人接听</option>
														<option value="5">占线</option>
														<option value="6">停机</option>
														<option value="7">拒访</option>
														<option value="8">其他</option>
												</select></th>
												<th style="width: 60px;">所属业务</th>
												<th style="width: 150px;">办理单位</th>
												<th style="width: 60px;">办理时间</th>
												<th style="width: 60px;">操作</th>
											</tr>
										</thead>
										<tbody v-cloak>
											<template v-for="(ClientInfoDTO,index) in clientInfoVO">
											<tr style="border-top: 1px solid #ddd;">
												<th>{{ClientInfoDTO.serviceClient.service_client_name}}</th>
												<td>{{ClientInfoDTO.serviceClient.service_client_sex}}</td>
												<td>{{ClientInfoDTO.serviceClient.service_client_phone}}</td>
												<td><span
													v-if="ClientInfoDTO.serviceClient.service_client_visit==1"
													class="label label-primary">成功</span> <span
													v-else-if="ClientInfoDTO.serviceClient.service_client_visit==2"
													class="label label-info">未回访</span> <span
													v-else-if="ClientInfoDTO.serviceClient.service_client_visit==3"
													class="label label-warning">空号</span> <span
													v-else-if="ClientInfoDTO.serviceClient.service_client_visit==4"
													class="label label-warning">无人接听</span> <span
													v-else-if="ClientInfoDTO.serviceClient.service_client_visit==5"
													class="label label-warning">占线</span> <span
													v-else-if="ClientInfoDTO.serviceClient.service_client_visit==6"
													class="label label-warning">停机</span> <span
													v-else-if="ClientInfoDTO.serviceClient.service_client_visit==7"
													class="label label-warning">拒访</span> <span
													v-else-if="ClientInfoDTO.serviceClient.service_client_visit==8"
													class="label label-warning">其他</span> <span v-else
													class="label label-warning">未定义</span></td>
												<td>{{ClientInfoDTO.serviceDefinition.service_definition_describe}}</td>
												<td>{{ClientInfoDTO.unit.unit_name}}</td>
												<td>{{ClientInfoDTO.serviceInstance.service_instance_date}}</td>
												<td><a href="javascript:;"
													@click="pageTo(ClientInfoDTO.serviceDefinition.jwcpxt_service_definition_id,ClientInfoDTO.serviceClient.jwcpxt_service_client_id)">业务回访</a>
												</td>
											</tr>
											</template>
										</tbody>
									</table>
									<!-- 分页 -->
									<div id="bottomPage" style="padding: 20px;" v-cloak>
										<span>当前页数:<span id="currPage">{{page.currPage}}</span></span>
										<span>共:<span id="totalPage">{{page.totalPage}}</span>页
										</span> <span class="pageOperation" @click="firstPage">首页</span> <span
											class="pageOperation" @click="prePage">上一页</span> <span
											class="pageOperation" @click="nextPage">下一页</span> <span
											class="pageOperation" @click="lastPage">末页</span> <span><input
											type="text" id="toPageInput"
											style="text-align: center; width: 60px; height: 30px;"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
											onblur="this.v();">
											<button class="btn btn-default" @click="toPage"
												style="height: 30px; vertical-align: middle; margin-bottom: 3px;">跳转</button>
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 引入底部 -->
		<s:action name="skipFooter" namespace="/Skip" executeResult="true" />
	</div>
	</div>
</body>
<script type="text/javascript">
	/* 处理侧边栏选项 */
	$('#sideManager').attr("class", "active");
</script>
<script src="<%=basePath%>js/evaluationPolice/clientInformation.js"></script>
</html>