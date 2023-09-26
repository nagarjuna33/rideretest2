package com.example.rideredirverapplication.Classes;

public class BankDetailsModel {
    public String accountNo,confirmAccountNo,ifscCode,branchDetails;

    public BankDetailsModel(String accountNo, String confirmAccountNo, String ifscCode, String branchDetails) {
        this.accountNo = accountNo;
        this.confirmAccountNo = confirmAccountNo;
        this.ifscCode = ifscCode;
        this.branchDetails = branchDetails;
    }
}
