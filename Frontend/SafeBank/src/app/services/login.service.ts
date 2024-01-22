import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { map } from 'rxjs/operators'

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  //isAuthenticated = new Subject<boolean>();
  clearTimer:any;

  url="http://localhost:9092/auth";

  constructor(private http:HttpClient, private router:Router) { }


  //calling server to generate token

  generateToken(user:any){

    return this.http.post(`${this.url}/login`,user);

  }


  //to check user is login or not
  isLoggedIn(){

    let token = localStorage.getItem("token");
    if(token == undefined || token === '' || token == null)
    {

      return false;
    }
    else
    {
      return true;
    }

  }


  //to logout the user
  logout() {
    localStorage.removeItem('userid');
    localStorage.removeItem('token');
    if(this.clearTimer){
      clearTimeout(this.clearTimer);
      this.router.navigate(['/login']);
    }
    location.reload();
  }


  autoLogout(expirationDate: number){
    console.log(expirationDate);
    this.clearTimer=setTimeout(()=>{
      this.logout();
    },expirationDate);
  }

  //to get the token
  getToken(){

    return localStorage.getItem('token');
  }

}
