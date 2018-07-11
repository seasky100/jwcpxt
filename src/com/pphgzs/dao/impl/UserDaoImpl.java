package com.pphgzs.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.pphgzs.dao.UserDao;
import com.pphgzs.domain.DO.jwcpxt_user;

public class UserDaoImpl implements UserDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public List<jwcpxt_user> list_user_all() {
		List<jwcpxt_user> userList = new ArrayList<jwcpxt_user>();

		Session session = getSession();
		String hql = "from jwcpxt_user";
		Query query = session.createQuery(hql);
		userList = query.list();
		session.clear();

		return userList;
	}

	@Override
	public boolean save_user(jwcpxt_user user) {
		Session session = getSession();
		session.save(user);
		session.flush();
		return true;
	}

	@Override
	public boolean update_user(jwcpxt_user user) {
		Session session = getSession();
		session.update(user);
		session.flush();
		return true;

	}

}
