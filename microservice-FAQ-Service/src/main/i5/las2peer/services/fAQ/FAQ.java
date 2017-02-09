package i5.las2peer.services.fAQ;


import java.net.HttpURLConnection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

import i5.las2peer.api.Service;
import i5.las2peer.api.Context;
import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;
import i5.las2peer.services.fAQ.database.DatabaseManager;
import java.sql.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import org.json.simple.*;
import java.io.*;  

/**
 *
 * FAQ-Service
 *
 * This microservice was generated by the CAE (Community Application Editor). If you edit it, please
 * make sure to keep the general structure of the file and only add the body of the methods provided
 * in this main file. Private methods are also allowed, but any "deeper" functionality should be
 * outsourced to (imported) classes.
 *
 */
@ServicePath("faq")
public class FAQ extends RESTService {


  /*
   * Database configuration
   */
  private String jdbcDriverClassName;
  private String jdbcLogin;
  private String jdbcPass;
  private String jdbcUrl;
  private String jdbcSchema;
  private DatabaseManager dbm;



  public FAQ() {
	super();
    // read and set properties values
    setFieldValues();
    // instantiate a database manager to handle database connection pooling and credentials
    dbm = new DatabaseManager(jdbcDriverClassName, jdbcLogin, jdbcPass, jdbcUrl, jdbcSchema);
  }

  @Override
  public void initResources() {
	getResourceConfig().register(RootResource.class);
  }

  // //////////////////////////////////////////////////////////////////////////////////////
  // REST methods
  // //////////////////////////////////////////////////////////////////////////////////////

  @Api
  @SwaggerDefinition(
      info = @Info(title = "FAQ-Service", version = "1.0",
          description = "A las2peer microservice generated by the CAE.",
          termsOfService = "none",
          contact = @Contact(name = "Neumann", email = "CAEAddress@gmail.com") ,
          license = @License(name = "BSD",
              url = "https://github.com/CAE-Community-Application-Editor/microservice-FAQ-Service/blob/master/LICENSE.txt") ) )
  @Path("/")
  public static class RootResource {

    private final FAQ service = (FAQ) Context.getCurrent().getService();

      /**
   * 
   * postFAQ
   * 
   * @param data a String 
   * 
   * @return Response  
   * 
   */
  @POST
  @Path("/")
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.TEXT_PLAIN)
  @ApiResponses(value = {
       @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "faqResponse")
  })
  @ApiOperation(value = "postFAQ", notes = " ")
  public Response postFAQ(String data) {

    try { 
        String[] requestData = data.split("&");
        String[] question = requestData[0].split("="); 
        String[] answer = requestData[1].split("=");  
        if(question.length>1&&answer.length>1){
            Connection conn = service.dbm.getConnection();
            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO faq  (question, answer) VALUES (?,?)");
            stmnt.setString(1, question[1]); 
            stmnt.setString(2, answer[1]);
            stmnt.executeUpdate();
            JSONObject result = new JSONObject();
            result.put("message", "successfully added Question: '"+question +"'"); 
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(result.toJSONString()).build();
    } catch (Exception e) { 
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        JSONObject result = new JSONObject(); 
        result.put("error", e.toString()); 
        result.put("args", data); 
        result.put("trace", sw.toString());
        return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(result.toJSONString()).build();
    }
  }

  /**
   * 
   * getFAQ
   * 
   *
   * 
   * @return Response  
   * 
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.TEXT_PLAIN)
  @ApiResponses(value = {
       @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "faqResponse")
  })
  @ApiOperation(value = "getFAQ", notes = " ")
  public Response getFAQ() {

    // faqResponse
    try {
        Connection conn = service.dbm.getConnection();
        PreparedStatement stmnt = conn.prepareStatement("SELECT id, question,answer FROM faq");
        ResultSet rs = stmnt.executeQuery();
        JSONArray result = new JSONArray();
        while (rs.next()) { 
            JSONObject obj = new JSONObject();
            obj.put("id", rs.getInt(1));
            obj.put("question", rs.getString(2)); 
            obj.put("answer",rs.getString(3));
            result.add(obj);
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(result.toJSONString()).build();
    } catch (Exception e) { 
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        JSONObject result = new JSONObject(); 
        result.put("error", e.toString()); 
        result.put("trace", sw.toString());
        return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(result.toJSONString()).build();
    }
  }



  }

  // //////////////////////////////////////////////////////////////////////////////////////
  // Service methods (for inter service calls)
  // //////////////////////////////////////////////////////////////////////////////////////
  
  

}
