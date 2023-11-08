import { Component, EventEmitter, Output, ElementRef, ViewChild } from "@angular/core";
import { modificationtoolData } from "./tool-data";

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-modificationtool',
  templateUrl: './modificationtool.component.html',
  styleUrls: ['./modificationtool.component.scss']
})
export class ModificationtoolComponent {

  @Output() onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter();
  collapsed = true;
  screenWidth = 0;
  toolData = modificationtoolData;

  // Define your variables for element information
  selectedElement: any;
  elementInfo: string = '';
  placeholderInfo: string = '';
  nameInfo: string = '';
  typeInfo: string = '';

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
        const tagName = this.selectedElement.tagName;
        const placeholder = this.selectedElement.placeholder;
        const type = this.selectedElement.type;
        const classes = Array.from(this.selectedElement.classList).join(' ');

        this.elementInfo = `Tag Name: ${tagName}\nPlaceholder: ${placeholder}\nClasses: ${classes}`;
        this.placeholderInfo = `${placeholder}`;
        this.nameInfo = `${tagName}`;
        this.typeInfo = `${type}`;
      }
    } else {
      this.elementInfo = '';
      this.placeholderInfo = '';
      this.nameInfo = '';
      this.typeInfo = '';
    }
  }

  protected readonly name = name;
}
