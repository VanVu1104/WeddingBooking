package com.example.Model;

public class AdminUser {

        private String name;
        private String email;
        private  String mk;

        public AdminUser() {
            // Để deserialize từ Firestore, cần phải có constructor mặc định
        }

        public AdminUser(String name, String email,String mk) {
            this.name = name;
            this.email = email;
            this.mk = mk;
        }

        // Getters và setters (có thể cần thiết trong một ứng dụng thực tế)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        public  String getMk(){
            return  mk;
        }
        public void setMk(String mk){
            this.mk = mk;
        }
    }

