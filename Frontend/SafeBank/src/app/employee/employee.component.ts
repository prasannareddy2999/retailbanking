import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../services/employee.service';
import { Router } from '@angular/router';
import * as moment from 'moment';

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {


  now = new Date();
  year = this.now.getFullYear();
  month = this.now.getMonth();
  day = this.now.getDay();
  maxDate = moment({year: this.year - 18, month: this.month, day: this.day}).format('YYYY-MM-DD');


  createCustomer_: boolean = false;
  getCustomer_: boolean = false;

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

  success: boolean = false;
  successMessage = "";


  error: boolean = false;
  errorMessage = "";


  constructor(private employeeService: EmployeeService, private router: Router) { }

  ngOnInit(): void {
  }

  create() {
    this.createCustomer_ = !this.createCustomer_;
    this.getCustomer_ = false;
  }

  getCustomer() {
    this.getCustomer_ = !this.getCustomer_;
    this.createCustomer_ = false;
  }

  refresh() {
    if (this.successMessage.includes("Customer Created Successfully")) {
      this.getCustomer_ = false;
      this.createCustomer_ = false;
    }

    if (this.successMessage.includes("Get Customer Successfull")) {
      this.getCustomer_ = false;
      this.createCustomer_ = false;
    }



    if (this.errorMessage.includes("Customer already exist")) {
      this.createCustomer_ = true;
      this.getCustomer_ = false;
    }
    if (this.errorMessage.includes("Customer not found")) {
      this.getCustomer_ = true;
      this.createCustomer_ = false;
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

  onSubmit(details: any) {

    let customer = {
      "customerName": details.value.customerName,
      "address": details.value.address,
      "dateOfBirth": details.value.dateOfBirth,
      "panNo": details.value.panNo,
      "gender": details.value.gender,
      "password": details.value.password
    };
    this.employeeService.createCustomer(customer).subscribe(
      (response: any) => {
        console.log(response);
        this.successMessage = response.message;
        this.customerDetails.customerId = response.customerId;
        this.error = false;
        this.success = true;
      },
      err => {
        console.log(err);
        this.errorMessage = "*" + err.error.message + "*";
        this.error = true;
        this.success = false;
        if (this.errorMessage.includes("JWT expired")) {

          this.errorMessage = "*Session Expired Please Login again*"
        }
      });


  }

  getCustomerDetails(getDetails: any) {

    this.customerDetails.customerId = getDetails.value.customerId;
    this.employeeService.getCustomerDetails(this.customerDetails.customerId).subscribe(
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
        this.errorMessage = "*" + err.error.message + "*";
        this.error = true;
        this.success = false;
        if (this.errorMessage.includes("JWT expired")) {

          this.errorMessage = "*Session Expired Please Login again*"
        }
      });

  }


}