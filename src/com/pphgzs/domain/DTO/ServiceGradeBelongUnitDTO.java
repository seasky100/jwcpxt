package com.pphgzs.domain.DTO;

import com.pphgzs.domain.DO.jwcpxt_service_definition;

//业务定义与分数
public class ServiceGradeBelongUnitDTO {
	private jwcpxt_service_definition serviceDefinition;
	private double grade;

	@Override
	public String toString() {
		return "ServiceGradeBelongUnitDTO [serviceDefinition=" + serviceDefinition + ", grade=" + grade + "]";
	}

	public jwcpxt_service_definition getServiceDefinition() {
		return serviceDefinition;
	}

	public void setServiceDefinition(jwcpxt_service_definition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

}
