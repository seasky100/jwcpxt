package com.pphgzs.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.pphgzs.dao.LoginAndLogoutDao;
import com.pphgzs.domain.DO.jwcpxt_admin;
import com.pphgzs.domain.DO.jwcpxt_user;

public class LoginAndLogoutDaoImpl implements LoginAndLogoutDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public jwcpxt_user getUserByAccount(String account) {
		jwcpxt_user user = null;
		Session session = getSession();
		String hql = "from jwcpxt_user where user_account='" + account + "'";
		Query query = session.createQuery(hql);
		user = (jwcpxt_user) query.uniqueResult();
		session.clear();
		return user;

	}

	@Override
	public jwcpxt_admin getAdminByAccount(String account) {
		jwcpxt_admin admin = null;
		Session session = getSession();
		String hql = "from jwcpxt_admin where admin_account='" + account + "'";
		Query query = session.createQuery(hql);
		admin = (jwcpxt_admin) query.uniqueResult();
		session.clear();
		return admin;
	}

}
