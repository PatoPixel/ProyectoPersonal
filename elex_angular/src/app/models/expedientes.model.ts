import { Tiposexpediente } from "./tiposexpediente.model";

export interface Expedientes {
    codigo: string,
    codigoNuevo: string,
    fecha: Date,
    situacion: string,
    opciones: string,
    descripcion: string,
    prioridad: string,
    ubicacion: string,
    tipoExpediente: Tiposexpediente
    activo: number,

}
