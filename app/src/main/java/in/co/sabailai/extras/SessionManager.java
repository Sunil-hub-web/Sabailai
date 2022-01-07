package in.co.sabailai.extras;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import in.co.sabailai.Login;
import in.co.sabailai.SplashScreen;


/**
 * Created by Narendra on 6/8/2017.
 */

public class SessionManager {

    SharedPreferences sharedprefernce;
    SharedPreferences.Editor editor;
    SharedPreferences sharedprefernceCoupon;
    SharedPreferences.Editor editorCoupon;
    Context context;
    int PRIVATE_MODE=0;

    private static final String PREF_NAME="sharedcheckLogin";
    private static final String PREF_NAME2="sharedcheckLogin2";
    private static final String User_Id="userid";
    private static final String DisplayUser_Id="displayuserid";
    private static final String UserName ="uname";
    private static final String Email="email";
    private static final String Phone="phone";
    private static final String PetName="petname";
    private static final String WalletAmount="walletamount";
    private static final String LoginToken="logintoken";
    private static final String ProfilePic="img";
    private static final String LogKeyExp="lkexp";
    private static final String LogKey="lkey";
    private static final String DisplayName ="dname";
    private static final String RestaurantStatus ="RestaurantStatus";
    private static final String UserRechargeMode="userrechargemode";
    private static final String EmailVerifyStatus="emailverifystatus";
    private static final String CCPay="ccpay";
    private static final String Otp="otp";
    private static final String OtpKey="otpkey";
    private static final String IS_LOGIN="islogin";
    private static final String FirstName="FirstName";
    private static final String LastName="LastName";
    private static final String Address1="Address1";
    private static final String Address2="Address2";
    private static final String City="City";
    private static final String State="State";
    private static final String PostCode="PostCode";
    private static final String country="country";
    private static final String PinCode="PinCode";
    private static final String shiipingEmail="shiipingEmail";
    private static final String SelectedShippingAddress="selectedshippingaddress";
    private static final String SelectedCityName="selectedcityname";
    private static final String SelectedCityId="selectedcityid";
    private static final String SelectedAddressId="selectedAddressid";

    private static final String Totalamt="Totalamt";
    private static final String Ti="Ti";
    private static final String Ai="Ai";
    private static final String IfAddressSame="IfAddressSame";

    private static final String billFirstANme="billFirstANme";
    private static final String billlastANme="billlastANme";
    private static final String billAddress1="billAddress1";
    private static final String billAddres2="billAddres2";
    private static final String billCity="billCity";
    private static final String billState="billState";
    private static final String billPostCode="billPostCode";
    private static final String billcountry="billcountry";
    private static final String billEmail="billEmail";
    private static final String billPhone="billPhone";
    private static final String CouponCode="CouponCode";
    private static final String CouponPrice="CouponPrice";
    private static final String FCMId="FCMId";

    private static final String PickPin="PickPin";
    private static final String PickCity="PickCity";
    private static final String DropPin="DropPin";
    private static final String DropCity="DropCity";

    private static final String PAddressName="PAddressName";
    private static final String PAddressHouse="PAddressHouse";
    private static final String PAddressLandmark="PAddressLandmark";
    private static final String PAddressState="PAddressState";
    private static final String PAddressStateId="PAddressStateId";
    private static final String PAddressCity="PAddressCity";
    private static final String PAddressCityId="PAddressCityId";
    private static final String PAddressPhone="PAddressPhone";

    private static final String DAddressName="DAddressName";
    private static final String DAddressHouse="DAddressHouse";
    private static final String DAddressLandmark="DAddressLandmark";
    private static final String DAddressState="DAddressState";
    private static final String DAddressStateId="DAddressStateId";
    private static final String DAddressCity="DAddressCity";
    private static final String DAddressCityId="DAddressCityId";
    private static final String DAddressPhone="DAddressPhone";

    private static final String UserType="UserType";
    private static final String DocDescription="DocDescription";
    private static final String DocWeight="DocWeight";
    private static final String DocDimension="DocDimension";
    private static final String PickDate="PickDate";
    private static final String PickTime="PickTime";



