package com.pphgzs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.pphgzs.dao.StatisticsDao;
import com.pphgzs.dao.UnitDao;
import com.pphgzs.domain.DO.jwcpxt_service_definition;
import com.pphgzs.domain.DO.jwcpxt_unit;
import com.pphgzs.domain.DTO.ServiceGradeBelongUnitDTO;
import com.pphgzs.domain.DTO.ServiceGradeDTO;
import com.pphgzs.domain.DTO.UnitHaveServiceGradeDTO;
import com.pphgzs.domain.VO.StatisticsVO;
import com.pphgzs.service.ServiceService;
import com.pphgzs.service.StatisticsService;

public class StatisticsServiceImpl implements StatisticsService {
	private StatisticsDao statisticsDao;
	private UnitDao unitDao;
	private ServiceService serviceService;

	@Override
	public void writeStatisticsExcel(StatisticsVO statisticsVO, HSSFWorkbook wb) {
		// 第二步，在webbook中添加一个sheet，对应Excel文件中的 sheet
		HSSFSheet sheet = wb.createSheet("统计数据");
		// 第三步，在sheet中添加表头第0行，注意老版本poi对Excel的行数列数有限制
		HSSFRow row = sheet.createRow(0);

		// 第五步，创建表头单元格，并设置样式
		HSSFCell cell;

		// 表头数
		int sheetHead_num = 0;

		/**
		 * 设置表头
		 */
		for (String sheetHeadName : statisticsVO.getSheetHeadNameList()) {
			cell = row.createCell(sheetHead_num++);
			cell.setCellValue(sheetHeadName);
		}
		cell = row.createCell(sheetHead_num++);
		cell.setCellValue("总分");

		cell = row.createCell(sheetHead_num++);
		cell.setCellValue("排名");

		/**
		 * 写入数据 遍历单位
		 */
		int listNum = 1;
		for (UnitHaveServiceGradeDTO unitHaveServiceGradeDTO : statisticsVO.getUnitHaveServiceGradeDTOList()) {
			/*
			 * 一行
			 */
			row = sheet.createRow(listNum++);
			/*
			 * 遍历一个单位的分
			 */
			sheetHead_num = 0;
			for (ServiceGradeBelongUnitDTO serviceGradeBelongUnitDTO : unitHaveServiceGradeDTO
					.getServiceGradeBelongUnitDTOList()) {
				cell = row.createCell(sheetHead_num++);
				cell.setCellValue(serviceGradeBelongUnitDTO.getGrade());
			}
			// 总分
			cell = row.createCell(sheetHead_num++);
			cell.setCellValue(unitHaveServiceGradeDTO.getTotalGrade());
			// 排名
			cell = row.createCell(sheetHead_num++);
			cell.setCellValue(listNum - 1);

		}

	}

	@Override
	public StatisticsVO getGradeByCondition(String[] unitIds, String searchTimeStart, String searchTimeEnd,
			List<ServiceGradeDTO> serviceGradeDTOList) {
		StatisticsVO statisticsVO = new StatisticsVO();
		List<UnitHaveServiceGradeDTO> unitHaveServiceGradeDTOList = new ArrayList<UnitHaveServiceGradeDTO>();

		UnitHaveServiceGradeDTO unitHaveServiceGradeDTO;
		for (int i = 0; i < unitIds.length; i++) {
			int totalGrade = 0;
			// 创建一个单位
			unitHaveServiceGradeDTO = new UnitHaveServiceGradeDTO();
			jwcpxt_unit unit = unitDao.get_unitDO_byID(unitIds[i]);
			unitHaveServiceGradeDTO.setUnit(unit);
			// 创建一个业务分数list
			List<ServiceGradeBelongUnitDTO> serviceGradeBelongUnitDTOList = new ArrayList<ServiceGradeBelongUnitDTO>();
			// 遍历需要统计的业务，查询这项业务分数
			for (ServiceGradeDTO serviceGradeDTO : serviceGradeDTOList) {
				int statisticsGrade;

				// 创建一个业务分数DTO
				ServiceGradeBelongUnitDTO serviceGradeBelongUnitDTO = new ServiceGradeBelongUnitDTO();
				// 将业务DO放入到DTO中
				serviceGradeBelongUnitDTO.setServiceDefinition(
						serviceService.get_serviceDefinitionDO_byServiceDefinitionID(serviceGradeDTO.getService_id()));
				// 统计这个单位下这个业务所得分

				if (unit.getUnit_grade() == 2) {
					statisticsGrade = statisticsDao.geteStatisticsGrade_byFatherUnit(serviceGradeDTO, unitIds[i],
							searchTimeStart, searchTimeEnd);
				} else {
					statisticsGrade = statisticsDao.geteStatisticsGrade(serviceGradeDTO, unitIds[i], searchTimeStart,
							searchTimeEnd);
				}
				totalGrade = totalGrade + statisticsGrade;
				// 将所得分放入到DTO中
				serviceGradeBelongUnitDTO.setGrade(statisticsGrade);
				// 将一个业务及他的分数的DTO放入到DTO列表中
				serviceGradeBelongUnitDTOList.add(serviceGradeBelongUnitDTO);
			}
			// 把所有业务的分数放入到单位中
			unitHaveServiceGradeDTO.setServiceGradeBelongUnitDTOList(serviceGradeBelongUnitDTOList);
			unitHaveServiceGradeDTO.setTotalGrade(totalGrade);
			// 这个单位DTO放入到单位列表中
			unitHaveServiceGradeDTOList.add(unitHaveServiceGradeDTO);

		}
		// 将统计完的所有单位放到VO中
		statisticsVO.setUnitHaveServiceGradeDTOList(unitHaveServiceGradeDTOList);
		/*
		 * 表头列表
		 */
		List<String> sheetHeadNameList = new ArrayList<String>();
		for (ServiceGradeDTO _serviceGradeDTO : serviceGradeDTOList) {
			if (_serviceGradeDTO.getService_id().equals("revisit")) {
				sheetHeadNameList.add("整改情况");
			} else {
				jwcpxt_service_definition serviceDefinition = serviceService
						.get_serviceDefinitionDO_byServiceDefinitionID(_serviceGradeDTO.getService_id());
				if (serviceDefinition != null) {
					sheetHeadNameList.add(serviceDefinition.getService_definition_describe());
				}

			}

		}
		statisticsVO.setSheetHeadNameList(sheetHeadNameList);
		/*
		 * 
		 */
		return statisticsVO;
	}

	public StatisticsDao getStatisticsDao() {
		return statisticsDao;
	}

	public void setStatisticsDao(StatisticsDao statisticsDao) {
		this.statisticsDao = statisticsDao;
	}

	public UnitDao getUnitDao() {
		return unitDao;
	}

	public void setUnitDao(UnitDao unitDao) {
		this.unitDao = unitDao;
	}

	public ServiceService getServiceService() {
		return serviceService;
	}

	public void setServiceService(ServiceService serviceService) {
		this.serviceService = serviceService;
	}

}
