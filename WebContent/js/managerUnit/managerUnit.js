/**
 * 
 */

function managerSonUnit(event){
	
}

function resetPassword(event){
	
		$.confirm({
		title : '重置密码',
		type : 'red',
		icon : 'fa fa-warning',
		content : '确定要重置密码吗？',
		autoClose : 'cancelAction|8000',
		buttons : {
			deleteUser : {
				text : '确定',
				btnClass : 'btn-blue',
				action : function() {
					$.ajax({
						url:'/jwcpxt/Unit/reset_unitPassword?unit.jwcpxt_unit_id='+event.id,
						type:'GET',
						success:function(data){
							if(data=="1"){
								toastr.success("重置密码成功！");
							}else{
								toastr.error("重置密码失败！");
							}
						}
					})
				}
			},
			cancelAction : {
				text : '关闭',
				btnClass : 'btn-red',
				action : function() {

				}
			}
		}
	})
}

function updateUnit(event) {
	$
			.confirm({
				title : '修改单位',
				type : 'blue',
				boxWidth : '500px',
				useBootstrap : false,
				content : '<div><form id="updateUnitForm">'
					+ '<label>单位名称：</label><input id="update_unit_name" name="unit.unit_name" class="form-control">'
					+ '<label>机构代码：</label><input id="update_unit_num" name="unit.unit_num" class="form-control">'
					+ '<label>单位账号：</label><input id="update_unit_account" name="unit.unit_account" class="form-control">'
					+ '<label>联系电话：</label><input id="update_unit_phone" name="unit.unit_phone" class="form-control">'
					+ '</form></div>',
				buttons : {
					cancel : {
						text : '关闭',
						btnClass : 'btn-red',
						action : function() {

						}
					},
					save : {
						text : '修改',
						btnClass : 'btn-blue',
						action : function() {
							var formData= new FormData(document.getElementById("updateUnitForm"));
							formData.append('unit.jwcpxt_unit_id',event.id)
								$
										.ajax({
											url : '/jwcpxt/Unit/update_unit',
											type : 'POST',
											data : formData,
											processData : false,
											contentType : false,
											success : function(data) {
												if (data == 1) {
													toastr.success("修改成功！");
													loadData();
												} else {
													toastr.error("修改失败！");
												}
											}
										})
						}
					}
				},
				onContentReady : function() {
					$.ajax({
						url:'/jwcpxt/Unit/get_unitDO_byID?unit.jwcpxt_unit_id='+event.id,
						type:'get',
						success:function(data){
							var unitServer = JSON.parse(data);
							$('#update_unit_name').val(unitServer.unit_name);
							$('#update_unit_num').val(unitServer.unit_num);
							$('#update_unit_account').val(unitServer.unit_account);
							$('#update_unit_phone').val(unitServer.unit_phone);
						}
					})
				}
			})
}

function addUnit() {
	$
			.confirm({
				title : '新建二级单位',
				type : 'blue',
				boxWidth : '500px',
				useBootstrap : false,
				content : '<div><form id="unitForm">'
						+ '<label>单位名称：</label><input name="unit.unit_name" class="form-control">'
						+ '<label>机构代码：</label><input name="unit.unit_num" class="form-control">'
						+ '<label>单位账号：</label><input name="unit.unit_account" class="form-control">'
						+ '<label>联系电话：</label><input name="unit.unit_phone" class="form-control">'
						+ '</form></div>',
				buttons : {
					cancel : {
						text : '关闭',
						btnClass : 'btn-red',
						action : function() {

						}
					},
					save : {
						text : '保存',
						btnClass : 'btn-blue',
						action : function() {
							var formData = new FormData(document.getElementById("unitForm"));
							$.ajax({
								url : '/jwcpxt/Unit/save_unit',
								type : 'POST',
								data : formData,
								processData : false,
								contentType : false,
								success : function(data) {
									if (data == 1) {
										toastr.success("新建成功！");
										loadData();
									} else {
										toastr.error("新建失败！");
									}
								}
							})
						}
					}
				},
				onContentReady : function() {

				}
			})
}