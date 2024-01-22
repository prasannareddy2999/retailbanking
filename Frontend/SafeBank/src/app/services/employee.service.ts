import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  baseUrl = 'http://localhost:9095/customer';

  constructor(private http:HttpClient) { }


  createCustomer(customer:any){

    return this.http.post(`${this.baseUrl}/createCustomer`,customer);
  }

  getCustomerDetails(customerId:string){
    return this.http.get(`${this.baseUrl}/getCustomerDetails/${customerId}`);
  }
}