import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
<<<<<<< HEAD
import { HttpClientModule } from  '@angular/common/http';
=======
import {HttpClientModule} from '@angular/common/http';
>>>>>>> 92653d593bfd813afe455a814cc388d28441dd1e
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {ModificationtoolComponent} from "./modificationtool/modificationtool.component";

@NgModule({
  declarations: [
    AppComponent,
    ModificationtoolComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [ModificationtoolComponent]
})
export class AppModule { }
