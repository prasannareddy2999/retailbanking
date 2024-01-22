import { Component, ElementRef, OnInit } from '@angular/core';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  credentials={
    userid:'',
    password:''
  }

  error:boolean=false;
  errorMessage="";
  
  href="";
  role="";
  customer: boolean = false;
  employee: boolean = false;


  constructor(private loginService:LoginService, private elementRef: ElementRef,private router: Router) { }

  ngOnInit(): void {

    if(this.credentials.userid.includes('EM')){
      this.employee = true;
      this.role = "Employee Login";
      this.customer = false;
    }
    else if(this.credentials.userid.includes('CU')){
      this.customer = true;
      this.role = "Customer Login";
      this.employee = false;
    }
  }


  onSubmit(){

    // if(this.credentials.userid.includes('EM')){
    //   console.log("employee");
    // }

    if(this.credentials.userid.includes('EM')){
      let user={
        "userid":this.credentials.userid,
        "password":this.credentials.password,
        "role":"EMPLOYEE",
      };
      this.loginService.generateToken(user).subscribe(
        (response:any)=>{
        console.log(response);
        window.localStorage.setItem("token",response.authToken);
        window.localStorage.setItem("userid",response.userid);
        window.location.href='/employeedashboard';
      },err=>{
        this.error=true;
        this.errorMessage="Invalid Credentials";
        console.log(err);
      });
    }
    else{
      if(this.credentials.userid.includes('CU')){
        let user={
          "userid":this.credentials.userid,
          "password":this.credentials.password,
          "role":"CUSTOMER",
        };
        this.loginService.generateToken(user).subscribe(
         (response:any)=>{
          window.localStorage.setItem("token",response.authToken);
          window.localStorage.setItem("userid",response.userid);
          window.location.href='/customerdashboard';
          console.log(response);
        },err=>{
          this.error=true;
          this.errorMessage="Invalid Credentials";
          console.log(err);
        });
      }
    }

    //console.log("Submit");

  }

}
