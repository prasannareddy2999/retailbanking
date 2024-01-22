import { TransactionRecord } from './../services/transactionrecord';
import { CustomerService } from './../services/customer.service';
import { Component, ElementRef, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent implements OnInit {

  accountId: String = '';
  fromAccountId: String = '';
  amount: number = 0;
  toAccountId: String = '';

  error: boolean = false;
  success: boolean = false;
  successMessage: String = '';
  errorMessage = "";

  fromDate: Date = new Date();
  toDate: Date = new Date();

  transactionHistory: TransactionRecord[] = [];
  public customerDetails = {
    "customerId": "",
    "customerName": "",
    "address": "",
    "dateOfBirth": "",
    "panNo": "",
    "gender": "",
    "password": "",
    "accounts": [
      {
        "accountId": "",
        "balance": ""
      },
      {
        "accountId": "",
        "balance": ""
      }]
  };

  public loggedIn = true;
  public employee = true;
  noRecords: boolean = false;
  noRecordMessage: string = '*No records found*';
  thDate: any;
  thAccountId: any;
  thCurrentBalance: any;



  constructor(private customerService: CustomerService, private router: Router) { }

  ngOnInit(): void {
  }


  withdraw_: boolean = false;
  deposit_: boolean = false;
  transfer_: boolean = false;
  getAccount_: boolean = false;
  getCustomer_: boolean = false;
  accountStatement_: boolean = false;

  withdraw() {
    this.error = false;
    this.success = false;
    this.withdraw_ = !this.withdraw_;
    this.accountStatement_ = false;
    this.deposit_ = false;
    this.getAccount_ = false;
    this.getCustomer_ = false;
    this.transfer_ = false;
  }

  deposit() {
    this.error = false;
    this.success = false;
    this.withdraw_ = false;
    this.accountStatement_ = false;
    this.deposit_ = !this.deposit_;
    this.getAccount_ = false;
    this.getCustomer_ = false;
    this.transfer_ = false;
  }

  transfer() {
    this.error = false;
    this.success = false;
    this.withdraw_ = false;
    this.accountStatement_ = false;
    this.deposit_ = false;
    this.getAccount_ = false;
    this.getCustomer_ = false;
    this.transfer_ = !this.transfer_;
  }

  getAccount() {
    this.error = false;
    this.success = false;
    this.withdraw_ = false;
    this.accountStatement_ = false;
    this.deposit_ = false;
    this.getAccount_ = !this.getAccount_;
    this.getCustomer_ = false;
    this.transfer_ = false;
  }

  getCustomer() {
    this.error = false;
    this.success = false;
    this.withdraw_ = false;
    this.accountStatement_ = false;
    this.deposit_ = false;
    this.getAccount_ = false;
    this.getCustomer_ = !this.getCustomer_;
    this.transfer_ = false;
  }

  accountStatement() {
    this.error = false;
    this.success = false;
    this.withdraw_ = false;
    this.accountStatement_ = !this.accountStatement_;
    this.deposit_ = false;
    this.getAccount_ = false;
    this.getCustomer_ = false;
    this.transfer_ = false;
  }

  refresh() {
    if (this.successMessage.includes("Get Customer Successfull")) {
      this.getCustomer_ = false;
    }
    if (this.errorMessage.includes("Customer not found")) {
      this.getCustomer_ = true;
    }
    if (this.errorMessage.includes("Session Expired Please Login again")) {

      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      let token = localStorage.getItem("token");
      if (token == undefined || token === '' || token == null) {
        this.loggedIn = false;
        this.employee = false;
      }
      this.router.navigate(['/home']);
      window.location.reload();
    }
  }


  onSubmitGetAccount(getAcc: any) {
    this.accountId = getAcc.value.accountId;
    console.log(this.accountId);
    this.customerService.getAccount(this.accountId).subscribe(
      (response: any) => {
        console.log(response);
        this.success = true;
        this.error = false;
        this.successMessage = `Your account ID: ${response.accountId}` + "<br><br>" + `Your current balance: ${response.balance}`;
      }, err => {
        this.error = true;
        this.success = false;
        if (err.error.message.includes("JWT expired")) {
          this.errorMessage = "*Session Expired Please Login again*"
        }
        if (err.error.message.includes("Account Not Found")) {
          this.errorMessage = "*Account Not Found*";
        }
      }
    );
  }


  onSubmitDeposit(deposit: any) {
    this.customerService.deposit(deposit.value.accountId, deposit.value.amount).subscribe(
      (response: any) => {
        this.error = false;
        console.log(response);
        this.success = true;

        let userId = localStorage.getItem('userid');
        if (Number(deposit.value.accountId.substring(5)) == Number(userId?.substring(4))) {
          this.successMessage = `${response.message}` + "<br><br>" + `Your source balance: ${response.sourceBalance}` + "<br><br>" + `Your destination balance: ${response.destinationBalance}`;
        } else {
          this.successMessage = `${response.message}`;
        }
      }, err => {
        this.error = true;
        this.success = false;
        if (err.error.message.includes("JWT expired")) {
          this.errorMessage = "*Session Expired Please Login again*"
        }
        if (err.error.message.includes("Account Not Found")) {
          this.errorMessage = "*Account Not Found*";
        }

      }
    );
  }

  onSubmitWithdraw(withdraw: any) {
    this.customerService.withdraw(withdraw.value.accountId, withdraw.value.amount).subscribe(
      (response: any) => {
        this.error = false;
        console.log(response);
        this.success = true;
        this.successMessage = `${response.message}` + "<br><br>" + `Your source balance: ${response.sourceBalance}` + "<br><br>" + `Your destination balance: ${response.destinationBalance}`;
      }, err => {
        this.error = true;
        this.success = false;
        if (err.error.message.includes("Account Not Found")) {
          this.errorMessage = "*Account Not Found*";
        }
        if (err.error.message.includes("JWT expired")) {
          this.errorMessage = "*Session Expired Please Login again*"
        }
        if (err.error.message.includes("No enough balance for proceeding this transaction")) {
          this.errorMessage = "*No enough balance for proceeding this transaction*"
        }

      }
    );
  }


  onSubmitTransfer(transfer: any) {
    console.log(transfer);
    this.customerService.transfer(transfer.value.fromAccountId, transfer.value.toAccountId, transfer.value.amount).subscribe(
      (response: any) => {
        console.log(response);
        this.success = true;
        this.error = false;
        this.successMessage = `${response.message}` + "<br><br>" + `Your source balance: ${response.sourceBalance}` + "<br><br>" + `Your destination balance: ${response.destinationBalance}`;
      }, err => {
        this.error = true;
        this.success = false;
        if (err.error.message.includes("Account Not Found")) {
          this.errorMessage = "*Account Not Found*";
        }
        if (err.error.message.includes("JWT expired")) {
          this.errorMessage = "*Session Expired Please Login again*"
        }
        if (err.error.message.includes("Not Allowed")) {
          this.errorMessage = "*Source and destination Account ID should not be same*"
        }
        if (err.error.message.includes("No enough balance for proceeding this transaction")) {
          this.errorMessage = "*No enough balance for proceeding this transaction*"
        }

      }
    );
  }

  onSubmitAccountStatement(statement: any) {
    if (statement.value.fromDate != null && statement.value.toDate != null) {
      this.customerService.getAccountStatementWithDates(statement.value.accountId, statement.value.fromDate, statement.value.toDate).subscribe(
        (response: any) => {
          console.log(response);
          this.success = true;
          this.error = false;
          this.thDate = response.date;
          this.thAccountId = response.accountId;
          this.thCurrentBalance = response.currentBalance;
          while(this.transactionHistory.length > 0) {
            this.transactionHistory.pop();
          }
          for (let i in response.history) {
            let history: TransactionRecord = {
              id: 0,
              tId: "",
              type: "",
              sId: "",
              dId: "",
              amount: 0,
              date: new Date(),
              status: "",
              sBalance: 0,
              dBalance: 0
            };
            history.id = response.history[i].id;
            history.tId = response.history[i].transactionId;
            history.type = response.history[i].transactionType;
            history.sId = response.history[i].sourceAccountId;
            history.dId = response.history[i].destinationAccountId;
            history.amount = response.history[i].amount;
            history.date = response.history[i].dateOfTransaction;
            history.status = response.history[i].transactionStatus;
            history.sBalance = response.history[i].sourceBalance;
            history.dBalance = response.history[i].destinationBalance;
            this.transactionHistory.push(history);
            if(this.transactionHistory.length == 0) {
              this.noRecords = true;
            } else {
              this.noRecords = false;
            }
          }
        }, err => {
          this.error = true;
          this.success = false;
          if (err.error.message.includes("Account Not Found")) {
            this.errorMessage = "*Account Not Found*";
          }
          if (err.error.message.includes("JWT expired")) {

            this.errorMessage = "*Session Expired Please Login again*"
          }

        }
      );
    } else {
      this.customerService.getAccountStatementWithoutDates(statement.value.accountId).subscribe(
        (response: any) => {
          console.log(response);
          this.success = true;
          this.error = false;
          this.thDate = response.date;
          this.thAccountId = response.accountId;
          this.thCurrentBalance = response.currentBalance;
          while(this.transactionHistory.length > 0) {
            this.transactionHistory.pop();
          }
          for (let i in response.history) {
            let history: TransactionRecord = {
              id: 0,
              tId: "",
              type: "",
              sId: "",
              dId: "",
              amount: 0,
              date: new Date(),
              status: "",
              sBalance: 0,
              dBalance: 0
            };
            history.id = response.history[i].id;
            history.tId = response.history[i].transactionId;
            history.type = response.history[i].transactionType;
            history.sId = response.history[i].sourceAccountId;
            history.dId = response.history[i].destinationAccountId;
            history.amount = response.history[i].amount;
            history.date = response.history[i].dateOfTransaction;
            history.status = response.history[i].transactionStatus;
            history.sBalance = response.history[i].sourceBalance;
            history.dBalance = response.history[i].destinationBalance;
            this.transactionHistory.push(history);
          }
          if(this.transactionHistory.length == 0) {
            this.noRecords = true;
          } else {
            this.noRecords = false;
          }
        }, err => {
          this.error = true;
          this.success = false;
          if (err.error.message.includes("Account Not Found")) {
            this.errorMessage = "*Account Not Found*";
          }
          if (err.error.message.includes("JWT expired")) {

            this.errorMessage = "*Session Expired Please Login again*"
          }

        }
      );
    }
  }


  getCustomerDetails(getDetails: any) {

    this.customerDetails.customerId = getDetails.value.customerId;
    this.customerService.getCustomerDetails(this.customerDetails.customerId).subscribe(
      (response: any) => {
        console.log(response);
        this.customerDetails.customerId = response.customerId;
        this.customerDetails.customerName = response.customerName;
        this.customerDetails.address = response.address;
        this.customerDetails.dateOfBirth = response.dateOfBirth;
        this.customerDetails.gender = response.gender;
        this.customerDetails.panNo = response.panNo;
        this.customerDetails.accounts[0].accountId = response.accounts[0].accountId;
        this.customerDetails.accounts[0].balance = response.accounts[0].balance;
        this.customerDetails.accounts[1].accountId = response.accounts[1].accountId;
        this.customerDetails.accounts[1].balance = response.accounts[1].balance;
        this.successMessage = "Get Customer Successfull";
        this.success = true;
        this.error = false;
      },
      err => {
        console.log(err);
        this.errorMessage = err.error.message;
        this.error = true;
        this.success = false;
        if (this.errorMessage.includes("JWT expired")) {

          this.errorMessage = "*Session Expired Please Login again*"
        }
      });

  }

}
