import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Documentos } from '../models/documentos.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentosService {

  private urlBase = `http://localhost:8100/documentos`;

  constructor(private http: HttpClient) { }

  consultarDocumentos(): Observable<Documentos[]> {
    return this.http.get<Documentos[]>(`${this.urlBase}/consultar`);
  } 

  consultarDocumentosCodigo(codigo: string): Observable<Documentos[]> {
    return this.http.get<Documentos[]>(`${this.urlBase}/consultar/${codigo}`);
  }

  // insertarDocumentos(Documentos: Documentos, materiaNueva: string): Observable<Documentos> {
  //   return this.http.post<Documentos>(`${this.urlBase}/insertar/${Documentos.codigo}/${Documentos.fecha}/${Documentos.situacion}/${Documentos.opciones}/${Documentos.descripcion}/${Documentos.prioridad}/${Documentos.ubicacion}/${materiaNueva}/${Documentos.activo}`, {});
  // }
  insertarDocumentos(Documentos: Documentos, materiaNueva: string): Observable<Documentos> {
    return this.http.post<Documentos>(`${this.urlBase}/insertar/${Documentos.ruta}/${Documentos.tarifa}/${Documentos.categoria}/${materiaNueva}/${Documentos.activo}`, {});
  }

   actualizarDocumentos(Documentos: Documentos, materiaNueva:string): Observable<Documentos> {
     return this.http.post<Documentos>(`${this.urlBase}/actualizar/${Documentos.ruta}/${Documentos.rutaNueva}/${Documentos.tarifa}/${Documentos.categoria}/${materiaNueva}/${Documentos.activo}`, {});
   }

  borrarLogicamenteDocumentos(codigo: string): Observable<Documentos> {
    return this.http.post<Documentos>(`${this.urlBase}/borrarlogico/${codigo}`, {});
  }
  activarDocumentos(codigo: string): Observable<Documentos> {
    return this.http.post<Documentos>(`${this.urlBase}/activar/${codigo}`, {});
  }

  borrarFisicamenteDocumentos(codigo: string): Observable<Documentos> {
    return this.http.post<Documentos>(`${this.urlBase}/borradofisico/${codigo}`, {});
  }
}
