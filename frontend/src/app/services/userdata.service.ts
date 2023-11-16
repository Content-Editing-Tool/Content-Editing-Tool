import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserData } from '../models/userdata';

@Injectable({
  providedIn: 'root'
})
export class UserDataService {

  private baseUrl = "http://localhost:8080/";

  constructor(private http: HttpClient) { }

  getUsers(): Observable<UserData[]>{
    return this.http.get<UserData[]>(`${this.baseUrl}`);
  }
}
