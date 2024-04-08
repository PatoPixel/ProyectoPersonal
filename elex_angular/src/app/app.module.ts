import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormulariosTiposComponent } from './components/formularios-tipos/formularios-tipos.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HeaderComponent } from './components/header/header.component';
import { IndexComponent } from './components/index/index.component';
import { LoginComponent } from './components/login/login.component';
import { ExpedientesComponent } from './components/expedientes/expedientes.component';
import { ActuacionesComponent } from './components/actuaciones/actuaciones.component';
import { DocumentosComponent } from './components/documentos/documentos.component';
import { ConsultaComponent } from './components/consulta/consulta.component';




@NgModule({
  declarations: [
    AppComponent,
    FormulariosTiposComponent,
    HeaderComponent,
    IndexComponent,
    LoginComponent,
    ExpedientesComponent,
    ActuacionesComponent,
    DocumentosComponent,
    ConsultaComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
