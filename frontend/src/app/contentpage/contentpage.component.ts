import {Component, OnInit} from "@angular/core";
import {ComponentData} from "../models/userdata";
import {UserDataService} from "../services/userdata.service";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";

@Component({
  selector: 'app-contentpage',
  templateUrl: './contentpage.component.html',
  styleUrls: ['./contentpage.component.scss']
})
export class ContentpageComponent implements OnInit {

  constructor(private UserDataService: UserDataService, private sanitizer: DomSanitizer) {}

  components?: ComponentData[];

  getSafeHtml(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
  ngOnInit(): void {
    this.UserDataService.getComponents().subscribe((data: ComponentData[]) => {
      console.log(data);
      this.components = data;
    });
  }

}
