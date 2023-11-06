import {Component, EventEmitter, Output} from "@angular/core";
import {modificationtoolData} from "./tool-data";

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
@Component({
  selector: 'app-modificationtool',
  templateUrl: './modificationtool.component.html',
  styleUrls: ['./modificationtool.component.scss']
})
export class ModificationtoolComponent{

  @Output() onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter();
  collapsed = true;
  screenWidth= 0;
  toolData = modificationtoolData

  toggleCollapse(): void {
    this.collapsed = !this.collapsed;
    this.onToggleSideNav.emit({collapsed: this.collapsed, screenWidth: this.screenWidth});
  }

  closeSidenav(): void {
    this.collapsed = false;
    this.onToggleSideNav.emit({collapsed: this.collapsed, screenWidth: this.screenWidth});
  }
}
