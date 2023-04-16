package sos.rest.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import io.github.cdimascio.dotenv.Dotenv;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import sos.rest.models.*;

public class DB {
	private static Connection connection;

	public DB() {
		connectDB();
	}

	private static void connectDB() {
		Dotenv dotenv = Dotenv.load();
		if (connection == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver"); // Busca que está instanciado el driver
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			String url = "jdbc:mysql://" + dotenv.get("DB_HOST")+":"+dotenv.get("DB_PORT") + "/" + dotenv.get("DB_NAME"); // URL para la conexión

			try {
				connection = DriverManager.getConnection(url, dotenv.get("DB_USER"), dotenv.get("DB_PW")); // Conectamos con estos credenciales
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 *
	 * Retrieves a list of users from the database based on the given query
	 * parameter.
	 *
	 * If no query parameter is provided, retrieves all users.
	 *
	 * @param query The name filter to search for in the users table. If null or
	 *              empty, retrieves all users.
	 * @return A Response object containing the retrieved users in XML format, or an
	 *         error message if an SQL error occurs.
	 */
	public static Response getUsers(String query) {
		connectDB();
		String sql;
		PreparedStatement stmt;
		String result = "<?xml version=\"1.0\"?>\n";
		Status st = Status.OK;
		try {
			if (query == null || query.isEmpty()) {
				sql = "SELECT * FROM users";
				stmt = connection.prepareStatement(sql);
			} else {
				sql = "SELECT * FROM users WHERE name LIKE ?";
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, "%" + query + "%");
			}

			ResultSet rs = stmt.executeQuery();
			result += "<Users>\n";
			while (rs.next()) {
				Long uId = rs.getLong("user_id");
				String name = rs.getString("name");
				String email = "" + rs.getString("email");
				String age = "" + rs.getInt("age");

				result += "<user>\n"
						+ "<userId>" + uId + "</userId>\n"
						+ "<name>" + name + "</name>\n"
						+ "<email>" + email + "</email>\n"
						+ "<age>" + age + "</age>\n"
						+ "</user>\n";
			}
			stmt.close();
			rs.close();
			result += "</Users>";
		} catch (SQLException e) {
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		return Response.status(st).entity(result).build();
	}

	public static Response getUserById(Long userId) {
		connectDB();
		String sql = "SELECT * FROM users WHERE user_id = ?";
		PreparedStatement stmt;
		String result = "<?xml version=\"1.0\"?>\n";
		Status st = Status.OK;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, userId);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				st = Response.Status.NOT_FOUND;
				result += "<message>User with id " + userId + " not found</message>";
			} else {
				String name = rs.getString("name");
				String email = "" + rs.getString("email");
				String age = "" + rs.getInt("age");

				result += "<user>\n"
						+ "<userId>" + userId + "</userId>\n"
						+ "<name>" + name + "</name>\n"
						+ "<email>" + email + "</email>\n"
						+ "<age>" + age + "</age>\n"
						+ "</user>\n";
			}

			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			st = Response.Status.INTERNAL_SERVER_ERROR;
			result = "<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n";
		}

		return Response.status(st).entity(result).build();
	}

	/**
	 * @param user_id
	 * @param limit
	 * @param offset
	 * @param sdate
	 * @param edate
	 * @return Response
	 */
	public static Response getPosts(Long user_id, String filter, Integer limit, Integer offset, String sdate, String edate) {
		connectDB();
		PreparedStatement sentence = null;
		String result = "<?xml version=\"1.0\"?>\n<Messages>\n";
		String query = "SELECT * \n"
				+ "FROM messages m, users u \n"
				+ "WHERE u.user_id = ? \n"
				+ "AND m.author_id = u.user_id\n";
		if (sdate != null && edate != null) {
			query += "AND m.time BETWEEN '" + sdate + "' AND '" + edate + "' \n";
		} else {
			if (sdate != null) {
				query += "AND m.time >= '" + sdate + "' \n";
			}
			if (edate != null) {
				query += "AND m.time <= '" + edate + "' \n";
			}
		}
		if (filter != null) {
			query += "AND m.message LIKE '%" + filter + "%' \n";
		}
		if (limit != null) {
			query += "LIMIT " + limit + " \n";
			if (offset != null) {
				query += "OFFSET " + offset + " \n";
			}
		}
		try {
			sentence = connection.prepareStatement(query);
			sentence.setLong(1, user_id);
			ResultSet rs = sentence.executeQuery();
			while (rs.next()) {
				String postBody = rs.getString("message");
				Date date = rs.getDate("time");
				result += "<message>" + postBody + "</message>\n"
						+ "<time>" + date + "</time>\n";
			}
			sentence.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}

		result += "</Messages>";
		return Response.status(Response.Status.OK).entity(result).build();
	}

