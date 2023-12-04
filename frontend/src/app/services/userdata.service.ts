import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ComponentData } from '../models/userdata';

@Injectable({
  providedIn: 'root'
})
export class UserDataService {

  private baseUrl = "http://localhost:8080/attributes";
  private baseUrlTest = "http://localhost:8080/component/components";
  private baseUrlTest2 = "http://localhost:8080/requestChanges";

  constructor(private http: HttpClient) { }

  getComponent(): Observable<ComponentData>{
    return this.http.get<ComponentData>(`${this.baseUrl}`);
  }

  sendComponent(ComponentData: ComponentData | undefined): Observable<ComponentData>{
    return this.http.post<ComponentData>(`${this.baseUrlTest2}`, ComponentData?.pageCode);
  }

  getComponentTest(): Observable<ComponentData[]>{
    return this.http.get<ComponentData[]>(`${this.baseUrlTest}`);
  }
}
