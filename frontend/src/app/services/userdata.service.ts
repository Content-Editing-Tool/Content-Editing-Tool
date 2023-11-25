import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ComponentData } from '../models/userdata';

@Injectable({
  providedIn: 'root'
})
export class UserDataService {

  private baseUrl = "http://localhost:8080/component/components";

  constructor(private http: HttpClient) { }

  getComponents(): Observable<ComponentData[]>{
    return this.http.get<ComponentData[]>(`${this.baseUrl}`);
  }
}
