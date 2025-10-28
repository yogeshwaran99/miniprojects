package com.blog;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class BlogServlet
 */
@WebServlet("/BlogServlet")
public class BlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlogServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    private Connection connect() throws Exception{
        Class.forName("org.postgresql.Driver");
    	String url  = "jdbc:postgresql://localhost:5432/mydb";
    	String username = "postgres";
    	String password = "12345";
    	return DriverManager.getConnection(url, username, password);
    }
        
     
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
    	response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Max-Age", "1728000");
        System.out.println("called");
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
      
        if (idParam != null&& idParam.matches("\\d+")) {
            try (Connection con = connect();
                 PreparedStatement ps = con.prepareStatement("SELECT * FROM blogs WHERE id = ?")) {
                ps.setInt(1, Integer.parseInt(idParam));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String json = "{"
                                + "\"id\":" + rs.getInt("id") + ","
                                + "\"title\":\"" + escapeJson(rs.getString("title")) + "\","
                                + "\"content\":\"" + escapeJson(rs.getString("content")) + "\","
                                + "\"date\":\"" + escapeJson(rs.getString("date")) + "\","
                                + "\"tags\":\"" + escapeJson(rs.getString("tags")) + "\""
                                + "}";
                        response.getWriter().write(json);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\":\"Blog not found\"}");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            }
        } 

        else {
            StringBuilder json = new StringBuilder();
            json.append("[");
            String sql = "SELECT * FROM blogs";
            try (Connection con = connect();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) json.append(",");
                    first = false;                    
                    json.append("{");
                    json.append("\"id\":").append(rs.getInt("id")).append(",");
                    json.append("\"title\":\"").append(escapeJson(rs.getString("title"))).append("\",");
                    json.append("\"content\":\"").append(escapeJson(rs.getString("content"))).append("\",");
                    json.append("\"date\":\"").append(escapeJson(rs.getString("date"))).append("\",");
                    json.append("\"tags\":\"").append(escapeJson(rs.getString("tags"))).append("\"");
                    json.append("}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().println("{\"error\":\"" + e.getMessage() + "\"}");
            }
            json.append("]");
            response.getWriter().write(json.toString());
        }
    }

	private String escapeJson(String value) {
	    if (value == null) return "";
	    return value.replace("\"", "\\\"")
	                .replace("\n", "\\n")
	                .replace("\r", "");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "1728000");
	    System.out.println("called post");
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/plain");
	    String title = request.getParameter("title");
	    String content = request.getParameter("content");
	    String date = request.getParameter("date");	   
	    String tags = request.getParameter("tags");
	    try (Connection conn = connect();
	         PreparedStatement ps = conn.prepareStatement(
	             "INSERT INTO blogs (title, content, date,  tags) VALUES (?, ?, ?, ?)")) {
	        ps.setString(1, title);
	        ps.setString(2, content);
	        ps.setString(3, date);	        
	        ps.setString(4, tags);
	        System.out.println(title);
	        int rows = ps.executeUpdate();
	        if (rows > 0) {
	            response.getWriter().println("Blog inserted successfully!");
	        } else {
	            response.getWriter().println("Failed to insert blog.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.getWriter().println("Error: " + e.getMessage());
	    }
	    
	}


	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		
	    response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "OPTIONS, PUT");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "1728000");
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/plain");

	    System.out.println("called put");

	    StringBuilder body = new StringBuilder();
	    try (BufferedReader reader = request.getReader()) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            body.append(line);
	        }
	    }
	    Map<String, String> params = new HashMap<>();
	    for (String pair : body.toString().split("&")) {
	        String[] kv = pair.split("=", 2);
	        if (kv.length == 2) {
	            params.put(
	                java.net.URLDecoder.decode(kv[0], "UTF-8"),
	                java.net.URLDecoder.decode(kv[1], "UTF-8")
	            );
	        }
	    }

	    String idStr = request.getParameter("id");
	    if (idStr == null || idStr.isEmpty()) {
	        String query = request.getQueryString();
	        if (query != null && query.contains("id=")) {
	            idStr = query.replaceAll(".*id=(\\d+).*", "$1");
	        }
	    }

	    if (idStr == null) {
	        response.getWriter().println("Error: Missing 'id' parameter.");
	        return;
	    }

	    int id = Integer.parseInt(idStr);
	    String title = params.get("title");
	    String content = params.get("content");
	    String date = params.get("date");
	    String tags = params.get("tags");

	    System.out.println(id + " " + title + " " + date + " " + tags);

	    String sql = "UPDATE blogs SET title = ?, content = ?, date = ?, tags = ? WHERE id = ?";
	    try (Connection conn = connect();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, title);
	        ps.setString(2, content);
	        ps.setString(3, date);
	        ps.setString(4, tags);
	        ps.setInt(5, id);

	        int rows = ps.executeUpdate();
	        if (rows > 0) {
	            response.getWriter().println("Blog updated successfully!");
	        } else {
	            response.getWriter().println("No blog found with id: " + id);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.getWriter().println("Error: " + e.getMessage());
	    }
	}



	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "OPTIONS, DELETE");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");	   
	    response.setContentType("text/plain");

	    String idStr = request.getParameter("id");
	    if (idStr == null) {
	        response.getWriter().println("Error: Missing 'id' parameter.");
	        return;
	    }

	    int id = Integer.parseInt(idStr);

	    String sql = "DELETE FROM blogs WHERE id = ?";
	    try (Connection conn = connect();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, id);
	        int rows = ps.executeUpdate();
	        if (rows > 0) {
	            response.getWriter().println("Blog deleted successfully!");
	        } else {
	            response.getWriter().println("No blog found with id: " + id);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.getWriter().println("Error: " + e.getMessage());
	    }
	}
	
	
	protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws IOException {
	    res.setHeader("Access-Control-Allow-Origin", "*");
	    res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
	    res.setHeader("Access-Control-Allow-Headers", "Content-Type");
	    res.setStatus(HttpServletResponse.SC_OK);
	}



}
