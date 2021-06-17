package com.example.tanga.driverprotection;

public class UrlConst {
   // public static String SERVER = "http://192.168.1.7:3000/";
   public static String SERVER = "http://192.168.43.179:3000/";
    public static String SERVER2 = "http://192.168.43.179:3001";
//   public static String SERVER = "http://192.168.1.63:3000/";
  //  public static String SERVER2 = "http://192.168.1.63:3001";
    public static String Prediction = "http://192.168.43.179:5000/predict?";
    public static String IMAGES = SERVER + "image/";


/////////////////////////////Chatroom////////////////////////////
    public static String addorgetMessage = SERVER + "messages";
 /////////////////////////// users //////////////////////////////
 public static String addUser = SERVER + "users";
 public static String updateUser = SERVER + "user/";
 public static String loginUser = SERVER + "userlogin/";
 /////////////////////////// vehicules //////////////////////////////
 public static String addVehicule = SERVER + "vehicles";
 public static String getVehicule = SERVER + "vehicle/";
///////////////////////////Predictions////////////////////
public static String addPrediction = SERVER + "predictions";
    ///////////////////////////Accidents////////////////////
    public static String addAccident = SERVER + "accidents";
    public static String getNbAccidents = SERVER + "accidents/";


    /////////////////////////// users //////////////////////////////

    public static String checkUser = SERVER + "user";
    public static String editRole = SERVER + "role";
    public static String FIND_A_USER= SERVER + "users/findauser/";
    public static String UPLOAD_FOLDER_URL = SERVER+"/uploads";
    /////////////////////////signals////////////////////////////
    public static String addSignal= SERVER + "signal";
    public static String deleteSignal= SERVER + "deletesignal/";
    public static String countSignal = SERVER + "signal/";
}
