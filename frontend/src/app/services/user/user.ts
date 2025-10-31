import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LoginUser, RegistrationUser } from '../../models/User';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Signer } from '../../models/Envelope';
import { AccountModel } from '../../models/AccountModel';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient){}

  createUser(user: RegistrationUser){
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });
    const body = new HttpParams()
      .set('name', user.name)
      .set('email', user.email)
      .set('password', user.password);
    return this.http.post(`${this.baseUrl}/newuser`, body.toString(), { headers: headers });
  }

  getName(): Observable<string>{
    return this.http.get(`${this.baseUrl}/getname`, { responseType: 'text' });
  }

  updatePassword(newPassword: string) {
    return this.http.patch(
      `${this.baseUrl}/updatepassword?password=${encodeURIComponent(newPassword)}`,
      {}
    );
  }

  updateName(newName: string) {
    return this.http.patch(
      `${this.baseUrl}/updatename?name=${encodeURIComponent(newName)}`,
      {}
    );
  }

  updateEmail(newEmail: string) {
    return this.http.patch(
      `${this.baseUrl}/updateemail?email=${encodeURIComponent(newEmail)}`,
      {}
    );
  }

  getAccounts(): Observable<AccountModel[]>{
    return this.http.get<AccountModel[]>(`${this.baseUrl}/getaccounts`);
  }

  deleteUser(){
    return this.http.delete(`${this.baseUrl}/deleteuser`);
  }

  login(user: LoginUser){
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });
    const body = new HttpParams()
      .set('email', user.email)
      .set('password', user.password);
    return this.http.post('http://localhost:8080/api/auth/login', body.toString(), { headers: headers });
  }

  getEnvelopes(): Observable<Signer[]>{
    return this.http.get<Signer[]>(`${this.baseUrl}/getenvelopes`);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwt');
  }

  logout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('userEmail');
  }
}
