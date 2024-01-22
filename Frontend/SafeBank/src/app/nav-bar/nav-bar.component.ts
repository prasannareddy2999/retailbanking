import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../services/customer.service';
import { LoginService } from '../services/login.service';
import { ServiceCharge } from '../services/servicecharge';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  public loggedIn = false;

  public customer = false;

  public employee = false;

  public success = false;
  public successMessage = '';

  sCharge = 0;
  cCharge = 0;

  savingsCharges: ServiceCharge = {
    accountId: '',
    message: '',
    balance: 0
  }
  currentCharges: ServiceCharge = {
    accountId: '',
    message: '',
    balance: 0
  }

  user: any = localStorage.getItem('userid');

  error: boolean = false;
  errorMessage: String = '';
  serviceCharges: ServiceCharge[] = [];

  constructor(private loginService: LoginService, private customerService: CustomerService, private router:Router) { }

  ngOnInit(): void {
    this.loginService.autoLogout(1000 * 60 * 15);
    this.loggedIn = this.loginService.isLoggedIn();
    if (this.user.includes('EM')) {
      this.employee = true;
    }
    else if (this.user.includes('CU')) {
      this.customer = true;
    }
  }

  refresh() {
    if (this.errorMessage.includes("Session Expired Please Login again")) {

      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      let token = localStorage.getItem("token");
      if (token == undefined || token === '' || token == null) {
        this.loggedIn = false;
      }
      this.router.navigate(['/login']);
      window.location.reload();
    }
  }

  logoutUser() {
    this.loginService.logout();
    location.reload();
  }

  getCharges() {
    this.success = true;
    this.customerService.getServiceCharges(this.user).subscribe(
      (response: any) => {
        this.success = true;
        this.error = false;
        this.savingsCharges.accountId = response[0].accountId,
        this.savingsCharges.message = response[0].message;
        this.savingsCharges.balance = response[0].balance;
        this.sCharge = 5000 - Number(this.savingsCharges.balance);
        this.currentCharges.accountId = response[1].accountId,
        this.currentCharges.message = response[1].message;
        this.currentCharges.balance = response[1].balance;
        this.cCharge = 11000 - Number(this.currentCharges.balance);
      }, err => {
        if(err.error.message.includes("JWT expired")) {
          this.error  = true;
          this.success = false;
          this.errorMessage = "*Session Expired Please Login again*"; 
        }
      }
    );
  }


}
