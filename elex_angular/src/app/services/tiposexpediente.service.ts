import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Tiposexpediente } from '../models/tiposexpediente.model';

@Injectable({
  providedIn: 'root'
})
export class TiposexpedienteService {

  private urlBase = `http://localhost:8100/tiposexpediente`;

  constructor(private http: HttpClient) { }

  consultarTipos(): Observable<Tiposexpediente[]> {
    return this.http.get<Tiposexpediente[]>(`${this.urlBase}/consultar`);
  } 

  consultarTiposMateria(materia: string): Observable<Tiposexpediente[]> {
    return this.http.get<Tiposexpediente[]>(`${this.urlBase}/consultar/${materia}`);
  }
  
  insertarTipos(tiposexpediente: Tiposexpediente): Observable<Tiposexpediente> {
    return this.http.post<Tiposexpediente>(`${this.urlBase}/insertar/${tiposexpediente.materia}/${tiposexpediente.activo}`, {});
  }

  actualizarTipos(tiposexpediente: Tiposexpediente): Observable<Tiposexpediente> {
    return this.http.post<Tiposexpediente>(`${this.urlBase}/actualizar/${tiposexpediente.materia}/${tiposexpediente.materianueva}/${tiposexpediente.activo}`, {});
  }

  borrarLogicamenteTipos(materia: string): Observable<Tiposexpediente> {
    return this.http.post<Tiposexpediente>(`${this.urlBase}/borrarlogico/${materia}`, {});
  }

  activarTipos(materia: string): Observable<Tiposexpediente> {
    return this.http.post<Tiposexpediente>(`${this.urlBase}/activar/${materia}`, {});
  }

  borrarFisicamenteTipos(materia: string): Observable<Tiposexpediente> {
    return this.http.post<Tiposexpediente>(`${this.urlBase}/borradofisico/${materia}`, {});
  }
}
