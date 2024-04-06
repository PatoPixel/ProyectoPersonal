import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Expedientes } from '../models/expedientes.model';

@Injectable({
  providedIn: 'root'
})
export class ExpedientesService {

  private urlBase = `http://localhost:8100/expedientes`;

  constructor(private http: HttpClient) { }

  consultarExpedientes(): Observable<Expedientes[]> {
    return this.http.get<Expedientes[]>(`${this.urlBase}/consultar`);
  } 

  consultarExpedientesCodigo(codigo: string): Observable<Expedientes[]> {
    return this.http.get<Expedientes[]>(`${this.urlBase}/consultar/${codigo}`);
  }
  
  insertarExpedientes(expedientes: Expedientes): Observable<Expedientes> {
    return this.http.post<Expedientes>(`${this.urlBase}/insertar/${expedientes.codigo}/${expedientes.fecha}/${expedientes.situacion}/${expedientes.opciones}/${expedientes.descripcion}/${expedientes.codigo}/${expedientes.activo}`, {});
  }

  actualizarExpedientes(expedientes: Expedientes): Observable<Expedientes> {
    return this.http.post<Expedientes>(`${this.urlBase}/actualizar/${expedientes.codigo}/${expedientes.codigoNuevo}/${expedientes.fecha}/${expedientes.situacion}/${expedientes.opciones}/${expedientes.descripcion}/${expedientes.codigo}/${expedientes.activo}`, {});
  }

  borrarLogicamenteExpedientes(codigo: string): Observable<Expedientes> {
    return this.http.post<Expedientes>(`${this.urlBase}/borrarlogico/${codigo}`, {});
  }

  borrarFisicamenteExpedientes(codigo: string): Observable<Expedientes> {
    return this.http.post<Expedientes>(`${this.urlBase}/borradofisico/${codigo}`, {});
  }
}
