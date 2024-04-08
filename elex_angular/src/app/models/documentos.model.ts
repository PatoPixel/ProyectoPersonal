import { Expedientes } from "./expedientes.model"

export interface Documentos {

    ruta: string
    rutaNueva:string
    tarifa: number
    categoria: string
    expediente: Expedientes
    activo: number
}
