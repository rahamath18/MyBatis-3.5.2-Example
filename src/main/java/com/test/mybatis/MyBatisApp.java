package com.test.mybatis;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisApp {

	public static void main(String[] args) throws IOException, SQLException {
		
		Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);		
		SqlSession session = sqlSessionFactory.openSession();
		
		insertUser(session);
		
		fetchAllUser(session);
		fetchUserById(session, 2);
		fetchUserById(session, 3);
		
		updateUser(session);
		fetchAllUser(session);

		deleteUserById(session, 2);
		fetchAllUser(session);

		userStoredProcedure(session);

		dynamicSql(session, "yahoo");
		
		session.close();

	}
 

	private static void dynamicSql(SqlSession session, String likeEmail) {
		try {
			System.out.println("Using MyBatis - Dynamic SQL...");
			User user = new User();
			user.setId(3);

			List<User> userList = session.selectList("User.getUsersByEmail", likeEmail);
			System.out.println("Using MyBatis - Dynamic SQL..." + userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void userStoredProcedure(SqlSession session) {
		try {
			System.out.println("Using MyBatis - Stored Procedures...");
			User user = (User) session.selectOne("User.callByUserId", 3);
			System.out.println("Using MyBatis - Stored Procedures..." + user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void deleteUserById(SqlSession session, int id) {
		try {
			System.out.println("Deleting In Table...");

			session.delete("User.deleteUserById", id);

			System.out.println("User Deleted Successfully ");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void updateUser(SqlSession session) {
		try {
			System.out.println("Updating In Table...");
			List<User> userList = fetchAllUser(session);
			for (User user : userList) {
				user.setLastName(user.getLastName() + " - Updated");
				user.setEmail("Updated" + "-"  + user.getEmail());
				session.update("User.updateUser", user);
			}
			session.commit();
			System.out.println("User Updated Successfully ");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	private static void fetchUserById(SqlSession session,int id) {
		try {
			System.out.println("Fetching By User Id From Table...");

			User user = session.selectOne("User.getUserById", id);
			System.out.println("User by id : " + user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static List<User> fetchAllUser(SqlSession session) {
		try {
			System.out.println("Fetching All Users From Table...");

			List<User> userList = session.selectList("User.getAllUsers");
			for (User user : userList) {
				System.out.println(user);
			}
			
			return userList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<User>();
	}

	private static void insertUser(SqlSession session) {
		try {
			System.out.println("Inserting Into Table...");
			
			User newUser = new User("Rahamath", "S", "rahamath18@yahoo.com");
			System.out.println("Before Save : " + newUser);
			session.insert("User.insertUser", newUser);
			System.out.println("After Save : " + newUser);
			

			for (int i = 2; i <= 3; i++) {
				User user = new User("User-" + i, "U-" + i, "user-" + i + "@info.com");
				session.insert("User.insertUser", user);
			}
			session.commit();
			System.out.println("Record Inserted Successfully ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
