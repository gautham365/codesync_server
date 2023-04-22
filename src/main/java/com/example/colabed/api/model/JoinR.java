package com.example.colabed.api.model;

public class JoinR {
   private String token;
   private String roomCode;
   public JoinR(){}

   public JoinR (String token, String roomCode){
      super();
      this.token = token;
      this.roomCode = roomCode;
   }


   public String getRoomCode() {
      return roomCode;
   }

   public String getToken(){
      return token;
   }

}
