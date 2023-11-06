import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ModificationtoolComponent} from "./modificationtool/modificationtool.component";

const routes: Routes = [{
  path: '', component: ModificationtoolComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
