import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Actuaciones } from '../models/actuaciones.model';
@Injectable({
  providedIn: 'root'
})
export class ActuacionesService {

  private urlBase = `http://localhost:8100/actuaciones`;

  constructor(private http: HttpClient) { }

  consultarActuaciones(): Observable<Actuaciones[]> {
    return this.http.get<Actuaciones[]>(`${this.urlBase}/consultar`);
  } 

  consultarActacionesDescripcionYFecha(codigo: string, fecha: string): Observable<Actuaciones[]> {
    return this.http.get<Actuaciones[]>(`${this.urlBase}/consultar/${codigo}/${fecha}`);
  }


   insertarActuaciones(Actuaciones: Actuaciones, materiaNueva: string): Observable<Actuaciones> {
    return this.http.post<Actuaciones>(`${this.urlBase}/insertar/${Actuaciones.descripcion}/${Actuaciones.finalizado}/${Actuaciones.modalidad}/${Actuaciones.fecha}/${materiaNueva}/${Actuaciones.activo}`, {});
   }

  //  insertarActuaciones(Actuaciones: Actuaciones, materiaNueva: string): Observable<Actuaciones> {
  //   return this.http.post<Actuaciones>(`${this.urlBase}/insertar/${Actuaciones.descripcion}/${Actuaciones.finalizado}/${Actuaciones.modalidad}/${Actuaciones.fecha}/${materiaNueva}/${Actuaciones.activo}`, {});
  //  }

  actualizarActuaciones(Actuaciones: Actuaciones, materia:string, materiaNueva:string): Observable<Actuaciones> {
    return this.http.post<Actuaciones>(`${this.urlBase}/actualizar/${Actuaciones.descripcion}/${Actuaciones.descripcionNueva}/${materia}/${materiaNueva}/${Actuaciones.finalizado}/${Actuaciones.modalidad}/${Actuaciones.fecha}/${Actuaciones.activo}`, {})
  }

  borrarLogicamenteActuaciones(codigo: string , expediente: string): Observable<Actuaciones> {
    return this.http.post<Actuaciones>(`${this.urlBase}/borrarlogico/${codigo}/${expediente}`, {});
  }
  activarActuaciones(codigo: string , expediente: string): Observable<Actuaciones> {
    return this.http.post<Actuaciones>(`${this.urlBase}/activar/${codigo}/${expediente}`, {});
  }

  borrarFisicamenteActuaciones(codigo: string, expediente: string): Observable<Actuaciones> {
    return this.http.post<Actuaciones>(`${this.urlBase}/borrarfisico/${codigo}/${expediente}`, {});
  }
  
}
