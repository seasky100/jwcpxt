$(function() {
	let definitionId = getUrlParam('definitionId');
	let serviceClientId = getUrlParam('serviceClientId');
	let myData = {
		definitionId : definitionId, //业务定义ID
		serviceClientId : serviceClientId, //业务当事人ID
		serviceClien : {
			service_client_name : '暂无信息',
			service_client_sex : '暂无信息',
			service_client_phone : '暂无信息'
		},
		serviceDefinition : {
			service_definition_describe : '暂无信息'
		},
		questionData : [],
	};
	let listAnswerDTO = [];
	let listAnswerInquiriesDTO = [];
	let vm = new Vue({
		el : "#content",
		data : myData,
		methods : {
			getInfo () {
				//获取业务定义
				if (definitionId != 'revisit') {
					$.post('/jwcpxt/Service/get_serviceDefinitionDo_byId', {
						"serviceDefinition.jwcpxt_service_definition_id" : myData.definitionId
					}, response => {
						myData.serviceDefinition = response;
					}, 'json')
				}
				//获取当事人ID
				/*$.post('/jwcpxt/Service/get_serviceClientDo_byId', {
					"serviceClient.jwcpxt_service_client_id" : myData.serviceClientId
				}, response => {
					myData.serviceClien = response;
				}, 'json')*/
				//获取所有问题
				$.post('/jwcpxt/Question/list_questionDTO_byServiceDefinition', {
					"serviceDefinition.jwcpxt_service_definition_id" : myData.definitionId
				}, response => {
					myData.questionData = response;
				}, 'json')
			},
		},
		mounted () {
			this.getInfo();
			this.$nextTick(function() {
				setTimeout(function() {
					$('input').iCheck({
						checkboxClass : 'icheckbox_square-blue',
						radioClass : 'iradio_square-blue',
						increaseArea : '20%' // optional
					});
				}, 1000);
			})
		},
	});

})


function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]);
	return null;
}