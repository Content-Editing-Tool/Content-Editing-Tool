import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SharedService {
  private elementSelectedSource = new Subject<HTMLElement>();
  elementSelected$ = this.elementSelectedSource.asObservable();

  selectElement(element: HTMLElement) {
    this.elementSelectedSource.next(element);
  }
}
