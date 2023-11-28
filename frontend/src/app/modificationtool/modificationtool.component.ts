import { Component, EventEmitter, Output, ElementRef, ViewChild, OnInit } from "@angular/core";
import { modificationtoolData } from "./tool-data";
import {ComponentData} from "../models/userdata";
import {UserDataService} from "../services/userdata.service";


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

  constructor(private UserDataService: UserDataService) {}

  // Define your variables for element information
  selectedElement: any;
  elementInfo: string = '';
  placeholderInfo: string = '';
  nameInfo: string = '';
  typeInfo: string = '';
  tagName: string = '';
  components?: ComponentData[];

  ngOnInit(): void {
    this.UserDataService.getComponents().subscribe((data: ComponentData[]) => {
      console.log(data);
      this.components = data;
  });
  }


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

        this.elementInfo = `Tag Name: ${tagInfo}\nPlaceholder: ${placeholder}\nClasses: ${classes}`;
        this.placeholderInfo = `${placeholder}`;
        this.tagName = `${tagInfo}`;
        this.typeInfo = `${type}`;
        this.nameInfo = `${name}`;
      }
    } else {
      this.elementInfo = '';
      this.placeholderInfo = '';
      this.nameInfo = '';
      this.typeInfo = '';
      this.nameInfo = '';
    }
  }

  protected readonly name = name;
  protected readonly ComponentData = ComponentData;
}