	public static Response getSpecificPost(Long user_id, Long message_id) {
		connectDB();
		PreparedStatement sentence = null;
		String result = "<?xml version=\"1.0\"?>\n<Messages>\n";
		String query = "SELECT * \n"
				+ "FROM messages m, users u \n"
				+ "WHERE u.user_id = ? \n"
				+ "AND m.author_id = u.user_id\n"
				+ "AND m.message_id = " + message_id + " \n";

		try {
			sentence = connection.prepareStatement(query);
			sentence.setLong(1, user_id);
			ResultSet rs = sentence.executeQuery();
			while (rs.next()) {
				String postBody = rs.getString("message");
				Date date = rs.getDate("time");
				result += "<message>" + postBody + "</message>\n"
						+ "<time>" + date + "</time>\n";
			}
			sentence.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}

		result += "</Messages>";
		return Response.status(Response.Status.OK).entity(result).build();
	}

	public static Response getFriendsPostsByDate(Long userId, String startDate, String endDate, Integer limit) {
		connectDB();
		String query = "SELECT m.message, m.time, u.name " +
				"FROM messages m " +
				"JOIN friends f ON m.author_id = f.friend_id " +
				"JOIN users u ON f.friend_id = u.user_id " +
				"WHERE f.user_id = ? "/*AND m.time BETWEEN ? AND ?"*/;
		if (limit != null)
			query += "LIMIT " + limit + " \n";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setLong(1, userId);
       /*stmt.setString(2, startDate );
        stmt.setString(3, endDate);*/
			ResultSet rs = stmt.executeQuery();

			String result = "<?xml version=\"1.0\"?>\n<messages>\n";
			while (rs.next()) {
				String message = rs.getString("message");
				Date date = rs.getDate("time");
				String authorName = rs.getString("name");
				result += "<message>\n"
						+ "<text>" + message + "</text>\n"
						+ "<time>" + date + "</time>\n"
						+ "<author>" + authorName + "</author>\n"
						+ "</message>\n";
			}
			result += "</messages>";
			stmt.close();
			rs.close();
			return Response.ok().entity(result).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n").build();
		}
	}

	public static Response deleteSpecificPost(Long user_id, Long message_id) {
		connectDB();
		PreparedStatement sentence = null;
		String query = "DELETE FROM messages WHERE message_id = ? AND author_id IN (SELECT user_id FROM users WHERE user_id = ?)";
		try {
			sentence = connection.prepareStatement(query);
			sentence.setLong(1, message_id);
			sentence.setLong(2, user_id);
			int rowsAffected = sentence.executeUpdate();
			if (rowsAffected == 0) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity(new String("<message>The post with id " + message_id + " was not found for user " + user_id + "</message>")).build();
			}
			sentence.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		return Response.status(Response.Status.OK).entity(new String("<message>The post with id " + message_id + " was successfully deleted for user " + user_id + "</message>")).build();
	}

	public static Response deleteUserFriend(Long userId, Long friendId) {
		connectDB();
		String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
		PreparedStatement stmt;
		String result = "<?xml version=\"1.0\"?>\n";
		Status st = Status.OK;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, userId);
			stmt.setLong(2, friendId);
			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected == 0) {
				st = Response.Status.NOT_FOUND;
				result += "<message>Friend with id " + friendId + " not found for user with id " + userId + "</message>";
			} else {
				result += "<message>Friend with id " + friendId + " has been removed from user with id " + userId + "</message>";
			}

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			st = Response.Status.INTERNAL_SERVER_ERROR;
			result = "<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n";
		}

		return Response.status(st).entity(result).build();
	}

	public static Response deleteUserById(Long userId) {
		connectDB();
		String sql = "DELETE FROM users WHERE user_id = ?";
		PreparedStatement stmt;
		String result = "<?xml version=\"1.0\"?>\n";
		Status st = Status.OK;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, userId);
			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected == 0) {
				st = Response.Status.NOT_FOUND;
				result += "<message>User with id " + userId + " not found</message>";
			} else {
				result += "<message>User with id " + userId + " has been deleted</message>";
			}

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			st = Response.Status.INTERNAL_SERVER_ERROR;
			result = "<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n";
		}

		return Response.status(st).entity(result).build();
	}

	public static Response getFriendsPostsByContent(Long userId, String searchTerm, Integer limit) {
		connectDB();
		String query = "SELECT m.message, m.time, u.name " +
				"FROM messages m " +
				"JOIN friends f ON m.author_id = f.friend_id " +
				"JOIN users u ON f.friend_id = u.user_id " +
				"WHERE f.user_id = ? AND m.message LIKE ? ";
		if (limit != null) {
			query += "LIMIT " + limit + ";";
		}
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setLong(1, userId);
			stmt.setString(2, "%" + searchTerm + "%");
			ResultSet rs = stmt.executeQuery();

			String result = "<?xml version=\"1.0\"?>\n<messages>\n";
			while (rs.next()) {
				String message = rs.getString("message");
				Date date = rs.getDate("time");
				String authorName = rs.getString("name");
				result += "<message>\n"
						+ "<text>" + message + "</text>\n"
						+ "<time>" + date + "</time>\n"
						+ "<author>" + authorName + "</author>\n"
						+ "</message>\n";
			}
			result += "</messages>";
			stmt.close();
			rs.close();
			return Response.ok().entity(result).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n").build();
		}
	}

	/*public static Response getFriends(Long user_id, String q, Integer limit, Integer offset) {
		PreparedStatement sentence = null;
		PreparedStatement sentenceFriend = null;
		String result = "<?xml version=\"1.0\"?>\n<Friends>\n";
		String query = "SELECT * \n"
				+ "FROM friends f, users u \n"
				+ "WHERE f.user_id = ? \n"
				+ "AND f.user_id = u.user_id \n";
		if (limit != null) {
			query += "LIMIT " + limit + " \n";
			if (offset != null) {
				query += "OFFSET " + offset + " \n";
			}
		}
		String queryFriend = "SELECT * \n"
				+ "FROM USERS u\n"
				+ "WHERE u.user_id = ?";
		try {
			sentence = connection.prepareStatement(query);
			sentence.setLong(1, user_id);
			ResultSet rs = sentence.executeQuery();
			while (rs.next()) {
				Integer friend = rs.getInt("user2");
				sentenceFriend = connection.prepareStatement(queryFriend);
				sentenceFriend.setInt(1, friend);
				ResultSet rsFriend = sentenceFriend.executeQuery();
				while (rsFriend.next()) {
					String name = rsFriend.getString("name");
					String lastname = rsFriend.getString("lastname");
					result += "<Friend>\n"
							+ "<Name>" + name + "</Name>"
							+ "<LastName>" + lastname + "</LastName>"
							+ "</Friend>";
				}
				sentenceFriend.close();
				rsFriend.close();
			}
			sentence.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		result += "</Friends>";
		return Response.status(Response.Status.OK).entity(result).build();
	}*/
	//userId se hace amigo de friend
	public static void insertFriend(Long userId, User friend) {
		connectDB();
		String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, userId);
			stmt.setLong(2, friend.getUserId());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Response getUserFriends(Long userId, String namePattern, Integer limit) {
		connectDB();
		String sql = "SELECT u.* FROM users u JOIN friends f ON u.user_id = f.friend_id WHERE f.user_id = ?";
		if (namePattern != null && !namePattern.isEmpty()) {
			sql += " AND name LIKE ?";
		}
		if (limit != null && limit > 0) {
			sql += " LIMIT ?";
		}
		PreparedStatement stmt;
		String result = "<?xml version=\"1.0\"?>\n";
		Status st = Status.OK;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, userId);
			int paramIndex = 2;
			if (namePattern != null && !namePattern.isEmpty()) {
				stmt.setString(paramIndex++, "%" + namePattern + "%");
			}
			if (limit != null && limit > 0) {
				stmt.setInt(paramIndex, limit);
			}
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				st = Response.Status.NOT_FOUND;
				result += "<message>User with id " + userId + " has no friends</message>";
			} else {
				result += "<userFriends>\n";
				do {
					Long friendId = rs.getLong("user_id");
					String name = rs.getString("name");
					String email = "" + rs.getString("email");
					String age = "" + rs.getInt("age");

					result += "<friend>\n"
							+ "<friendId>" + friendId + "</friendId>\n"
							+ "<name>" + name + "</name>\n"
							+ "<email>" + email + "</email>\n"
							+ "<age>" + age + "</age>\n"
							+ "</friend>\n";
				} while (rs.next());
				result += "</userFriends>\n";
			}

			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			st = Response.Status.INTERNAL_SERVER_ERROR;
			result = "<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n";
		}

		return Response.status(st).entity(result).build();
	}

	//Working...
	public static int addUserToDB(User user) {
		connectDB();
		//Connection conn = null;
		PreparedStatement stmt = null;
		try {


			// Preparar una sentencia SQL para insertar un nuevo usuario
			String query = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
			stmt = connection.prepareStatement(query);
			//stmt.setLong(1, user.getUserId());
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setInt(3, user.getAge());

			// Ejecutar la sentencia SQL para insertar un nuevo usuario
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			// Si se produce un error, devolver -1 para indicar que la inserción falló
			return -1;
		} finally {
			try {
				// Cerrar la conexión y la sentencia SQL
				if (stmt != null) {
					stmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param user_id
	 * @param username
	 * @param email
	 * @param age
	 * @return
	 */

	public static Response createUser(Long user_id, String username, String email, int age) {
		Statement sentence = null;
		String query = "INSERT INTO USERS\n"
				+ "(user_id, name, email, age)\n"
				+ "VALUES\n"
				+ "('" + user_id + "', '" + username
				+ email + "', '"
				+ age + "', '"
				+ "');";
		try {
			sentence = connection.createStatement();
			int rs = sentence.executeUpdate(query);
			if (rs == 0) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			sentence.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		return Response.status(Response.Status.CREATED).build();
	}

	public static Response postDeleteUser(Long user_id) {
		Statement sentence = null;
		String query = "DELETE FROM USERS\n"
				+ "WHERE user_id='" + user_id + "';";
		try {
			sentence = connection.createStatement();
			int rs = sentence.executeUpdate(query);
			if (rs != 0) {
				return Response.status(Response.Status.OK).build();
			}
			sentence.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	public static Response postAddPost(Integer idPost, String postBody, String time, Integer user) {
		Statement sentence = null;
		String query = "INSERT INTO POST\n"
				+ "(idPost, postBody, time, user)\n"
				+ "VALUES\n"
				+ "('" + idPost + "', '" + postBody + "', '" + time + "', '" + user + "');";

		try {
			sentence = connection.createStatement();
			int rs = sentence.executeUpdate(query);
			if (rs != 0) {
				return Response.status(Response.Status.CREATED).build();
			}
			sentence.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	public static Response postDeletePost(Integer idPost) {
		Statement sentence = null;
		String query = "DELETE FROM POST\n"
				+ "WHERE idPost = " + idPost + ";";
		try {
			sentence = connection.createStatement();
			int rs = sentence.executeUpdate(query);
			if (rs != 0) {
				return Response.status(Response.Status.OK).build();
			}
			sentence.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	public static Response postAddFriend(Long user_id, Integer user_idFriend) {
		Statement sentence1 = null;
		Statement sentence2 = null;
		Boolean allIsGood = false;
		String query1 = "INSERT INTO ISFRIEND\n"
				+ "(user1, user2)\n"
				+ "VALUES\n"
				+ "('" + user_id + "', '" + user_idFriend + "');\n";
		try {
			sentence1 = connection.createStatement();
			int rs = sentence1.executeUpdate(query1);
			if (rs != 0) {
				allIsGood = true;
			}
			sentence1.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}

		String query2 = "INSERT INTO ISFRIEND\n"
				+ "(user1, user2)\n"
				+ "VALUES\n"
				+ "('" + user_idFriend + "', '" + user_id + "');\n";
		try {
			sentence2 = connection.createStatement();
			int rs = sentence2.executeUpdate(query2);
			if (rs != 0 && allIsGood) {
				return Response.status(Response.Status.CREATED).build();
			}
			sentence2.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}


	public static Boolean postModifyName(Long user_id, String name) {
		String query = "UPDATE USERS u\n"
				+ "SET u.name = '" + name + "'\n"
				+ "WHERE u.user_id = " + user_id;
		Boolean result = false;
		Statement sentence = null;
		try {
			sentence = connection.createStatement();
			int rs = sentence.executeUpdate(query);
			if (rs != 0) {
				result = true;
			}
			sentence.close();
		} catch (SQLException e) {
			e.printStackTrace();
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		return result;
	}

	public static Boolean postModifyMail(Long user_id, String mail) {
		String query = "UPDATE USERS u\n"
				+ "SET u.mail = '" + mail + "'\n"
				+ "WHERE u.user_id = " + user_id;
		Boolean result = false;
		Statement sentence = null;
		try {
			sentence = connection.createStatement();
			int rs = sentence.executeUpdate(query);
			if (rs != 0) {
				result = true;
			}
			sentence.close();
		} catch (SQLException e) {
			e.printStackTrace();
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();
		}
		return result;
	}

	public static Response putModifyPost(Integer idPost, String postBody) {
		String query = "UPDATE POST p\n"
				+ "SET p.postBody = '" + postBody + "'\n"
				+ "WHERE p.idPost = " + idPost;
		Statement sentence = null;
		try {
			sentence = connection.createStatement();
			int rs = sentence.executeUpdate(query);
			if (rs == 0) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();

		}
		return Response.status(Response.Status.OK).build();
	}

	public static Response postDeleteFriend(Long user_id, Integer idFriend) {
		String query1 = "DELETE FROM ISFRIEND \n"
				+ "WHERE user1 = " + user_id + "\n"
				+ "AND user2 = " + idFriend;
		String query2 = "DELETE FROM ISFRIEND\n"
				+ "WHERE user1 = " + idFriend + "\n"
				+ "AND user2 = " + user_id;
		Statement sentence1 = null;
		Statement sentence2 = null;
		try {
			sentence1 = connection.createStatement();
			int rs = sentence1.executeUpdate(query1);
			if (rs == 0) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			sentence1.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();

		}
		try {
			sentence2 = connection.createStatement();
			int rs = sentence2.executeUpdate(query2);
			if (rs == 0) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			sentence2.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n")).build();

		}
		return Response.status(Response.Status.OK).build();
	}

	public static Response getFriendPosts(Long user_id, String postBody,
										  Integer limit, Integer offset, String sdate, String edate) {
		Statement sentenceFriend = null;
		String queryFriend = "SELECT user2\n"
				+ "FROM ISFRIEND f\n"
				+ "WHERE f.user1 = " + user_id;
		String queryPost = "SELECT *\n"
				+ "FROM POST p, USERS u\n"
				+ "WHERE p.user = ?\n"
				+ "AND u.user_id = ?\n";
		if (sdate != null && edate != null) {
			queryPost += "AND p.time BETWEEN '" + sdate + "' AND '" + edate + "' \n";
		} else {
			if (sdate != null) {
				queryPost += "AND p.time >= '" + sdate + "' \n";
			}
			if (edate != null) {
				queryPost += "AND p.time <= '" + edate + "' \n";
			}
		}
		if (postBody != null) {
			queryPost += "AND p.postBody LIKE '%" + postBody + "%'";
		}
		if (limit != null) {
			queryPost += "LIMIT " + limit + " \n";
			if (offset != null) {
				queryPost += "OFFSET " + offset + " \n";
			}
		}
		String subString = "";
		int counter = 0;
		PreparedStatement sentencePost = null;
		String xmlResult = "<?xml version=\"1.0\"?>\n<Messages>\n";
		try {
			sentenceFriend = connection.createStatement();
			ResultSet rsFriend = sentenceFriend.executeQuery(queryFriend);
			while (rsFriend.next()) {
				Integer myFriend = rsFriend.getInt("user2");
				sentencePost = connection.prepareStatement(queryPost);
				sentencePost.setInt(1, myFriend);
				sentencePost.setInt(2, myFriend);
				ResultSet rsPost = sentencePost.executeQuery();
				subString += "<Friend>\n";
				while (rsPost.next()) {
					String body = rsPost.getString("postBody");
					String friendName = rsPost.getString("username");
					subString += "<Username>" + friendName + "</Username>\n"
							+ "<PostBody>" + body + "</PostBody>\n";
					counter += 1;
				}
				if (counter != 0) {
					subString += "</Friend>\n";
					xmlResult += subString;
				}
				subString = "";
				counter = 0;
				sentencePost.close();
				rsPost.close();
			}
			sentenceFriend.close();
			rsFriend.close();
		} catch (SQLException e) {
			e.printStackTrace();
			xmlResult += new String("<SQL ERROR>\n\t" + e.getMessage() + "\n</SQL ERROR>\n");
			// st = Status.INTERNAL_SERVER_ERROR;
		}
		xmlResult += "</Messages>";
		return Response.status(Response.Status.OK).entity(xmlResult).build();
	}

}