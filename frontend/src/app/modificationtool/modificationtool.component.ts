import { Component, EventEmitter, Output, ElementRef, ViewChild, OnInit } from "@angular/core";
import { modificationtoolData } from "./tool-data";
import {ComponentData} from "../models/userdata";
import {UserDataService} from "../services/userdata.service";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";


interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-modificationtool',
  templateUrl: './modificationtool.component.html',
  styleUrls: ['./modificationtool.component.scss']
})
export class ModificationtoolComponent implements OnInit{

  @Output() onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter();
  collapsed = true;
  screenWidth = 0;
  toolData = modificationtoolData;

  constructor(private UserDataService: UserDataService,  private sanitizer: DomSanitizer) {}

  // Define your variables for element information
  selectedElement: any;
  elementInfo: string = '';
  placeholderInfo: string = '';
  nameInfo: string = '';
  typeInfo: string = '';
  tagName: string = '';
  textContent: string = '';
  component?: ComponentData;
  htmlContent: string = '';
  // components?: ComponentData[];

  getSafeHtml(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }

  jsonToHtml(jsonData: any): string {
    let htmlContent = '';
    for (const key in jsonData) {
      htmlContent += `<h1>${key}</h1>`;
      const value = jsonData[key];
      for (const innerKey in value) {
        htmlContent += `<h2>${innerKey}:</h2>`;
        if (Array.isArray(value[innerKey])) {
          value[innerKey].forEach((item: any) => {
            htmlContent += `<p>${item}</p>`;
          });
        } else if (typeof value[innerKey] === 'object') {
          for (const langKey in value[innerKey]) {
            htmlContent += `<h3>${langKey}:</h3><p contenteditable='true'>${value[innerKey][langKey]}</p>`;
          }
        } else {
          htmlContent += `<p>${value[innerKey]}</p>`;
        }
      }
    }
    return htmlContent;
  }

  htmlToJson(htmlData: string): any {
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlData, 'text/html');
    let jsonResult: { [key: string]: any } = {};

    doc.querySelectorAll('h1').forEach(h1 => {
      const key = h1.textContent?.trim();
      if (!key) return; // Skip if key is null or empty

      let obj: { [key: string]: any } = {};
      let currentElement = h1.nextElementSibling as HTMLElement | null;

      while (currentElement && currentElement.tagName !== 'H1') {
        if (currentElement.tagName === 'H2') {
          const innerKey = currentElement.textContent?.trim().replace(':', '');
          if (!innerKey) {
            currentElement = currentElement.nextElementSibling as HTMLElement | null;
            continue;
          }

          if (innerKey === 'entities') {
            obj[innerKey] = [];
            currentElement = currentElement.nextElementSibling as HTMLElement | null;
            while (currentElement && currentElement.tagName === 'P') {
              obj[innerKey].push(currentElement.textContent?.trim() || '');
              currentElement = currentElement.nextElementSibling as HTMLElement | null;
            }
          } else if (innerKey === 'questionText') {
            obj[innerKey] = {};
            currentElement = currentElement.nextElementSibling as HTMLElement | null;
            while (currentElement && currentElement.tagName === 'H3') {
              const langKey = currentElement.textContent?.trim().replace(':', '');
              if (!langKey) {
                currentElement = currentElement.nextElementSibling as HTMLElement | null;
                continue;
              }

              currentElement = currentElement.nextElementSibling as HTMLElement | null;
              if (currentElement) {
                obj[innerKey][langKey] = currentElement.textContent?.trim() || '';
              }
              // @ts-ignore
              currentElement = currentElement.nextElementSibling as HTMLElement | null;
            }
          }
        }
        // @ts-ignore
        currentElement = currentElement.nextElementSibling as HTMLElement | null;
      }
      jsonResult[key] = obj;
    });

    return jsonResult;
  }



  ngOnInit(): void {
    this.UserDataService.getComponent().subscribe((data: ComponentData) => {
      console.log(data);
      this.component = data;
      this.jsonToHtml(data);
       this.component.pageCode = this.jsonToHtml(data);
  });
  }

  sendModificationRequest(): void {
    if (this.htmlToJson && this.component && this.component.pageCode) {
      const convertedJson = this.htmlToJson(this.component.pageCode);
      console.log(convertedJson);
      if (convertedJson) {
        this.component.pageCode = convertedJson;
        console.log(this.component.pageCode); // Consider removing for production

        this.UserDataService.sendComponent(this.component).subscribe(
          (data: ComponentData) => {

          },
          error => {
            console.error('Error sending component data:', error);
          }
        );
      } else {
        console.error('Conversion to JSON failed');
      }
    } else {
      console.error('Required properties or methods are not defined');
    }
  }
  // ngOnInit(): void {
  //   this.UserDataService.getComponentTest().subscribe((data: ComponentData[]) => {
  //     console.log(data);
  //     this.components = data;
  //   });
  // }


  isSelecting: boolean = false;

  @ViewChild('contentContainer') contentContainer!: ElementRef;

  toggleCollapse(): void {
    this.collapsed = !this.collapsed;
    this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
  }

  closeSidenav(): void {
    this.collapsed = false;
    this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
  }

  selectElement() {
    if (this.isSelecting) {
      this.isSelecting = false; // Deactivate selection
      this.elementInfo = '';
      this.placeholderInfo = '';
      this.nameInfo = '';
      this.typeInfo = '';
    } else {
      this.isSelecting = true; // Activate selection
      this.attachEvent();
    }
  }

  attachEvent() {
    this.contentContainer.nativeElement.addEventListener('click', (event: Event) => {
      event.stopPropagation();
      this.selectedElement = event.target;
      this.updateElementInfo();
    });
  }

  updateElementInfo() {
    if (this.isSelecting) {
      if (this.selectedElement) {
        const tagInfo = this.selectedElement.tagName;
        const placeholder = this.selectedElement.placeholder;
        const type = this.selectedElement.type;
        const classes = Array.from(this.selectedElement.classList).join(' ');
        const name = this.selectedElement.name;
        const textContent = this.selectedElement.textContent?.trim() || ''; // Getting the text content

        this.elementInfo = `Tag Name: ${tagInfo}\nPlaceholder: ${placeholder}\nClasses: ${classes}`;
        this.placeholderInfo = `${placeholder}`;
        this.tagName = `${tagInfo}`;
        this.typeInfo = `${type}`;
        this.nameInfo = `${name}`;
        this.textContent = `${textContent}`;
      }
    } else {
      this.elementInfo = '';
      this.placeholderInfo = '';
      this.nameInfo = '';
      this.typeInfo = '';
      this.nameInfo = '';
      this.textContent = '';
    }
  }

  protected readonly name = name;
  protected readonly ComponentData = ComponentData;
}
