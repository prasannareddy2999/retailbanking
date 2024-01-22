import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http: HttpClient) { }

  customerUrl = "http://localhost:9095/customer";
  accountUrl = "http://localhost:9096/account";
  transactionUrl = "http://localhost:9094/transaction"

  getAccount(accountId: String) {
    return this.http.get(`${this.accountUrl}/getAccount/${accountId}`);
  }

  deposit(accountId: String, amount: Number) {
    return this.http.post(`${this.transactionUrl}/deposit/${accountId}/${amount}`, "");
  }

  withdraw(accountId: String, amount: Number) {
    return this.http.post(`${this.transactionUrl}/withdraw/${accountId}/${amount}`, "")
  }

  transfer(fromAccountId: String, toAccountId: String, amount: Number) {
    return this.http.post(`${this.transactionUrl}/transfer/${fromAccountId}/${toAccountId}/${amount}`, "")
  }

  getAccountStatementWithDates(accountId: String, fromDate: Date, toDate: Date) {
    return this.http.get(`${this.accountUrl}/getAccountStatement/${accountId}/${fromDate}/${toDate}`);
  }

  getAccountStatementWithoutDates(accountId: String) {
    return this.http.get(`${this.accountUrl}/getAccountStatement/${accountId}`);
  }

  getCustomerDetails(customerId: string) {
    return this.http.get(`${this.customerUrl}/getCustomerDetails/${customerId}`);
  }

  getServiceCharges(customerId: string) {
    return this.http.get(`${this.transactionUrl}/getServiceCharges/${customerId}`);
  }

}
