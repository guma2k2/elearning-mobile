package com.example.elearningmobile.model;

public class VerifyPostVM {
    private String email;
    private String verificationCode;
    private String type;

    public VerifyPostVM(String email, String verificationCode, String type) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.type = type;
    }

    public VerifyPostVM() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