    public SessionManager(Context context){

        this.context =  context;
        sharedprefernce = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor=sharedprefernce.edit();

        sharedprefernceCoupon=context.getSharedPreferences(PREF_NAME2,PRIVATE_MODE);
        editorCoupon=sharedprefernceCoupon.edit();

    }

    public Boolean isLogin(){
        return sharedprefernce.getBoolean(IS_LOGIN, false);

    }
    public void setLogin(){

        editor.putBoolean(IS_LOGIN, true);
        editor.commit();

    }


    public void setFCMId(String fcmid){

        editorCoupon.putString(FCMId,fcmid);
        editorCoupon.commit();
    }

    public String getFCMId(){

        return sharedprefernceCoupon.getString(FCMId,"");
    }











    public void setUserID(String id ){
     editor.putString(User_Id,id);
     editor.commit();
    }
    public String getUserID(){
        return  sharedprefernce.getString(User_Id,"DEFAULT");
    }

    public void setDisplayUserId(String uid ){
     editor.putString(DisplayUser_Id,uid);
     editor.commit();
    }
    public String getDisplayUserId(){
        return  sharedprefernce.getString(DisplayUser_Id,"DEFAULT");
    }





    public void setUserName(String uname){

        editor.putString(UserName,uname);
        editor.commit();
    }

    public String getUserName(){

        return sharedprefernce.getString(UserName,"Default");
    }

    public void setImgUrl(String img){

        editor.putString(ProfilePic,img);
        editor.commit();
    }

    public String getImgUrl(){

        return sharedprefernce.getString(ProfilePic,"Default");
    }




    public void setEmail(String email){

        editor.putString(Email,email);
        editor.commit();
    }

    public String getEmail(){

        return sharedprefernce.getString(Email,"Default");
    }


    public void setLogKeyExp(String lkexp){

        editor.putString(LogKeyExp,lkexp);
        editor.commit();
    }

    public String getLogKeyExp(){

        return sharedprefernce.getString(LogKeyExp,"Default");
    }


    public void setLogKey(String lkey){

        editor.putString(LogKey,lkey);
        editor.commit();
    }

    public String getLogKey(){

        return sharedprefernce.getString(LogKey,"Default");
    }


    public void setDisplayName(String dname){

        editor.putString(DisplayName,dname);
        editor.commit();
    }

    public String getDisplayName(){

        return sharedprefernce.getString(DisplayName,"Default");
    }
    public void setRestaurantStatus(String stat){

        editor.putString(RestaurantStatus,stat);
        editor.commit();
    }

    public String getRestaurantStatus(){

        return sharedprefernce.getString(RestaurantStatus,"Default");
    }


    public void setUserRechargeMode(String userRechargeMode){

        editor.putString(UserRechargeMode,userRechargeMode);
        editor.commit();
    }


    public String getUserRechargeMode(){

        return sharedprefernce.getString(UserRechargeMode,"Default");
    }


    public void setEmailVerifyStatus(String emailVerifyStatus){

        editor.putString(EmailVerifyStatus,emailVerifyStatus);
        editor.commit();
    }


    public String getEmailVerifyStatus(){

        return sharedprefernce.getString(EmailVerifyStatus,"Default");
    }

    public void setCCPay(String ccpay){

        editor.putString(CCPay,ccpay);
        editor.commit();
    }


    public String getCCPay(){

        return sharedprefernce.getString(CCPay,"default");
    }


    public void setOtp(String oTp){

        editor.putString(Otp,oTp);
        editor.commit();
    }
    public String getOtp(){

        return sharedprefernce.getString(Otp,"Default");
    }



    public void setOtpKey(String otpKey){

        editor.putString(OtpKey,otpKey);
        editor.commit();
    }
    public String getOtpKey(){

        return sharedprefernce.getString(OtpKey,"Default");
    }

    public void setFirstName(String name){
        editor.putString(FirstName,name);
        editor.commit();

    }
    public String getFirstName(){
        return  sharedprefernce.getString(FirstName,"First Name");
    }

    public void setLastName(String name){
        editor.putString(LastName,name);
        editor.commit();

    }
    public String getLastName(){
        return  sharedprefernce.getString(LastName,"Last Name");
    }

