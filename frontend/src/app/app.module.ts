import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpClientModule } from  '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {ModificationtoolComponent} from "./modificationtool/modificationtool.component";
import {ContentpageComponent} from "./contentpage/contentpage.component";

@NgModule({
  declarations: [
    AppComponent,
    ModificationtoolComponent,
    ContentpageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
