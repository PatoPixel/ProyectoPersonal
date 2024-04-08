import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormulariosTiposComponent } from './components/formularios-tipos/formularios-tipos.component';
import { IndexComponent } from './components/index/index.component';
import { ExpedientesComponent } from './components/expedientes/expedientes.component';
import { ActuacionesComponent } from './components/actuaciones/actuaciones.component';
import { DocumentosComponent } from './components/documentos/documentos.component';
import { LoginComponent } from './components/login/login.component';
import { AutenticacionGuard } from './guards/autenticacion.guard';
import { ConsultaComponent } from './components/consulta/consulta.component';

const routes: Routes = [
  {
    path: "",
    pathMatch: "full",
    redirectTo: "login",
  },
  {
    path: "documentos",
    component: DocumentosComponent,
  },
  {
    path: "index",
    component: IndexComponent,
  },
   {
     path: "tiposexpediente",
     component: FormulariosTiposComponent,
     canActivate: [AutenticacionGuard]
  },
  {
    path: "expedientes",
    component: ExpedientesComponent,
    canActivate: [AutenticacionGuard]
  },
  {
    path: "actuaciones",
    component: ActuacionesComponent,

  },
  {
    path: "consulta",
    component: ConsultaComponent,

  },
  
  {
    path: "login",
    component: LoginComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
