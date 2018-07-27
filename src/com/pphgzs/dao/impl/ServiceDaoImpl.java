package com.pphgzs.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.pphgzs.dao.ServiceDao;
import com.pphgzs.domain.DO.jwcpxt_grab_instance;
import com.pphgzs.domain.DO.jwcpxt_grab_journal;
import com.pphgzs.domain.DO.jwcpxt_option;
import com.pphgzs.domain.DO.jwcpxt_question;
import com.pphgzs.domain.DO.jwcpxt_service_client;
import com.pphgzs.domain.DO.jwcpxt_service_definition;
import com.pphgzs.domain.DO.jwcpxt_service_grab;
import com.pphgzs.domain.DO.jwcpxt_service_instance;
import com.pphgzs.domain.DO.jwcpxt_unit;
import com.pphgzs.domain.DO.jwcpxt_unit_service;
import com.pphgzs.domain.DO.jwcpxt_user;
import com.pphgzs.domain.DTO.ClientInfoDTO;
import com.pphgzs.domain.DTO.ClientInstanceDTO;
import com.pphgzs.domain.DTO.ClientNotSatisfiedQusetionAndOptionDTO;
import com.pphgzs.domain.DTO.ServiceConnectDTO;
import com.pphgzs.domain.DTO.ServiceDefinitionDTO;
import com.pphgzs.domain.VO.ClientInfoVO;
import com.pphgzs.domain.VO.CountFinishReturnVisitVo;
import com.pphgzs.domain.VO.ServiceDefinitionVO;
import com.pphgzs.domain.VO.ServiceInstanceVO;
import com.pphgzs.util.TimeUtil;

