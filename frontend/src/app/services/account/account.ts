import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountModel } from '../../models/AccountModel';
import { Transaction } from '../../models/Envelope';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private baseUrl = 'http://localhost:8080/api/account';

  constructor(private http: HttpClient){}

  makeTransaction(id: number, amount: number, receiverId: string){
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });
    const body = new HttpParams()
      .set('receiverAccount', receiverId)
      .set('amount', amount);
    return this.http.post(`${this.baseUrl}/${id}/newtransaction`, body.toString(), { headers: headers });
  }

  addBalance(id: number, amount: number){
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });
    const body = new HttpParams()
      .set('amount', amount);
    return this.http.post(`${this.baseUrl}/${id}/addbalance`, body.toString(), { headers: headers });
  }

  addAccount(): Observable<AccountModel>{
    return this.http.post<AccountModel>(`${this.baseUrl}/newaccount`, {});
  }

  getAccount(id: number): Observable<AccountModel>{
    return this.http.get<AccountModel>(`${this.baseUrl}/${id}`);
  }

  getAccountSentTransactions(id: number): Observable<Transaction[]>{
    return this.http.get<Transaction[]>(`${this.baseUrl}/${id}/senttransactions`);
  }

  getAccountReceivedTransactions(id: number): Observable<Transaction[]>{
    return this.http.get<Transaction[]>(`${this.baseUrl}/${id}/receivedtransactions`);
  }

  getAccountBalance(id: number){
    return this.http.get(`${this.baseUrl}/${id}/getaccountbalance`);
  }

  deleteAccount(id: number){
    return this.http.delete(`${this.baseUrl}/${id}/deleteaccount`);
  }
}