    public void setAddress1(String name){
        editor.putString(Address1,name);
        editor.commit();

    }
    public String getAddress1(){
        return  sharedprefernce.getString(Address1,"Address 1");
    }

    public String getAddress2(){
        return  sharedprefernce.getString(Address2,"Address 1");
    }

    public void setAddress2(String name){
        editor.putString(Address2,name);
        editor.commit();

    }


    public void setPinCode(String name){
        editor.putString(PinCode,name);
        editor.commit();

    }
    public String getPinCode(){
        return  sharedprefernce.getString(PinCode,"Address 2");
    }

    public void setCity(String name){
        editor.putString(City,name);
        editor.commit();

    }
    public String getCity(){
        return  sharedprefernce.getString(City,"City");
    }

    public void setState(String name){
        editor.putString(State,name);
        editor.commit();

    }
    public String getState(){
        return  sharedprefernce.getString(State,"ODISHA");
    }

    public void setPostCode(String name){
        editor.putString(PostCode,name);
        editor.commit();

    }
    public String getPostCode(){
        return  sharedprefernce.getString(PostCode,"Pin Code");
    }

    public void setcountry(String name){
        editor.putString(country,name);
        editor.commit();

    }
    public String getcountry(){
        return  sharedprefernce.getString(country,"INDIA");
    }

    public void setshiipingEmaile(String name){
        editor.putString(shiipingEmail,name);
        editor.commit();

    }
    public String getshiipingEmail(){
        return  sharedprefernce.getString(shiipingEmail,"");
    }

    public void setSelectedShippingAddress(String selshipadd){
        editor.putString(SelectedShippingAddress,selshipadd);
        editor.commit();

    }
    public String getSelectedShippingAddress(){
        return  sharedprefernce.getString(SelectedShippingAddress,"");
    }

    public void setSelectedCityName(String selctynm){
        editor.putString(SelectedCityName,selctynm);
        editor.commit();

    }
    public String getSelectedCityName(){
        return  sharedprefernce.getString(SelectedCityName,"");
    }

    public void setSelectedCityId(String selctyid){
        editor.putString(SelectedCityId,selctyid);
        editor.commit();

    }
    public String getSelectedCityId(){
        return  sharedprefernce.getString(SelectedCityId,"");
    }

    public void setSelectedAddressId(String seladdid){
        editor.putString(SelectedAddressId,seladdid);
        editor.commit();

    }
    public String getSelectedAddressId(){
        return  sharedprefernce.getString(SelectedAddressId,"");
    }

    public void setPhone(String name){
        editor.putString(Phone,name);
        editor.commit();

    }
    public String getPhone(){
        return  sharedprefernce.getString(Phone,"Phone");
    }

    public void setPetName(String petname){
        editor.putString(PetName,petname);
        editor.commit();

    }
    public String getPetName(){
        return  sharedprefernce.getString(PetName,"PetName");
    }

    public void setWalletAmount(String amount){
        editor.putString(WalletAmount,amount);
        editor.commit();

    }
    public String getWalletAmount(){
        return  sharedprefernce.getString(WalletAmount,"WalletAmount");
    }

    public void setLoginToken(String token){
        editor.putString(LoginToken,token);
        editor.commit();

    }
    public String getLoginToken(){
        return  sharedprefernce.getString(LoginToken,"LoginToken");
    }


    public void setTotalamt(String name){
        editor.putString(Totalamt,name);
        editor.commit();

    }
    public String getTotalamt(){
        return  sharedprefernce.getString(Totalamt,"");
    }


    public void setTi(String name){
        editor.putString(Ti,name);
        editor.commit();

    }
    public String getTi(){
        return  sharedprefernce.getString(Ti,"");
    }


    public void setIfAddressSame(String name){
        editor.putString(IfAddressSame,name);
        editor.commit();

    }
    public String getIfAddressSame(){
        return  sharedprefernce.getString(IfAddressSame,"No");
    }

    public void setAi(String name){
        editor.putString(Ai,name);
        editor.commit();

    }
    public String getAi(){
        return  sharedprefernce.getString(Ai,"");
    }
    public void setBillFirstANme(String name){
        editor.putString(billFirstANme,name);
        editor.commit();

    }
    public String getBillFirstANme(){
        return  sharedprefernce.getString(billFirstANme,"First Name");
    }