public class ServiceDaoImpl implements ServiceDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	/**
	 * 测评员列表
	 */
	@Override
	public List<jwcpxt_user> list_userDO() {
		Session session = getSession();
		String hql = "from jwcpxt_user where user_type = 1 and user_state = 1";
		Query query = session.createQuery(hql);
		//
		List<jwcpxt_user> list = query.list();
		session.clear();
		return list;
	}

	/**
	 * 根据Vo获取数量
	 */
	@Override
	public int get_clientInfoVOCount_byUserId(ClientInfoVO clientInfoVO) {
		int count = 0;
		Session session = getSession();
		String hql = "select count(*) from " + "jwcpxt_service_instance serviceInstance,"
				+ "jwcpxt_service_client serviceClient," + "jwcpxt_service_definition serviceDefinition,"
				+ "jwcpxt_unit unit," + "jwcpxt_user _user where "
				+ "serviceClient.service_client_service_instance = serviceInstance.jwcpxt_service_instance_id and "
				+ "serviceInstance.service_instance_service_definition = serviceDefinition.jwcpxt_service_definition_id and "
				+ "serviceInstance.service_instance_judge = _user.jwcpxt_user_id and "
				+ "serviceInstance.service_instance_belong_unit = unit.jwcpxt_unit_id and serviceInstance.service_instance_date >= :startTime and "
				+ "serviceInstance.service_instance_date <= :endTime and serviceDefinition.jwcpxt_service_definition_id like :screenService and "
				+ "serviceClient.service_client_visit like :screenVisit and _user.jwcpxt_user_id like :screenUser and "
				+ "(serviceClient.service_client_name like :search or serviceClient.service_client_phone like :search or "
				+ "unit.unit_name like :search) order by serviceClient.service_client_visit desc,serviceClient.service_client_gmt_create desc";
		Query query = session.createQuery(hql);
		if (clientInfoVO.getStartTime().equals("")) {
			query.setParameter("startTime", "%%");
		} else {
			query.setParameter("startTime", clientInfoVO.getStartTime());
		}
		if (clientInfoVO.getEndTime().equals("")) {
			query.setParameter("endTime", "%%");
		} else {
			query.setParameter("endTime", clientInfoVO.getEndTime());
		}
		if (clientInfoVO.getScreenService().equals("")) {
			query.setParameter("screenService", "%%");
		} else {
			query.setParameter("screenService", clientInfoVO.getScreenService());
		}
		if (clientInfoVO.getScreenVisit().equals("")) {
			query.setParameter("screenVisit", "%%");
		} else {
			query.setParameter("screenVisit", clientInfoVO.getScreenVisit());
		}
		if (clientInfoVO.getScreenUser().equals("")) {
			query.setParameter("screenUser", "%%");
		} else {
			query.setParameter("screenUser", clientInfoVO.getScreenUser());
		}
		if (clientInfoVO.getSearch().equals("")) {
			query.setParameter("search", "%%");
		} else {
			query.setParameter("search", "%" + clientInfoVO.getSearch() + "%");
		}
		if (query.uniqueResult() == null) {
			return 0;
		}
		count = ((Number) query.uniqueResult()).intValue();
		session.clear();
		return count;
	}

	/**
	 * 根据VO获取当事人列表
	 */
	@Override
	public List<ClientInfoDTO> get_clientInfoVO_byUserId(ClientInfoVO clientInfoVO) {
		List<ClientInfoDTO> listClientInfo = new ArrayList<>();
		Session session = getSession();
		String hql = "select new com.pphgzs.domain.DTO.ClientInfoDTO(serviceClient,serviceInstance,serviceDefinition,_user,unit) from "
				+ "jwcpxt_service_instance serviceInstance," + "jwcpxt_service_client serviceClient,"
				+ "jwcpxt_service_definition serviceDefinition," + "jwcpxt_unit unit," + "jwcpxt_user _user where "
				+ "serviceClient.service_client_service_instance = serviceInstance.jwcpxt_service_instance_id and "
				+ "serviceInstance.service_instance_service_definition = serviceDefinition.jwcpxt_service_definition_id and "
				+ "serviceInstance.service_instance_judge = _user.jwcpxt_user_id and "
				+ "serviceInstance.service_instance_belong_unit = unit.jwcpxt_unit_id and serviceInstance.service_instance_date >= :startTime and "
				+ "serviceInstance.service_instance_date <= :endTime and serviceDefinition.jwcpxt_service_definition_id like :screenService and "
				+ "serviceClient.service_client_visit like :screenVisit and _user.jwcpxt_user_id like :screenUser and "
				+ "(serviceClient.service_client_name like :search or serviceClient.service_client_phone like :search or "
				+ "unit.unit_name like :search) order by serviceClient.service_client_visit desc,serviceClient.service_client_gmt_create desc";
		Query query = session.createQuery(hql);
		System.out.println("startTime" + clientInfoVO.getStartTime());
		System.out.println("endTime" + clientInfoVO.getEndTime());
		System.out.println("screenService" + clientInfoVO.getScreenService());
		System.out.println("screenVisit" + clientInfoVO.getScreenVisit());
		System.out.println("screenUser" + clientInfoVO.getScreenUser());
		System.out.println("search" + clientInfoVO.getSearch());
		if (clientInfoVO.getStartTime().equals("")) {
			query.setParameter("startTime", "0000-00-00");
		} else {
			query.setParameter("startTime", clientInfoVO.getStartTime());
		}
		if (clientInfoVO.getEndTime().equals("")) {
			query.setParameter("endTime", "9999-99-99");
		} else {
			query.setParameter("endTime", clientInfoVO.getEndTime());
		}
		if (clientInfoVO.getScreenService().equals("")) {
			query.setParameter("screenService", "%%");
		} else {
			query.setParameter("screenService", clientInfoVO.getScreenService());
		}
		if (clientInfoVO.getScreenVisit().equals("")) {
			query.setParameter("screenVisit", "%%");
		} else {
			query.setParameter("screenVisit", clientInfoVO.getScreenVisit());
		}
		if (clientInfoVO.getScreenUser().equals("")) {
			query.setParameter("screenUser", "%%");
		} else {
			query.setParameter("screenUser", clientInfoVO.getScreenUser());
		}
		if (clientInfoVO.getSearch().equals("")) {
			query.setParameter("search", "%%");
		} else {
			query.setParameter("search", "%" + clientInfoVO.getSearch() + "%");
		}
		System.out.println(hql);
		query.setFirstResult((clientInfoVO.getCurrPage() - 1) * clientInfoVO.getPageSize());
		query.setMaxResults(clientInfoVO.getPageSize());
		listClientInfo = query.list();
		//
		session.clear();
		for (ClientInfoDTO clientInfoDTO : listClientInfo) {
			if (!clientInfoVO.getSearch().equals("")) {
				// 当事人姓名
				clientInfoDTO.getServiceClient()
						.setService_client_name(clientInfoDTO.getServiceClient().getService_client_name().replaceAll(
								clientInfoVO.getSearch(), "<span style='color: #ff5063;'>" + clientInfoVO.getSearch()
										+ "</span>"));
				// 性别
				clientInfoDTO.getServiceClient()
						.setService_client_phone(clientInfoDTO.getServiceClient().getService_client_phone().replaceAll(
								clientInfoVO.getSearch(), "<span style='color: #ff5063;'>" + clientInfoVO.getSearch()
										+ "</span>"));
				// 单位名称
				clientInfoDTO.getUnit()
						.setUnit_name(clientInfoDTO.getUnit().getUnit_name().replaceAll(clientInfoVO.getSearch(),
								"<span style='color: #ff5063;'>" + clientInfoVO.getSearch() + "</span>"));
			}
		}
		return listClientInfo;
	}

	/**
	 * 
	 */
	@Override
	public jwcpxt_service_client get_serviceClientDo_byId(String serviceClientId) {
		Session session = getSession();
		String hql = "from jwcpxt_service_client where jwcpxt_service_client_id=:serviceClientId ";
		//
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceClientId", serviceClientId);
		//
		jwcpxt_service_client jwcpxt_service_client = (jwcpxt_service_client) query.uniqueResult();
		session.clear();
		return jwcpxt_service_client;
	}

	@Override
	public ClientInstanceDTO get_notServiceClientDTO_byJudge_revisit(String jwcpxt_user_id) {
		List<ClientInstanceDTO> listClientInstanceDTO = new ArrayList<>();
		Session session = getSession();
		String hql = "select "//
				+ " new com.pphgzs.domain.DTO.ClientInstanceDTO(serviceInstance,serviceClient,serviceDefinition,unit) "//
				+ " from "//
				+ " jwcpxt_service_instance serviceInstance , "//
				+ " jwcpxt_service_client serviceClient , "//
				+ " jwcpxt_service_definition serviceDefinition , "//
				+ " jwcpxt_unit unit"//
				+ " where "//
				+ " serviceInstance.jwcpxt_service_instance_id = serviceClient.service_client_service_instance "//
				+ " and serviceInstance.service_instance_service_definition = serviceDefinition.jwcpxt_service_definition_id "//
				+ " and serviceInstance.service_instance_belong_unit = unit.jwcpxt_unit_id"//
				+ " and serviceClient.service_client_visit = '2' "//
				+ " and serviceInstance.service_instance_judge= :userId "//
				+ " and serviceInstance.service_instance_service_definition = 'revisit' "//
				+ " order by "//
				+ " serviceInstance.service_instance_gmt_create "//
				+ " asc ";
		Query query = session.createQuery(hql);
		query.setParameter("userId", jwcpxt_user_id);
		query.setMaxResults(1);
		//
		listClientInstanceDTO = query.list();
		if (listClientInstanceDTO.size() <= 0) {
			return null;
		}
		return listClientInstanceDTO.get(0);
	}

	/**
	 * 获取ClientInstanceDTO内容
	 */
	@Override
	public ClientInstanceDTO get_notServiceClientDTO_byJudge_general(String userId) {
		List<ClientInstanceDTO> listClientInstanceDTO = new ArrayList<>();
		Session session = getSession();
		String hql = "select "//
				+ " new com.pphgzs.domain.DTO.ClientInstanceDTO(serviceInstance,serviceClient,serviceDefinition,unit) "//
				+ " from "//
				+ " jwcpxt_service_instance serviceInstance , "//
				+ " jwcpxt_service_client serviceClient , "//
				+ " jwcpxt_service_definition serviceDefinition , "//
				+ " jwcpxt_unit unit"//
				+ " where "//
				+ " serviceInstance.jwcpxt_service_instance_id = serviceClient.service_client_service_instance "//
				+ " and serviceInstance.service_instance_service_definition = serviceDefinition.jwcpxt_service_definition_id "//
				+ " and serviceInstance.service_instance_belong_unit = unit.jwcpxt_unit_id"//
				+ " and serviceClient.service_client_visit= '2' "//
				+ " and serviceInstance.service_instance_judge= :userId "//
				+ " and serviceInstance.service_instance_service_definition != 'revisit' "//
				+ " order by "//
				+ " serviceInstance.service_instance_gmt_create "//
				+ " asc ";
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		query.setMaxResults(1);
		//
		listClientInstanceDTO = query.list();
		if (listClientInstanceDTO.size() <= 0) {
			return null;
		}
		return listClientInstanceDTO.get(0);
	}

	@Override
	public int get_serviceInstanceCount_byServiceDefinitionAndUnit(String serviceDefinitionID, String unitID) {
		Session session = getSession();
		String hql = " select "//
				+ " count(*) "//
				+ " from "//
				+ " jwcpxt_service_instance "//
				+ " where "//
				+ " service_instance_service_definition = :serviceDefinitionID "//
				+ " and service_instance_belong_unit = :unitID "//
				+ " and service_instance_gmt_create >= :date ";
		Query query = session.createQuery(hql);
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		query.setParameter("unitID", unitID);
		query.setParameter("date", TimeUtil.getStringDay());
		//
		int count = ((Number) query.uniqueResult()).intValue();
		//
		session.clear();
		return count;
	}

	@Override
	public int get_serviceInstanceCount_byServiceDefinitionAndFatherUnitID(String serviceDefinitionID,
			String fatherUnitID) {
		Session session = getSession();
		String hql = " select "//
				+ " count(*) "//
				+ " from "//
				+ " jwcpxt_service_instance serviceInstance , "//
				+ " jwcpxt_unit unit"//
				+ " where "//
				+ " serviceInstance.service_instance_belong_unit = unit.jwcpxt_unit_id "//
				//
				+ " and serviceInstance.service_instance_service_definition = :serviceDefinitionID "//
				+ " and unit.unit_father = :fatherUnitID "//
				+ " and serviceInstance.service_instance_gmt_create >= :date ";
		Query query = session.createQuery(hql);
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		query.setParameter("fatherUnitID", fatherUnitID);
		query.setParameter("date", TimeUtil.getStringDay());
		//
		int count = ((Number) query.uniqueResult()).intValue();
		//
		session.clear();
		return count;
	}

	/**
	 * 根据业务定义id获取抓取
	 */
	@Override
	public List<jwcpxt_service_grab> list_serviceGrab_byServiceDefinitionId(String serviceDefinitionId) {
		Session session = getSession();
		String hql = "from jwcpxt_service_grab where service_grab_service_definition = :serviceDefinitionId";
		Query query = session.createQuery(hql);
		query.setParameter("serviceDefinitionId", serviceDefinitionId);
		//
		List<jwcpxt_service_grab> list = query.list();
		session.clear();
		return list;
	}

	@Override
	public List<jwcpxt_service_definition> list_serviceDefinitionDOList_byUnitID(String unitID) {
		Session session = getSession();
		String hql = "select "//
				+ " serviceDefinition "//
				+ " from "//
				+ " jwcpxt_unit_service unitService , "//
				+ " jwcpxt_service_definition serviceDefinition "//
				+ " where "//
				+ " unitService.service_definition_id = serviceDefinition.jwcpxt_service_definition_id"//
				+ " and unitService.unit_id = :unitID"//
		;
		Query query = session.createQuery(hql);
		query.setParameter("unitID", unitID);
		System.out.println("hql:" + hql);
		System.out.println("unitID：" + unitID);
		//
		List<jwcpxt_service_definition> list = query.list();
		session.clear();
		return list;
	}

	/**
	 * 删除抓取记录
	 */
	@Override
	public boolean delete_serviceGrab_byServiceGrabId(jwcpxt_service_grab serviceGrab) {
		Session session = getSession();
		String hql = "delete from jwcpxt_service_grab where jwcpxt_service_grab_id=:serviceGrabId";
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceGrabId", serviceGrab.getJwcpxt_service_grab_id());
		//
		query.executeUpdate();
		session.flush();
		return true;
	}

	/**
	 * 保存
	 * 
	 * @param obj
	 */
	@Override
	public void saveOrUpdateObject(Object obj) {
		Session session = getSession();
		session.saveOrUpdate(obj);
		session.flush();
	}

	/**
	 * 根据id获取业务单位关联表
	 */
	@Override
	public jwcpxt_unit_service get_unitService_byUnitServiceId(String unitServiceId) {
		Session session = getSession();
		String hql = "from "//
				+ " jwcpxt_unit_service "//
				+ " where "//
				+ " jwcpxt_unit_service_id=:unitServiceId "//
		;
		//
		Query query = session.createQuery(hql);
		//
		query.setParameter("unitServiceId", unitServiceId);
		//
		jwcpxt_unit_service jwcpxt_unit_service = (jwcpxt_unit_service) query.uniqueResult();
		session.clear();
		return jwcpxt_unit_service;
	}

	@Override
	public List<ServiceConnectDTO> list_serviceDefinitionDTO_connectService(String unitId) {
		Session session = getSession();
		String hql = "select "//
				+ " new com.pphgzs.domain.DTO.ServiceConnectDTO(serviceDefinition,unitSer) "//
				+ " from jwcpxt_service_definition serviceDefinition , "//
				+ "jwcpxt_unit_service unitSer "//
				+ " where "//
				+ "serviceDefinition.jwcpxt_service_definition_id = unitSer.service_definition_id "//
				+ " and unitSer.unit_id = :unitID "//
				+ " order by "//
				+ " serviceDefinition.service_definition_gmt_create desc"//
		;
		Query query = session.createQuery(hql);
		query.setParameter("unitID", unitId);
		//
		List<ServiceConnectDTO> list = query.list();
		session.clear();
		return list;
	}

	/**
	 * 获取未关联某单位的所有业务
	 */
	@Override
	public List<jwcpxt_service_definition> list_serviceDefinition_notConnectService(String unitId) {
		Session session = getSession();
		String hql = "select serviceDefinition from jwcpxt_service_definition serviceDefinition where "
				+ " serviceDefinition.jwcpxt_service_definition_id not in (select serVice.service_definition_id from jwcpxt_unit_service serVice where serVice.unit_id=:unitId) order by serviceDefinition.service_definition_gmt_create ";
		Query query = session.createQuery(hql);
		query.setParameter("unitId", unitId);
		//
		List<jwcpxt_service_definition> list = query.list();
		session.clear();
		return list;
	}

	/**
	 * 业务定义列表
	 */
	@Override
	public List<jwcpxt_service_definition> list_serviceDefinitionDO_all() {
		Session session = getSession();
		String hql = "from jwcpxt_service_definition ";
		Query query = session.createQuery(hql);
		//
		List<jwcpxt_service_definition> list = query.list();
		session.clear();
		return list;
	}

	@Override
	public int get_serviceInstanceTotalCount_byServiceInstanceVO(ServiceInstanceVO serviceInstanceVO) {
		Session session = getSession();
		String hql = "select count(*) from jwcpxt_service_instance "
				+ " where service_instance_judge like :screenServiceInstanceJudge "
				+ " and service_instance_service_definition like :screenServiceDefinition"
				+ " and service_instance_date >= :screenServiceInstanceStartDate "
				+ " and service_instance_date <= :screenServiceInstanceStopDate ";
		Query query = session.createQuery(hql);
		//
		if (serviceInstanceVO.getScreenServiceInstanceJudge().equals("")) {
			query.setParameter("screenServiceInstanceJudge", "%%");
		} else {
			query.setParameter("screenServiceInstanceJudge",
					"%" + serviceInstanceVO.getScreenServiceInstanceJudge() + "%");
		}
		if (serviceInstanceVO.getScreenServiceDefinition().equals("")) {
			query.setParameter("screenServiceDefinition", "%%");
		} else {
			query.setParameter("screenServiceDefinition", "%" + serviceInstanceVO.getScreenServiceDefinition() + "%");
		}
		if (serviceInstanceVO.getScreenServiceInstanceStartDate().equals("")) {
			query.setParameter("screenServiceInstanceStartDate", "0000-00-00");
		} else {
			query.setParameter("screenServiceInstanceStartDate", serviceInstanceVO.getScreenServiceInstanceStartDate());
		}
		if (serviceInstanceVO.getScreenServiceInstanceStopDate().equals("")) {
			query.setParameter("screenServiceInstanceStopDate", "9999-99-99");
		} else {
			query.setParameter("screenServiceInstanceStopDate", serviceInstanceVO.getScreenServiceInstanceStopDate());
		}
		//
		int count = ((Number) query.uniqueResult()).intValue();
		//
		session.clear();
		return count;
	}

	@Override
	public int get_serviceInstanceDistributionCount_byDateAndServiceDefinitionID(String date,
			String serviceDefinitionID) {
		Session session = getSession();
		String hql = "select "//
				+ " count(*) "//
				+ " from "//
				+ " jwcpxt_service_instance "//
				+ " where "//
				+ " service_instance_service_definition = :serviceDefinitionID "//
				+ " and service_instance_gmt_create >= :date "//
				+ " and service_instance_judge != 'none' ";
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		query.setParameter("date", date);
		//
		int count = ((Number) query.uniqueResult()).intValue();
		//
		session.clear();
		return count;
	}

	@Override
	public int get_serviceInstanceTotalCount_byDateAndServiceDefinitionID(String date, String serviceDefinitionID) {
		Session session = getSession();
		String hql = "select count(*) from jwcpxt_service_instance "
				+ " where service_instance_service_definition = :serviceDefinitionID "
				+ " and service_instance_gmt_create >= :date ";
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		query.setParameter("date", date);
		//
		int count = ((Number) query.uniqueResult()).intValue();
		//
		session.clear();
		return count;
	}

	@Override
	public int get_serviceInstanceTotalCount_byToday() {
		Session session = getSession();
		String hql = "select count(*) "//
				+ "from jwcpxt_service_instance  "//
				+ " where  service_instance_gmt_create >= :date";
		Query query = session.createQuery(hql);
		//
		query.setParameter("date", TimeUtil.getStringDay());
		//
		int count = ((Number) query.uniqueResult()).intValue();
		//
		session.clear();
		return count;
	}

	@Override
	public int get_serviceDefinitionTotalCount_byServiceDefinitionVO(ServiceDefinitionVO serviceDefinitionVO) {
		Session session = getSession();
		String hql = "select count(*) from jwcpxt_service_definition serviceDefinition "
				+ " where serviceDefinition.service_definition_describe like :screenSearch and serviceDefinition.service_definition_unit like :screenUnit ";
		Query query = session.createQuery(hql);
		//
		if (serviceDefinitionVO.getScreenSearch().equals("")) {
			query.setParameter("screenSearch", "%%");
		} else {
			query.setParameter("screenSearch", "%" + serviceDefinitionVO.getScreenSearch() + "%");
		}
		if (serviceDefinitionVO.getScreenUnit().equals("")) {
			query.setParameter("screenUnit", "%%");
		} else {
			query.setParameter("screenUnit", "%" + serviceDefinitionVO.getScreenUnit() + "%");
		}
		//
		int count = ((Number) query.uniqueResult()).intValue();
		//
		session.clear();
		return count;
	}

	@Override
	public List<jwcpxt_service_client> list_client_byServiceInstanceID(String serviceInstanceID) {

		Session session = getSession();
		String hql = "from jwcpxt_service_client serviceClient where serviceClient.service_client_service_instance=:serviceInstanceID";
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceInstanceID", serviceInstanceID);
		//
		List<jwcpxt_service_client> list = query.list();
		session.clear();
		return list;
	}

	@Override
	public List<jwcpxt_service_instance> list_serviceInstance_byNoJudgeAndRandomAndNumAndServiceDefinitionIDAndDate(
			int num, String serviceDefinitionID, String date) {
		Session session = getSession();
		String hql = " from jwcpxt_service_instance "//
				+ " where service_instance_judge = 'none' "//
				+ " and service_instance_service_definition = :serviceInstanceID "//
				+ " and service_instance_gmt_create >= :date "//
				+ " order by rand() ";
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceInstanceID", serviceDefinitionID);
		query.setParameter("date", date);
		query.setMaxResults(num);
		//
		List<jwcpxt_service_instance> list = query.list();
		session.clear();
		return list;
	}

	@Override
	public List<jwcpxt_service_client> list_serviceClient_byServiceInstanceID(String serviceInstanceID) {
		Session session = getSession();
		String hql = "from jwcpxt_service_client serviceClient where serviceClient.service_client_service_instance = :serviceInstanceID";
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceInstanceID", serviceInstanceID);
		//
		List<jwcpxt_service_client> list = query.list();
		session.clear();
		return list;
	}

	@Override
	public List<jwcpxt_service_instance> list_serviceInstance_byServiceDefinitionID(String serviceDefinitionID) {
		Session session = getSession();
		String hql = "from jwcpxt_service_instance serviceInstance where serviceInstance.service_instance_service_definition=:serviceDefinitionID";
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		//
		List<jwcpxt_service_instance> list = query.list();
		session.clear();
		return list;
	}

	@Override
	public ServiceDefinitionDTO get_serviceDefinitionDTO_byServiceDefinitionID(String serviceDefinitionID) {
		Session session = getSession();
		String hql = "select new com.pphgzs.domain.DTO.ServiceDefinitionDTO(serviceDefinition) from jwcpxt_service_definition serviceDefinition"
				+ " where serviceDefinition.jwcpxt_service_definition_id = :serviceDefinitionID  ";
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		//
		ServiceDefinitionDTO serviceDefinitionDTO = (ServiceDefinitionDTO) query.uniqueResult();
		session.clear();
		return serviceDefinitionDTO;
	}

	@Override
	public List<jwcpxt_service_instance> list_serviceInstance_byServiceInstanceVO(ServiceInstanceVO serviceInstanceVO) {
		Session session = getSession();
		String hql = "from jwcpxt_service_instance " + " where service_instance_judge like :screenServiceInstanceJudge "
				+ " and service_instance_service_definition like :screenServiceDefinition"
				+ " and service_instance_date >= :screenServiceInstanceStartDate "
				+ " and service_instance_date <= :screenServiceInstanceStopDate "
				+ " order by service_instance_gmt_create";
		Query query = session.createQuery(hql);
		//
		if (serviceInstanceVO.getScreenServiceInstanceJudge().equals("")) {
			query.setParameter("screenServiceInstanceJudge", "%%");
		} else {
			query.setParameter("screenServiceInstanceJudge",
					"%" + serviceInstanceVO.getScreenServiceInstanceJudge() + "%");
		}
		if (serviceInstanceVO.getScreenServiceDefinition().equals("")) {
			query.setParameter("screenServiceDefinition", "%%");
		} else {
			query.setParameter("screenServiceDefinition", "%" + serviceInstanceVO.getScreenServiceDefinition() + "%");
		}
		if (serviceInstanceVO.getScreenServiceInstanceStartDate().equals("")) {
			query.setParameter("screenServiceInstanceStartDate", "0000-00-00");
		} else {
			query.setParameter("screenServiceInstanceStartDate", serviceInstanceVO.getScreenServiceInstanceStartDate());
		}
		if (serviceInstanceVO.getScreenServiceInstanceStopDate().equals("")) {
			query.setParameter("screenServiceInstanceStopDate", "9999-99-99");
		} else {
			query.setParameter("screenServiceInstanceStopDate", serviceInstanceVO.getScreenServiceInstanceStopDate());
		}
		query.setFirstResult((serviceInstanceVO.getCurrPage() - 1) * serviceInstanceVO.getPageSize());
		query.setMaxResults(serviceInstanceVO.getPageSize());
		//
		List<jwcpxt_service_instance> list = query.list();
		session.clear();
		return list;
	}

	@Override
	public List<ServiceDefinitionDTO> list_serviceDefinitionDTO_byServiceDefinitionVO(
			ServiceDefinitionVO serviceDefinitionVO) {
		Session session = getSession();
		String hql = "select "//
				+ " new com.pphgzs.domain.DTO.ServiceDefinitionDTO(serviceDefinition,unit) "//
				+ " from "//
				+ " jwcpxt_service_definition serviceDefinition ,"//
				+ " jwcpxt_unit unit" + " where "//
				+ " serviceDefinition.service_definition_unit=unit.jwcpxt_unit_id "//
				+ " and serviceDefinition.service_definition_describe like :screenSearch "//
				+ " and serviceDefinition.service_definition_unit like :screenUnit "//
				+ " order by service_definition_unit";
		Query query = session.createQuery(hql);
		//
		if (serviceDefinitionVO.getScreenSearch().equals("")) {
			query.setParameter("screenSearch", "%%");
		} else {
			query.setParameter("screenSearch", "%" + serviceDefinitionVO.getScreenSearch() + "%");
		}
		if (serviceDefinitionVO.getScreenUnit().equals("")) {
			query.setParameter("screenUnit", "%%");
		} else {
			query.setParameter("screenUnit", "%" + serviceDefinitionVO.getScreenUnit() + "%");
		}
		query.setFirstResult((serviceDefinitionVO.getCurrPage() - 1) * serviceDefinitionVO.getPageSize());
		query.setMaxResults(serviceDefinitionVO.getPageSize());
		//
		List<ServiceDefinitionDTO> list = null;
		list = query.list();
		session.clear();
		return list;
	}

	@Override
	public boolean ifExist_serviceDefinition_byServiceDefinitionDescribe(String service_definition_describe) {
		Session session = getSession();

		String hql = "from jwcpxt_service_definition where service_definition_describe=:definitionDescribe";
		Query query = session.createQuery(hql);
		//
		query.setParameter("definitionDescribe", service_definition_describe);
		//
		jwcpxt_service_definition serviceDefinition = (jwcpxt_service_definition) query.uniqueResult();
		session.clear();
		if (serviceDefinition != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public jwcpxt_grab_instance get_grabInstance_byServiceDefinitionIDAndFatherOrganizationCode_notDistribution_random(
			String serviceDefinitionID, String organizationCode) {
		Session session = getSession();
		String hql = "select grabInstance "//
				+ " from "//
				+ " jwcpxt_grab_instance grabInstance , "//
				+ " jwcpxt_unit unit , "//
				+ " jwcpxt_unit fatherUnit "//
				//
				+ " where "//
				+ " grabInstance.grab_instance_distribution='2' "//
				+ " and grabInstance.grab_instance_service_time >= :date "//
				//
				+ " and unit.unit_father=fatherUnit.jwcpxt_unit_id "// 连接二三级单位
				//
				+ " and fatherUnit.unit_num=:organizationCode "// 查出二级单位
				+ " and grabInstance.grab_instance_service_definition=:serviceDefinitionID "//
				+ " and grabInstance.grab_instance_organization_code=unit.unit_num "// 抓取实例的机构代码=三级单位的机构代码
				+ " order by rand() "//
		;
		Query query = session.createQuery(hql);
		//
		String date = TimeUtil.getStringDay_before7();
		date = date.replaceAll("-", "");
		//
		query.setParameter("date", date);
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		query.setParameter("organizationCode", organizationCode);
		//
		query.setMaxResults(1);
		//
		List<jwcpxt_grab_instance> list = query.list();
		session.clear();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public jwcpxt_grab_instance get_grabInstance_byServiceDefinitionIDAndOrganizationCode_notDistribution_random(
			String serviceDefinitionID, String organizationCode) {
		Session session = getSession();
		String hql = "select grabInstance "//
				+ " from "//
				+ " jwcpxt_grab_instance grabInstance "//
				//
				+ " where "//
				+ " grabInstance.grab_instance_distribution = '2' "//
				+ " and grabInstance.grab_instance_service_time >= :date "//
				//
				+ " and grabInstance.grab_instance_organization_code=:organizationCode "//
				+ " and grabInstance.grab_instance_service_definition=:serviceDefinitionID "//

				+ " order by rand() "//
		;
		Query query = session.createQuery(hql);
		//
		String date = TimeUtil.getStringDay_before7();
		date = date.replaceAll("-", "");
		//
		query.setParameter("date", date);
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		query.setParameter("organizationCode", organizationCode);
		//
		query.setMaxResults(1);
		//
		List<jwcpxt_grab_instance> list = query.list();
		session.clear();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	public jwcpxt_grab_journal get_grabJournal_byServiceDefinitionIDAndDate(String serviceDefinitionID, String date) {
		Session session = getSession();

		String hql = "from jwcpxt_grab_journal"//
				+ " where grab_journal_service_definition=:serviceDefinitionID "//
				+ " and grab_journal_gmt_create >= :date";
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		query.setParameter("date", date);
		//
		jwcpxt_grab_journal grabJournal = (jwcpxt_grab_journal) query.uniqueResult();
		session.clear();
		return grabJournal;
	}

	@Override
	public jwcpxt_service_definition get_serviceDefinition_byServiceDefinitionID(String serviceDefinitionID) {
		Session session = getSession();
		String hql = "from jwcpxt_service_definition where jwcpxt_service_definition_id=:serviceDefinitionID ";
		//
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		//
		jwcpxt_service_definition serviceDefinition = (jwcpxt_service_definition) query.uniqueResult();
		session.clear();
		return serviceDefinition;
	}

	@Override
	public jwcpxt_service_grab get_serviceGrabDO_byServiceDefinitionID(String serviceDefinitionID) {
		Session session = getSession();
		String hql = "from jwcpxt_service_grab where service_grab_service_definition=:serviceDefinitionID ";
		//
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceDefinitionID", serviceDefinitionID);
		//
		jwcpxt_service_grab service_grab = (jwcpxt_service_grab) query.uniqueResult();
		session.clear();
		return service_grab;
	}

	@Override
	public jwcpxt_service_grab get_serviceGrab_byServiceGrabID(String serviceGrabId) {
		Session session = getSession();
		String hql = "from jwcpxt_service_grab where jwcpxt_service_grab_id=:serviceGrabId ";
		//
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceGrabId", serviceGrabId);
		//
		jwcpxt_service_grab serviceInstance = (jwcpxt_service_grab) query.uniqueResult();
		session.clear();
		return serviceInstance;
	}

	@Override
	public jwcpxt_service_instance get_serviceInstance_byServiceInstanceID(String serviceInstanceID) {
		Session session = getSession();
		String hql = "from jwcpxt_service_instance where jwcpxt_service_instance_id=:serviceInstanceID ";
		//
		Query query = session.createQuery(hql);
		//
		query.setParameter("serviceInstanceID", serviceInstanceID);
		//
		jwcpxt_service_instance serviceInstance = (jwcpxt_service_instance) query.uniqueResult();
		session.clear();
		return serviceInstance;
	}

	@Override
	public boolean update_grabInstance(jwcpxt_grab_instance grabInstance) {
		Session session = getSession();
		session.update(grabInstance);
		session.flush();
		return true;
	}

	@Override
	public boolean update_serviceInstance(jwcpxt_service_instance serviceInstance) {
		Session session = getSession();
		session.update(serviceInstance);
		session.flush();
		return true;
	}

	@Override
	public boolean update_serviceDefinition(jwcpxt_service_definition serviceDefinitionOld) {
		Session session = getSession();
		session.update(serviceDefinitionOld);
		session.flush();
		return true;
	}

	@Override
	public boolean update_serviceGrab(jwcpxt_service_grab serviceGrabOld) {
		Session session = getSession();
		session.update(serviceGrabOld);
		session.flush();
		return true;
	}

	@Override
	public boolean save_grabJournal(jwcpxt_grab_journal grabJournal) {
		Session session = getSession();
		session.save(grabJournal);
		session.flush();
		return true;
	}

	@Override
	public boolean save_serviceGrab(jwcpxt_service_grab serviceGrab) {
		Session session = getSession();
		session.save(serviceGrab);
		session.flush();
		return true;

	}

	@Override
	public boolean save_serviceDefinition(jwcpxt_service_definition serviceDefinition) {
		Session session = getSession();
		session.save(serviceDefinition);
		session.flush();
		return true;
	}

	@Override
	public jwcpxt_unit get_unitDo_byOrginaiId(String orginId) {
		Session session = getSession();
		String hql = "from jwcpxt_unit where unit_num = '" + orginId + "'";
		Query query = session.createQuery(hql);
		jwcpxt_unit newUnit = (jwcpxt_unit) query.uniqueResult();
		session.clear();
		return newUnit;
	}

	@Override
	public int get_countFinishReturnVisit_inDateAndByUserId(CountFinishReturnVisitVo countFinishReturnVisitVo) {
		String appraisalId = countFinishReturnVisitVo.getAppraisalId();
		String beginTime = countFinishReturnVisitVo.getBeginTime();
		String endTime = countFinishReturnVisitVo.getEndTime();
		String countType = countFinishReturnVisitVo.getCountType();
		String type = countFinishReturnVisitVo.getType();
		Session session = getSession();
		String hql = "select count(*)" + " from jwcpxt_grab_instance instance,jwcpxt_service_client client "// like用来匹配所有id
				+ " where instance.service_instance_judge like :appraisalId "//
				+ " and instance.service_instance_gmt_modified >= :beginTime "//
				+ " and instance.service_instance_gmt_modified <= :endTime "
				+ " and client.service_client_visit like :type ";//
		Query query = session.createQuery(hql);
		// 单个查询
		appraisalId = (!"".equals(appraisalId) && appraisalId != null) ? appraisalId : "%";
		// 此时开始时间和结束时间将无效
		if ("all".equals(countType)) {
			beginTime = "0000-00-00";
			endTime = "9999-99-99";
		} else {
			// 如果结束时间为空，则只做开始时间的查询
			if ("".equals(endTime) || endTime == null) {
				endTime = beginTime;
			}
		}
		query.setParameter("appraisalId", appraisalId);
		query.setParameter("beginTime", beginTime);
		query.setParameter("endTime", endTime);
		// 如果为-1，则获取全部状态的数量
		if ("-1".equals(type)) {
			type = "%";
		}
		query.setParameter("type", type);
		int count = ((Number) query.uniqueResult()).intValue();
		session.clear();
		return count;
	}

	@Override
	public List<jwcpxt_question> get_AllQuestion_ByServiceId(String jwcpxt_service_definition_id) {
		Session session = getSession();
		String hql = "from jwcpxt_question where question_service_definition = '" + jwcpxt_service_definition_id + "'";
		Query query = session.createQuery(hql);
		List<jwcpxt_question> allQuestion = query.list();
		session.clear();
		return allQuestion;
	}

	@Override
	public String get_ClientAnswer_ByQuestionAndClientId(jwcpxt_question question, String jwcpxt_service_client_id) {
		Session session = getSession();
		String hql = null;
		switch (question.getQuestion_type()) {
		case "4":
		case "1":
			hql = " select option.option_describe " + " from jwcpxt_answer_choice choice,jwcpxt_option option "
					+ " where choice.answer_choice_client = :jwcpxt_service_client_id "
					+ " and choice.answer_choice_question = :question "
					+ " and option.jwcpxt_option_id = choice.answer_choice_option";
			break;
		case "3":
		case "2":
			hql = " select open.answer_open_content " + " from jwcpxt_answer_open open "
					+ " where open.answer_open_client = :jwcpxt_service_client_id "
					+ " and open.answer_open_question = :question ";
			break;
		default:
			break;
		}
		System.out.println(hql);
		Query query = session.createQuery(hql);
		query.setParameter("jwcpxt_service_client_id", jwcpxt_service_client_id);
		query.setParameter("question", question.getJwcpxt_question_id());
		String describe = (String) query.uniqueResult();
		session.clear();
		return describe;
	}

	@Override
	public List<jwcpxt_question> get_askQusetionList_ByQuestionAndClientId(jwcpxt_question question,
			String jwcpxt_service_client_id) {
		Session session = getSession();
		String hql = " select question "
				+ " from jwcpxt_answer_choice choice,jwcpxt_option option, jwcpxt_question question"
				+ " where choice.answer_choice_client = :jwcpxt_service_client_id "
				+ " and choice.answer_choice_question = :question "
				+ " and option.jwcpxt_option_id = choice.answer_choice_option "
				+ " and question.question_service_definition = option.jwcpxt_option_id";
		Query query = session.createQuery(hql);
		query.setParameter("jwcpxt_service_client_id", jwcpxt_service_client_id);
		query.setParameter("question", question.getJwcpxt_question_id());
		List<jwcpxt_question> askQuestion = query.list();
		session.clear();
		if (askQuestion.size() > 0) {
			return askQuestion;
		} else {
			return null;
		}
	}

	/*
	 * 
	 */

}
