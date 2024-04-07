
import { Expedientes } from "./expedientes.model"

export interface Actuaciones {
		descripcion: string,
		descripcionNueva: string,
		finalizado: number,
        fecha: Date,
		modalidad: string,
		activo: number
		expediente: Expedientes
}