    public void setBilllastANme(String name){
        editor.putString(billlastANme,name);
        editor.commit();

    }
    public String getBilllastANme(){
        return  sharedprefernce.getString(billlastANme,"Last Name");
    }

    public void setBillAddress1(String name){
        editor.putString(billAddress1,name);
        editor.commit();

    }
    public String getBillAddress1(){
        return  sharedprefernce.getString(billAddress1,"");
    }

    public void setBillAddres2(String name){
        editor.putString(billAddres2,name);
        editor.commit();

    }
    public String getBillAddres2(){
        return  sharedprefernce.getString(billAddres2,"");
    }

    public void setBillCity(String name){
        editor.putString(billCity,name);
        editor.commit();

    }
    public String getBillCity(){
        return  sharedprefernce.getString(billCity,"");
    }

    public void setBillState(String name){
        editor.putString(billState,name);
        editor.commit();

    }
    public String getBillState(){
        return  sharedprefernce.getString(billState,"");
    }

    public void setBillPostCode(String name){
        editor.putString(billPostCode,name);
        editor.commit();

    }
    public String getBillPostCode(){
        return  sharedprefernce.getString(billPostCode,"");
    }

    public void setBillcountry(String name){
        editor.putString(billcountry,name);
        editor.commit();

    }
    public String getBillcountry(){
        return  sharedprefernce.getString(billcountry,"");
    }

    public void setBillEmaile(String name){
        editor.putString(billEmail,name);
        editor.commit();

    }
    public String getBillEmail(){
        return  sharedprefernce.getString(billEmail,"");
    }

    public void setBillPhone(String name){
        editor.putString(billPhone,name);
        editor.commit();

    }
    public String getBillPhone(){
        return  sharedprefernce.getString(billPhone,"");
    }



    public void setCouponCode(String name){
        editor.putString(CouponCode,name);
        editor.commit();

    }

    public String getCouponCode(){
        return  sharedprefernce.getString(CouponCode,"Phone");
    }



    public void setCouponPrice(String name){
        editor.putString(CouponPrice,name);
        editor.commit();

    }

    public String getCouponPrice(){
        return  sharedprefernce.getString(CouponPrice,"Phone");
    }

    public void clearCoupon(){

        editorCoupon.clear();
        editorCoupon.commit();

    }

    public void setPickPin(String id ){
        editor.putString(PickPin,id);
        editor.commit();
    }
    public String getPickPin(){
        return  sharedprefernce.getString(PickPin,"DEFAULT");
    }

    public void setPickCity(String id ){
        editor.putString(PickCity,id);
        editor.commit();
    }
    public String getPickCity(){
        return  sharedprefernce.getString(PickCity,"DEFAULT");
    }
    public void setDropPin(String id ){
        editor.putString(DropPin,id);
        editor.commit();
    }
    public String getDropPin(){
        return  sharedprefernce.getString(DropPin,"DEFAULT");
    }
    public void setDropCity(String id ){
        editor.putString(DropCity,id);
        editor.commit();
    }
    public String getDropCity(){
        return  sharedprefernce.getString(DropCity,"DEFAULT");
    }


    public void setPAddressName(String id ){
        editor.putString(PAddressName,id);
        editor.commit();
    }
    public String getPAddressName(){
        return  sharedprefernce.getString(PAddressName,"DEFAULT");
    }
    public void setPAddressHouse(String id ){
        editor.putString(PAddressHouse,id);
        editor.commit();
    }
    public String getPAddressHouse(){
        return  sharedprefernce.getString(PAddressHouse,"DEFAULT");
    }
    public void setPAddressLandmark(String id ){
        editor.putString(PAddressLandmark,id);
        editor.commit();
    }
    public String getPAddressLandmark(){
        return  sharedprefernce.getString(PAddressLandmark,"DEFAULT");
    }
    public void setPAddressState(String id ){
        editor.putString(PAddressState,id);
        editor.commit();
    }
    public String getPAddressState(){
        return  sharedprefernce.getString(PAddressState,"DEFAULT");
    }

