import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Envelope } from '../../models/Envelope';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EnvelopeService {
  private baseUrl = 'http://localhost:8080/api/envelopes';

  constructor(private http: HttpClient){}

  signEnvelope(id: number){
    return this.http.post(`${this.baseUrl}/${id}/sign`, {});
  }

  getEnvelopeById(id: number): Observable<Envelope> {
    return this.http.get<Envelope>(`${this.baseUrl}/${id}`, {});
  }

  checkEnvelopeStatus(id: number): Observable<boolean>{
    return this.http.get<boolean>(`${this.baseUrl}/${id}/checkstatus`, {});
  }
}
