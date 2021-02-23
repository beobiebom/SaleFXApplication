package Code_FXML;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
   @FXML
   private TextField txtusername;
   @FXML
   private TextField txtpass;
   @FXML
   private Button btnlogin;
   @FXML
   private Label lberror;

    Connection con=null;
    PreparedStatement ps=null;
    ResultSet rs=null;

    public HomeController() {
        con=ConnectionUtil.conDB();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private String name;
    private String phone;
    @FXML
    public void handlebtaction(MouseEvent e){
      if(e.getSource()==btnlogin){
          if(login().equals("Success")){
             passuserdata();
             try{
                 Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                 FXMLLoader loader=new FXMLLoader();
                 loader.setLocation(getClass().getResource("Menu.fxml"));
                 Scene scene=new Scene(loader.load());
                 stage.setTitle("Home");
                 MenuController controller = loader.getController();
                 controller.show(name,phone);
                 stage.setScene(scene);
              //   stage.setFullScreen(true);
                 stage.show();
             }catch (IOException ioe){
                 ioe.printStackTrace();
             }
          }
      }
   }
   public void passuserdata(){
        String sqlget="SELECT * FROM user WHERE username = ?";
        try{
          ps=con.prepareStatement(sqlget);
          ps.setString(1,txtusername.getText());
          rs=ps.executeQuery();
          rs.next();
          name=rs.getString(3);
          phone=rs.getString(4);
        }catch (SQLException e){
            e.printStackTrace();
        }
   }
   private String login(){
      String username=txtusername.getText();
      String pass=txtpass.getText();
      String sqluser="SELECT * FROM user WHERE username = ? and password = ?";
      String stt="Success";
      if(username.isEmpty()||pass.isEmpty()) {
          lberror.setText("VUI LÒNG NHẬP EMAIL HOẶC MẬT KHẨU");
          return "Error";
      }else{
          try{
            ps=con.prepareStatement(sqluser);
            ps.setString(1,username);
            ps.setString(2,pass);
            rs=ps.executeQuery();
            if(!rs.next()){
               lberror.setText("SAI TÀI KHOẢN HOẶC MẬT KHẨU");
               return "Error";
            }else{
                lberror.setText("ĐĂNG NHẬP THÀNH CÔNG");
            }
          }catch (SQLException sql){
              System.err.println(sql.getMessage());
              return "Exception";
          }
      }
      return stt;
   }

}
