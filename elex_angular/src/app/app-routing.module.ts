import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormulariosTiposComponent } from './components/formularios-tipos/formularios-tipos.component';
import { IndexComponent } from './components/index/index.component';
import { ExpedientesComponent } from './components/expedientes/expedientes.component';

const routes: Routes = [
  {
    path: "",
    pathMatch: "full",
    redirectTo: "login",
  },
   {
     path: "tiposexpediente",
     component: FormulariosTiposComponent,
  },
  {
    path: "expedientes",
    component: ExpedientesComponent,
  },
  {
    path: "index",
    component: IndexComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