    public void setPAddressStateId(String sid ){
        editor.putString(PAddressStateId,sid);
        editor.commit();
    }
    public String getPAddressStateId(){
        return  sharedprefernce.getString(PAddressStateId,"DEFAULT");
    }
    public void setPAddressCity(String id ){
        editor.putString(PAddressCity,id);
        editor.commit();
    }
    public String getPAddressCity(){
        return  sharedprefernce.getString(PAddressCity,"DEFAULT");
    }
    public void setPAddressCityId(String id ){
        editor.putString(PAddressCityId,id);
        editor.commit();
    }
    public String getPAddressCityId(){
        return  sharedprefernce.getString(PAddressCityId,"DEFAULT");
    }
    public void setPAddressPhone(String id ){
        editor.putString(PAddressPhone,id);
        editor.commit();
    }
    public String getPAddressPhone(){
        return  sharedprefernce.getString(PAddressPhone,"DEFAULT");
    }


    public void setDAddressName(String id ){
        editor.putString(DAddressName,id);
        editor.commit();
    }
    public String getDAddressName(){
        return  sharedprefernce.getString(DAddressName,"DEFAULT");
    }
    public void setDAddressHouse(String id ){
        editor.putString(DAddressHouse,id);
        editor.commit();
    }
    public String getDAddressHouse(){
        return  sharedprefernce.getString(DAddressHouse,"DEFAULT");
    }
    public void setDAddressLandmark(String id ){
        editor.putString(DAddressLandmark,id);
        editor.commit();
    }
    public String getDAddressLandmark(){
        return  sharedprefernce.getString(DAddressLandmark,"DEFAULT");
    }
    public void setDAddressState(String id ){
        editor.putString(DAddressState,id);
        editor.commit();
    }
    public String getDAddressState(){
        return  sharedprefernce.getString(DAddressState,"DEFAULT");
    }
    public void setDAddressStateId(String id ){
        editor.putString(DAddressStateId,id);
        editor.commit();
    }
    public String getDAddressStateId(){
        return  sharedprefernce.getString(DAddressStateId,"DEFAULT");
    }
    public void setDAddressCity(String id ){
        editor.putString(DAddressCity,id);
        editor.commit();
    }
    public String getDAddressCity(){
        return  sharedprefernce.getString(DAddressCity,"DEFAULT");
    }
    public void setDAddressCityId(String id ){
        editor.putString(DAddressCityId,id);
        editor.commit();
    }
    public String getDAddressCityId(){
        return  sharedprefernce.getString(DAddressCityId,"DEFAULT");
    }
    public void setDAddressPhone(String id ){
        editor.putString(DAddressPhone,id);
        editor.commit();
    }
    public String getDAddressPhone(){
        return  sharedprefernce.getString(DAddressPhone,"DEFAULT");
    }


    public void setUserType(String typ ){
        editor.putString(UserType,typ);
        editor.commit();
    }
    public String getUserType(){
        return  sharedprefernce.getString(UserType,"DEFAULT");
    }
    public void setDocDescription(String id ){
        editor.putString(DocDescription,id);
        editor.commit();
    }
    public String getDocDescription(){
        return  sharedprefernce.getString(DocDescription,"DEFAULT");
    }
    public void setDocWeight(String id ){
        editor.putString(DocWeight,id);
        editor.commit();
    }
    public String getDocWeight(){
        return  sharedprefernce.getString(DocWeight,"DEFAULT");
    }
    public void setDocDimension(String id ){
        editor.putString(DocDimension,id);
        editor.commit();
    }
    public String getDocDimension(){
        return  sharedprefernce.getString(DocDimension,"DEFAULT");
    }
    public void setPickDate(String id ){
        editor.putString(PickDate,id);
        editor.commit();
    }
    public String getPickDate(){
        return  sharedprefernce.getString(PickDate,"DEFAULT");
    }
    public void setPickTime(String id ){
        editor.putString(PickTime,id);
        editor.commit();
    }
    public String getPickTime(){
        return  sharedprefernce.getString(PickTime,"DEFAULT");
    }


    public void clear(){

//        sharedprefernce.

    editor.clear();
        editor.commit();
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

    public void clearUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

}


