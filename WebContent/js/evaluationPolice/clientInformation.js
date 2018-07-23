$(function() {
	let myData = {
		ready : false,
		clientInfoVO : [],
		page : {
			currPage : 1,
			totalPage : 1,
			totalCount : 11,
			pageSize : 10,
			haveNextPage : false,
			havePrePage : false,
			isFirstPage : false,
			isLastPage : false
		},
		isUnit : false,
		allAppraisal : [],
		allService : []
	};

	let queryData = {
		"clientInfoVO.currPage" : 1,
		"clientInfoVO.startTime" : '',
		"clientInfoVO.endTime" : '',
		"clientInfoVO.screenService" : '',
		"clientInfoVO.screenVisit" : '',
		"clientInfoVO.screenUser" : '',
		"clientInfoVO.search" : ''
	}

	let vm = new Vue({
		el : "#content",
		data : myData,
		methods : {
			before () {
				//业务id
				let definitionId = getUrlParam('definitionId');
				if (definitionId) {
					//选中传过来的业务ID
					$('input[name="clientInfoVO.screenService"]').val();
				}
				$.post('/jwcpxt/LoginAndLogout/getCurrentUser', {}, response => {
					if (response.jwcpxt_unit_id) {
						myData.isUnit = true;
						//获取所有测评员
						$.post('/jwcpxt/Service/list_userDO', {}, response => {
							myData.allAppraisal = response;
						}, 'json')
					} else if (response.jwcpxt_user_id) {
						//queryData["clientInfoVO.screenUser"] = response.jwcpxt_user_id;
					}
					this.getInfo(queryData);
				}, 'json');
				//获取所有的业务
				$.post('/jwcpxt/Service/list_serviceDefinition_all', {}, response => {
					myData.allService = response;
				}, 'json');

			},
			getInfo (pramas) {
				$.post('/jwcpxt/Service/get_clientInfoVO_byUserId', pramas, response => {
					myData.clientInfoVO = response.listClientInfoDTO;
					myData.page.currPage = response.currPage;
					myData.page.totalPage = response.totalPage;
					myData.page.totalCount = response.totalCount;
					myData.page.pageSize = response.pageSize;
					this.pageInit(response);
					myData.ready = true;
				}, 'json');
			},
			pageInit (response) {
				myData.page.haveNextPage = response.currentPage < response.totalPage;
				myData.page.havePrePage = response.currentPage > 1;
				myData.page.isFirstPage = response.currentPage == 1;
				myData.page.isLastPage = response.currentPage == response.totalPage;
			},
			queryClient ($event) {
				queryData[$event.target.name] = $($event.target).val();
				this.getInfo(queryData);
			},
			pageTo (definition_id, client_id) {
				window.location.href = `/jwcpxt/Skip/skipPoliceAssessmentPage?definitionId=${definition_id}&serviceClientId=${client_id}`;
			},
			firstPage () {
				if (myData.page.isFirstPage) {
					toastr.error("已经是在首页了哦~");
					return;
				}
				queryData["clientInfoVO.currPage"] = 1;
				this.getInfo(queryData)
			},
			prePage () {
				if (!myData.page.havePrePage) {
					toastr.error("没有上一页了哦~");
					return;
				}
				queryData["clientInfoVO.currPage"] -= 1;
				this.getInfo(queryData)
			},
			nextPage () {
				if (!myData.page.haveNextPage) {
					toastr.error("没有下一页了哦~");
					return;
				}
				queryData["clientInfoVO.currPage"] += 1;
				this.getInfo(queryData)
			},
			lastPage () {
				if (myData.page.isLastPage) {
					toastr.error("已经是在尾页了哦~");
					return;
				}
				queryData["clientInfoVO.currPage"] = myData.page.totalPage;
				this.getInfo(queryData)
			},
			toPage () {
				let pageIndex = $('#toPageInput').val();
				if (pageIndex < 1 || pageIndex > myData.page.totalPage) {
					toastr.error("输入的数字不在页数范围内,请检查页码");
					return;
				}
				queryData["clientInfoVO.currPage"] = pageIndex;
				this.getInfo(queryData)
			},
		},
		mounted () {
			this.before();
		},
	})

	randerTimeUtil();
	function randerTimeUtil() {
		$.datetimepicker.setLocale('ch');
		$('.mydate').datetimepicker({
			pickerPosition : "top-right",
			yearStart : 1900, // 设置最小年份
			yearEnd : 2050, // 设置最大年份
			yearOffset : 0, // 年偏差
			timepicker : false, // 关闭时间选项
			format : 'Y-m-d', // 格式化日期年-月-日
			minDate : '1900/01/01', // 设置最小日期
			maxDate : '2050/01/01', // 设置最大日期
		});
	}

	function getUrlParam(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null) return unescape(r[2]);
		return null;
	}
})