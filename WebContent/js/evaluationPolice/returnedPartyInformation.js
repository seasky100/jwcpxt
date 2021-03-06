$(function() {
	let myData = {
		returnedParty : {
			serviceInstance : {
				service_client_name : ''
			},
			serviceClient : {
				service_client_sex : '',
				service_client_phone : ''
			},
			serviceDefinition : {
				service_definition_describe : ''
			}
		},
	};

	let vm = new Vue({
		el : "#content",
		data : myData,
		methods : {
			getInfo () {
				$.post('/jwcpxt/Service/get_notServiceClient_byServiceClientId', '', response => {
					this.returnedParty = response;
				}, 'json')
			},
			beginReturned () {
				window.location.href = `/jwcpxt/Skip/skipPoliceAssessmentPage?definitionId=${this.returnedParty.serviceDefinition.jwcpxt_service_definition_id}&serviceClientId=${this.returnedParty.serviceClient.jwcpxt_service_client_id}`;
			}
		},
		mounted () {
			this.getInfo();
		},
	})
})